var path = require('path');
var express = require('express');

var applyConfiguration = function(server) {
	server.set('port', process.env.PORT || 3009);
	server.use(express.favicon());
	server.use(express.logger('dev'));

	server.use(server.router);
	server.use(express.json())
		.use(express.urlencoded());

	server.use(express.methodOverride());
	server.use(express.cookieParser('kestrel'));
	server.use(express.session());

	server.use(express.static(path.join(__dirname, 'public')));

	// development only
	if ('development' == server.get('env')) {
		server.use(express.errorHandler());
	}
};

exports.applyConfiguration = applyConfiguration;
