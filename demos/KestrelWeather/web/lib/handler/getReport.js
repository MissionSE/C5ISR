var debug = require('debug')('kestrel:handler');

module.exports = function(db) {
	return function(req, res) {
		debug('GET report id: ' + req.params.id);

		var reportId = req.params.id;
		db.Report.findById(reportId).lean().exec(function(err, reports) {
			if (reports) {
				res.send(reports);
			} else {
				res.send('No report with that ID found.');
			}
		});
	}
};
