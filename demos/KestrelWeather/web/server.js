// modules
var express = require('express');
var http = require('http');
var path = require('path');
var mongo = require('mongodb');
var debug = require('debug')('kestrel:core');
var commander = require('commander');

debug('running with debug logging');

commander
	.version('0.1.1')
	.option('-d, --debug', 'Enable test route.')
	.parse(process.argv);

if (commander.debug) {
	debug('running with test route enabled');
}

var db = require('./lib/db');
var handler = require('./lib/handler')(db);

// express framework setup
var server = express();
var config = require('./config');
config.applyConfiguration(server);

// set up routes
require('./lib/router')(server, handler, commander.debug);

http.createServer(server).listen(server.get('port'), function(){
	console.log('Express server listening on port ' + server.get('port'));
});
