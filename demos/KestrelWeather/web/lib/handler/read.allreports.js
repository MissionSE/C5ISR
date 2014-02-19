module.exports = function(db) {
	return function(req, res) {
		db.Report.find().lean().exec(function(err, reports) {
			if (reports) {
				res.send(reports);
			} else {
				res.send('No reports found.');
			}
		});
	}
};
