var formidable = require('formidable');
var util = require('util');
var path = require('path');
var fs = require('fs');
var _ = require('underscore');
var easyimage = require('easyimage');

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

			var contentDir = '/unknown/';
			if (file.type) {
				if (file.type.search('image') >= 0) {
					contentDir = '/images/';
				} else if (file.type.search('audio') >= 0) {
					contentDir = '/audio/';
				} else if (file.type.search('video') >= 0) {
					contentDir = '/video/';
				}
			}
			file.path = stagingArea + contentDir + path.basename(file.path);
			file.publicPath = contentDir + path.basename(file.path);
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
					updatedat: data.updatedat,
					title: data.title
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
				newReport.weather.name = data.weather.name;
				newReport.weather.country = data.weather.country;

				for (var index = 0; index < data.notes.length; index++) {
					newReport.notes.push(data.notes[index]);
				}

				newReport.save(function(err, newReport) {
					if (!err) {
						res.writeHead(200, {'content-type': 'text/plain'});
						debug(postReport, JSON.stringify(newReport));
						res.end(JSON.stringify({
							status: 'ok',
							id : newReport._id
						}));

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
							debug(postReport, 'saving ' + files[0].publicPath + ' to ' + data.id);
							if (files[0].type.search('image') >= 0) {
								var thumbnailPath = '/thumbnails/thumb-' + path.basename(files[0].publicPath);
								easyimage.thumbnail({
									src: stagingArea + files[0].publicPath,
									dst: stagingArea + thumbnailPath,
									width: 96, height: 96,
									x: 0, y: 0
								}, function(err, image) {
									db.Report.update( { _id: data.id },
										{
											$push: {
												images: {
													filename: data.filename,
													url: files[0].publicPath,
													thumbnail: thumbnailPath,
													size: data.size,
													date: data.date
												}
											}
										}, { upsert: true }, function(err) {
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
												url: files[0].publicPath,
												thumb: thumbnailPath
											}));

											var newEvent = new db.Event({
												reportId: data.id,
												eventType: 'modify'
											});
											newEvent.save(function(err, newEvent) {
												debug(postReport, newEvent.eventType + ' event logged');
											});
										}
									});
								});
							} else if (files[0].type.search('audio') >= 0) {
								db.Report.update( { _id: data.id },
									{
										$push: {
											audio: {
												filename: data.filename,
												url: files[0].publicPath,
												size: data.size,
												date: data.date
											}
										}
									}, { upsert: true }, function(err) {
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
											url: files[0].publicPath
										}));

										var newEvent = new db.Event({
											reportId: data.id,
											eventType: 'modify'
										});
										newEvent.save(function(err, newEvent) {
											debug(postReport, newEvent.eventType + ' event logged');
										});
									}
								});
							} else if (files[0].type.search('video') >= 0) {
								db.Report.update( { _id: data.id },
									{
										$push: {
											video: {
												filename: data.filename,
												url: files[0].publicPath,
												size: data.size,
												date: data.date
											}
										}
									}, { upsert: true }, function(err) {
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
											url: files[0].publicPath
										}));

										var newEvent = new db.Event({
											reportId: data.id,
											eventType: 'modify'
										});
										newEvent.save(function(err, newEvent) {
											debug(postReport, newEvent.eventType + ' event logged');
										});
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
