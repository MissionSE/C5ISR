var debug = require('debug')('kestrel:handler');

module.exports = function(db) {
	return function(req, res) {
		debug('GET event all');

		db.Event.find().lean().exec(function(err, events) {
			if (events) {
				res.send(events);
			} else {
				res.send('No events found.');
			}
		});
	}
};
