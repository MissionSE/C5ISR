var debug = require('debug')('kestrel:handler');

module.exports = function(db) {
	return function(req, res) {
		debug('GET event id: ' + req.params.id);

		var eventId = req.params.id;
		db.Event.findById(eventId).lean().exec(function(err, events) {
			if (events) {
				res.send(events);
			} else {
				res.send('No event with that ID found.');
			}
		});
	}
};
