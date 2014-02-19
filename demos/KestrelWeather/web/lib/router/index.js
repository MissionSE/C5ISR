var impl = require('implementjs');

module.exports = function (server, handler) {
	console.log('setting up routes...');

	impl.implements(handler, { readIndex: impl.F });
	impl.implements(handler, { readAllReports: impl.F });
	impl.implements(handler, { readReport: impl.F });
	impl.implements(handler, { createReport: impl.F });

	server.get('/', handler.readIndex);
	server.get('/report', handler.readAllReports);
	server.get('/report/all', handler.readAllReports);
	server.get('/report/new', handler.createReport);
	server.get('/report/:id', handler.readReport);
};
