var debug = require('debug')('kestrel:handler');

module.exports = function(db) {
	return function(req, res) {
		debug('GET event id: ' + req.params.id);

		var eventId = req.params.id;
		db.Event.findById(eventId).lean().exec(function(err, events) {
			if (events) {
				res.writeHead(200, {'content-type': 'text/plain'});
				res.end(JSON.stringify(events));
			} else {
				res.writeHead(404, {'content-type': 'text/plain'});
				res.end(JSON.stringify({
					status: 'nok'
				}));
			}
		});
	}
};
