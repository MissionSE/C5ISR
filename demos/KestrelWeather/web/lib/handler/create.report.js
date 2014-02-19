module.exports = function(db) {
	return function(req, res) {
		var newReport = new db.Report({content : 'some text'});
		newReport.save(function(err, newReport) {
			res.send('created new report of id: ' + newReport._id);
		});
	}
};
