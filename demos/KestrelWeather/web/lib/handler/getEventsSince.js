var _ = require('underscore');
var debug = require('debug')('kestrel:handler');

module.exports = function(db) {
	return function(req, res) {
		debug('GET event since ' + req.params.id);

		var lastClientEventId = req.params.id,
			fetchableReportIds = [],
			removableReportIds = [];
		var latestEventId = 0;
		db.Event.nextCount(function(err, count) {
			latestEventId = count - 1;

			db.Event.find( { _id: { $gt: lastClientEventId } }).exec(function(err, events) {
				if (events) {
					for (var index = 0; index < events.length; index++) {
						if (events[index].eventType === 'create' ||
							events[index].eventType === 'modify') {
							fetchableReportIds.push(events[index].reportId);
						} else if (events[index].eventType === 'delete') {
							fetchableReportIds = _.without(fetchableReportIds, events[index].reportId);
							removableReportIds.push(events[index].reportId);
						}

						if (events[index]._id > latestEventId) {
							latestEventId = events[index]._id;
						}
					}
					fetchableReportIds = _.uniq(fetchableReportIds);
				}
				
				res.writeHead(200, {'content-type': 'text/plain'});
				res.end(JSON.stringify({
					latestEvent: latestEventId,
					toFetch: fetchableReportIds,
					toRemove: removableReportIds
				}));
			});
		});

	}
};
