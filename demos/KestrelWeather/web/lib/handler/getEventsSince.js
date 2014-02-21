var _ = require('underscore');
var debug = require('debug')('kestrel:handler');

module.exports = function(db) {
	return function(req, res) {
		debug('GET event since ' + req.params.id);

		var lastEventId = req.params.id,
			reportIds = [];
		db.Event.find( { _id: { $gt: lastEventId } }).exec(function(err, events) {
			if (events) {
				for (var index = 0; index < events.length; index++) {
					if (events[index].eventType === 'create' ||
						events[index].eventType === 'modify') {
						reportIds.push(events[index].reportId);
					} else if (events[index].eventType === 'delete') {
						reportIds = _.without(reportIds, events[index].reportId);
					}
				}
				reportIds = _.uniq(reportIds);
				res.send(reportIds);
			} else {
				res.send('No events found.');
			}
		});
	}
};
