var debug = require('debug')('kestrel:router');

module.exports = function (server, handler, testRouteEnabled) {
	debug('setting up routes');

	server.get('/', handler.getIndex);
	if (testRouteEnabled) {
		server.get('/test/:type', handler.getTest);
	}

	server.get('/report', handler.getAllReports);
	server.get('/report/all', handler.getAllReports);
	server.get('/report/:id', handler.getReport);

	server.post('/upload', handler.postReport);

	server.get('/event', handler.getAllEvents);
	server.get('/event/since/:id', handler.getEventsSince);
	server.get('/event/:id', handler.getEvent);
};
