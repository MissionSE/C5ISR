var debug = require('debug')('kestrel:handler');

module.exports = function(db) {
	return function(req, res) {
		debug('GET report all');

		db.Report.find().lean().exec(function(err, reports) {
			if (reports) {
				res.writeHead(200, {'content-type': 'text/plain'});
				res.end(JSON.stringify(reports));
			} else {
				res.writeHead(404, {'content-type': 'text/plain'});
				res.end();
			}
		});
	}
};
