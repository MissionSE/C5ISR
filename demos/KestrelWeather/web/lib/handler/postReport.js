var formidable = require('formidable');
var util = require('util');
var path = require('path');
var fs = require('fs');
var randomstring = require('randomstring');
var _ = require('underscore');

var debug = require('debug')('kestrel:handler');
var postReport = 'postReport';

var stagingArea = path.join(__dirname, '../../public');

module.exports = function(db) {
	return function(req, res) {
		if (req.method.toLowerCase() !== 'post') {
			res.writeHead(200, {'content-type': 'text/plain'});
			res.end('Please use POST.');
			return;
		}

		var contentType = req.get('Content-Type');
		debug(postReport, 'content-type: ' + contentType);
		debug(postReport, 'content-length: ' + req.get('Content-Length'));

		var form = new formidable.IncomingForm(),
			files = [],
			fields = [];

		form.uploadDir = stagingArea;
		form.keepExtensions = true;

		form.on('field', function(field, value) {
			fields.push([field, value]);
		})
		.on('fileBegin', function(name, file) {
			debug(postReport, 'file name: ' + file.name);
			debug(postReport, 'file mime: ' + file.type);

			if (file.type) {
				if (file.type.search('image') >= 0) {
					file.path = stagingArea + '/images/' + path.basename(file.path);
				} else if (file.type.search('audio') >= 0) {
					file.path = stagingArea + '/audio/' + path.basename(file.path);
				}
			} else {
				file.path = stagingArea + '/unknown/' + path.basename(file.path);
			}
		})
		.on('progress', function(bytesReceived, bytesExpected) {
			debug(postReport, bytesReceived + '/' + bytesExpected);
		})
		.on('aborted', function(name, file) {
			debug(postReport, '-> post aborted');
		})
		.on('file', function(name, file) {
			files.push(file);
		})
		.on('end', function() {
			debug(postReport, '-> post end');
			debug(postReport, '\nFields: '+ util.inspect(fields));
			debug(postReport, '\nFiles: '+ util.inspect(files));

			var data = _.object(fields);

			// if json, then create a new report
			if (contentType == 'application/json') {
				//save a new report and add an event log entry
				var newReport = new db.Report({
					userid: data.userid,
					latitude: data.latitude,
					longitude: data.longitude,
					createdat: data.createdat,
					updatedat: data.updatedat
				});
				newReport.kestrel.temperature = data.kestrel.temperature;
				newReport.kestrel.humidity = data.kestrel.humidity;
				newReport.kestrel.pressure = data.kestrel.pressure;
				newReport.kestrel.pressuretrend = data.kestrel.pressuretrend;
				newReport.kestrel.heatindex = data.kestrel.heatindex;
				newReport.kestrel.windspeed = data.kestrel.windspeed;
				newReport.kestrel.winddirection = data.kestrel.winddirection;
				newReport.kestrel.windchill = data.kestrel.windchill;
				newReport.kestrel.dewpoint = data.kestrel.dewpoint;

				newReport.weather.conditioncode = data.weather.conditioncode;
				newReport.weather.description = data.weather.description;

				for (var index = 0; index < data.notes.length; index++) {
					newReport.notes.push(data.notes[index]);
				}

				newReport.save(function(err, newReport) {
					if (!err) {
						res.writeHead(200, {'content-type': 'text/plain'});
						debug(postReport, JSON.stringify(newReport));
						res.end(JSON.stringify( { id : newReport._id }));

						var newEvent = new db.Event({
							reportId: newReport._id,
							eventType: 'create'
						});
						newEvent.save(function(err, newEvent) {
							debug(postReport, newEvent.eventType + ' event logged');
						});
					} else {
						res.writeHead(404, {'content-type': 'text/plain'});
						res.end(JSON.stringify({
							status: 'nok'
						}));
					}
				});
			} else {
				db.Report.findById(data.id).exec(function(err, reports) {
					if (err || !reports) {
						debug(postReport, 'invalid id specified for file upload');
						res.writeHead(404, {'content-type': 'text/plain'});
						res.end(JSON.stringify({
							status: 'nok'
						}));
					} else {
						if (files[0].type) {
							var publicPath = files[0].path.split('public')[2]; //TODO: Fix this nonsense
							debug(postReport, 'saving ' + publicPath + ' to ' + data.id);
							if (files[0].type.search('image') >= 0) {
								db.Report.update( { _id: data.id }, { $push: { images: publicPath } }, { upsert: true }, function(err) {
									if (err) {
										debug(postReport, "query update failed");
										res.writeHead(404, {'content-type': 'text/plain'});
										res.end(JSON.stringify({
											status: 'nok'
										}));
									} else {
										res.writeHead(200, {'content-type': 'text/plain'});
										res.end(JSON.stringify({
											status: 'ok',
											url: publicPath
										}));
									}
								});
							} else if (files[0].type.search('audio') >= 0) {
								db.Report.update( { _id: data.id }, { $push: { audio: publicPath } }, { upsert: true }, function(err) {
									if (err) {
										debug(postReport, "query update failed");
										res.writeHead(404, {'content-type': 'text/plain'});
										res.end(JSON.stringify({
											status: 'nok'
										}));
									} else {
										res.writeHead(200, {'content-type': 'text/plain'});
										res.end(JSON.stringify({
											status: 'ok',
											url: publicPath
										}));
									}
								});
							}
						} else {
							debug(postReport, "no file mime type specified");
							res.writeHead(404, {'content-type': 'text/plain'});
							res.end(JSON.stringify({
								status: 'nok'
							}));
						}
					}
				});
			}
		});
		form.parse(req);
	}
};
