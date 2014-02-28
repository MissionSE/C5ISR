var request = require('request');
var requestify = require('requestify');
var fs = require('fs');
var path = require('path');
var randomstring = require('randomstring');
var debug = require('debug')('kestrel:handler');

var webRoot = path.join(__dirname, '../..');

module.exports = function(db) {
	return function(req, res) {
		debug('GET test type ' + req.params.type);

		if (req.params.type.toLowerCase() == 'json') {
			requestify.post('http://localhost:3009/upload', {
				userid: randomstring.generate(8),
				latitude: (Math.random() * 180 - 90).toFixed(2),
				longitude: (Math.random() * 360 - 180).toFixed(2),

				createdat: Date.now(),
				updatedat: Date.now(),

				kestrel: {
					temperature: (Math.random() * 150 - 50).toFixed(2),
					humidity: (Math.random() * 100).toFixed(2),
					pressure: (Math.random() * 20 + 20).toFixed(2),
					pressuretrend: 'down',
					heatindex: (Math.random() * 150 - 35).toFixed(2),
					windspeed: (Math.random() * 40).toFixed(2),
					winddirection: (Math.random() * 360).toFixed(2),
					windchill: (Math.random() * 150 - 60).toFixed(2),
					dewpoint: (Math.random() * 50 + 50).toFixed(2)
				},
				weather: {
					conditioncode: (Math.random() * 10).toFixed(0),
					description: 'some really bad weather'
				},
				notes: [{
					title: 'Note 1',
					content: 'incoherent ranting'
				}, {
					title: 'Note 2',
					content: 'dear diary'
				}, {
					title: 'The Last Note',
					content: 'an acute observation'
				}]
			})
			.then(function(response) {
				res.end(response.getBody());
			});
		} else if (req.params.type.toLowerCase() == 'multi') {
			db.Report.nextCount(function(err, count) {
				if (count - 1 >= 0) {
					var postable = request.post('http://localhost:3009/upload', function(err, response, body) {
						debug('err:' + err);
						debug('response:' + response);
						debug('body:' + body);
						res.end(body);
					});

					var form = postable.form();

					form.append('id', count - 1);
					form.append('upload', fs.createReadStream(webRoot + '/test/images/droplet.png'));
					form.append('upload', fs.createReadStream(webRoot + '/test/audio/droplet.mp3'));
				} else {
					res.writeHead(404, {'content-type': 'text/plain'});
					res.end(JSON.stringify({
						status: 'nok'
					}));
				}
			});
		}
	}
};
