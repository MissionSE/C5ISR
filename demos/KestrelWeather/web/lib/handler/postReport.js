var formidable = require('formidable');
var util = require('util');
var path = require('path');
var fs = require('fs');
var randomstring = require('randomstring');
var _ = require('underscore');
var debug = require('debug')('kestrel:handler');
var postReport = 'postReport';

var stagingArea = path.join(__dirname, '../../public/staging');

module.exports = function(db) {
	return function(req, res) {
		var form = new formidable.IncomingForm(),
			files = [],
			fields = [];

		form.uploadDir = stagingArea;
		form.keepExtensions = true;

		form.on('field', function(field, value) {
			fields.push([field, value]);
		})
		.on('file', function(name, file) {
			files.push(file);
		})
		.on('progress', function(bytesReceived, bytesExpected) {
			debug(postReport, bytesReceived + '/' + bytesExpected);
		})
		.on('end', function() {
			debug(postReport, '-> upload done');
			res.writeHead(200, {'content-type': 'text/plain'});
			
			debug(postReport, '\n'+ util.inspect(fields));
			debug(postReport, '\n'+ util.inspect(files));

			var data = _.object(fields);
			//save a new report and add an event log entry
			var newReport = new db.Report({
				userId: data.userId,
				latitude: data.latitude,
				longitude: data.longitude
			});
			newReport.weather.temperature = data.temperature;
			newReport.weather.humidity = data.humidity;
			newReport.weather.pressure = data.pressure;
			newReport.weather.pressureTrend = data.pressureTrend;
			newReport.weather.heatIndex = data.heatIndex;
			newReport.weather.windSpeed = data.windSpeed;
			newReport.weather.windDirection = data.windDirection;
			newReport.weather.windChill = data.windChill;
			newReport.weather.dewPoint = data.dewPoint;

			for (var index = 0; index < files.length; index++) {
				newReport.images.push(files[index].path);
			}

			newReport.save(function(err, newReport) {
				debug(postReport, JSON.stringify(newReport));
				res.end('' + newReport._id);

				var newEvent = new db.Event({
					reportId: newReport._id,
					eventType: 'create'
				});
				newEvent.save(function(err, newEvent) {
					debug(postReport, newEvent.eventType + ' event logged');
				});
			});
		});

		form.parse(req);
	}
};
