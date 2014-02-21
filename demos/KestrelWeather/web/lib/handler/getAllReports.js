var debug = require('debug')('kestrel:handler');

module.exports = function(db) {
	return function(req, res) {
		debug('GET report all');

		db.Report.find().lean().exec(function(err, reports) {
			if (reports) {
				res.send(reports);
			} else {
				res.send('No reports found.');
			}
		});
	}
};
