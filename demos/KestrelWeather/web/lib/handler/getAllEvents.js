var debug = require('debug')('kestrel:handler');

module.exports = function(db) {
	return function(req, res) {
		debug('GET event all');

		db.Event.find().lean().exec(function(err, events) {
			if (events) {
				res.writeHead(200, {'content-type': 'text/plain'});
				res.end(JSON.stringify(events));
			} else {
				res.writeHead(404, {'content-type': 'text/plain'});
				res.end();
			}
		});
	}
};
