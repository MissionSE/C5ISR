module.exports = function(db) {
	return {
		readIndex: function(req, res) {
			res.send('rendering index');
		},
		readAllReports: require('./read.allreports')(db),
		readReport: require('./read.report')(db),
		createReport: require('./create.report')(db)
	};
};
