var impl = require('implementjs');

module.exports = function (server, handler) {
	console.log('setting up routes...');

	impl.implements(handler, { getIndex: impl.F });
	impl.implements(handler, { getTest: impl.F });
	impl.implements(handler, { getAllReports: impl.F });
	impl.implements(handler, { getReport: impl.F });
	impl.implements(handler, { postReport: impl.F });

	server.get('/', handler.getIndex);
	server.get('/test', handler.getTest);

	server.get('/report', handler.getAllReports);
	server.get('/report/all', handler.getAllReports);
	server.get('/report/:id', handler.getReport);

	server.post('/upload', handler.postReport);
};
