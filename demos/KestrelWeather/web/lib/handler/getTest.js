var request = require('request');
var fs = require('fs');
var path = require('path');
var randomstring = require('randomstring');

var webRoot = path.join(__dirname, '../..');

module.exports = function(db) {
	return function(req, res) {
		var postable = request.post('http://localhost:3000/upload', function(err, response, body) {
			res.end(body);
		});

		var form = postable.form();
		form.append('userId', randomstring.generate(8));
		form.append('latitude', (Math.random() * 180 - 90).toFixed(2));
		form.append('longitude', (Math.random() * 360 - 180).toFixed(2));

		form.append('temperature', (Math.random() * 150 - 50).toFixed(2));
		form.append('humidity', (Math.random() * 100).toFixed(2));
		form.append('pressure', (Math.random() * 20 + 20).toFixed(2));
		form.append('pressureTrend', 'down');
		form.append('heatIndex', (Math.random() * 150 - 35).toFixed(2));
		form.append('windSpeed', (Math.random() * 40).toFixed(2));
		form.append('windDirection', (Math.random() * 360).toFixed(2));
		form.append('windChill', (Math.random() * 150 - 60).toFixed(2));
		form.append('dewPoint', (Math.random() * 50 + 50).toFixed(2));

		form.append('upload', fs.createReadStream(webRoot + '/test/images/droplet.png'));
	}
};
