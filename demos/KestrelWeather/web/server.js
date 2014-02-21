// modules
var express = require('express');
var http = require('http');
var path = require('path');
var mongo = require('mongodb');
var debug = require('debug')('kestrel');

debug('booting in debug mode');

var db = require('./lib/db');
var handler = require('./lib/handler')(db);

// express framework setup
var server = express();
var config = require('./config');
config.applyConfiguration(server);

// set up routes
require('./lib/router')(server, handler);

http.createServer(server).listen(server.get('port'), function(){
	console.log('Express server listening on port ' + server.get('port'));
});
