var debug = require('debug')('kestrel:handler');
var stringifyObject = require('stringify-object');

module.exports = function(db) {
	return function(req, res) {
		debug('GET report id: ' + req.params.id);

		var reportId = req.params.id;
		db.Report.findById(reportId).lean().exec(function(err, report) {
			if (report) {
				res.writeHead(200, {'content-type': 'text/plain'});
				res.end(JSON.stringify(report));
			} else {
				res.writeHead(404, {'content-type': 'text/plain'});
				res.end();
			}
		});
	}
};
