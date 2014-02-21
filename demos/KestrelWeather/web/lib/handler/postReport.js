var formidable = require('formidable');
var util = require('util');
var path = require('path');
var fs = require('fs');
var randomstring = require('randomstring');
var _ = require('underscore');

var stagingArea = path.join(__dirname, '../../public/staging');

module.exports = function(db) {
	return function(req, res) {
		var form = new formidable.IncomingForm(),
			files = [],
			fields = [];

		form.uploadDir = stagingArea;
		form.keepExtensions = true;

		form.on('field', function(field, value) {
			console.log(field, value);
			fields.push([field, value]);
		})
		.on('fileBegin', function(name, file) {
			//file.path = form.uploadDir + "/" + file.name;
		})
		.on('file', function(name, file) {
			console.log(name, file);
			files.push(file);
		})
		.on('progress', function(bytesReceived, bytesExpected) {
			console.log(bytesReceived + '/' + bytesExpected);
		})
		.on('end', function() {
			console.log('-> upload done');
			res.writeHead(200, {'content-type': 'text/plain'});
			res.write('received fields:\n\n ' + util.inspect(fields));
			res.write('\n\n');
			res.write('received files:\n\n ' + util.inspect(files));

			var data = _.object(fields);
			//console.log(JSON.stringify(data));

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
				res.write('\n\nCreated report:\n\n');
				res.write(JSON.stringify(newReport));
				res.write('\n\nResponse to client:\n\n');
				res.end('new report id: ' + newReport._id);
			});
		});

		form.parse(req);
	}
};
