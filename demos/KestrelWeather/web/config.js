var path = require('path');
var express = require('express');

var applyConfiguration = function(server) {
	server.set('port', process.env.PORT || 3000);
	server.use(express.favicon());
	server.use(express.logger('dev'));
	server.use(express.json());
	server.use(express.urlencoded());
	server.use(express.methodOverride());
	server.use(express.cookieParser('kestrel'));
	server.use(express.session());
	server.use(server.router);
	server.use(express.static(path.join(__dirname, 'public')));

	// development only
	if ('development' == server.get('env')) {
		server.use(express.errorHandler());
	}
};

exports.applyConfiguration = applyConfiguration;