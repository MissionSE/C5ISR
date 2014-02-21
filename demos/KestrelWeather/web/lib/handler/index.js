module.exports = function(db) {
	return {
		getIndex: function(req, res) {
			res.send('You\'ve landed at the KestrelWeather index.');
		},
		getTest: require('./getTest')(db),
		getAllReports: require('./getAllReports')(db),
		getReport: require('./getReport')(db),
		postReport: require('./postReport')(db),
		getAllEvents: require('./getAllEvents')(db),
		getEventsSince: require('./getEventsSince')(db),
		getEvent: require('./getEvent')(db)
	};
};
