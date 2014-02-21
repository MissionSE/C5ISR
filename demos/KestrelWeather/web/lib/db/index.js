var mongoose = require('mongoose');
var autoIncrement = require('mongoose-auto-increment');
var debug = require('debug')('kestrel:db');

// connection setup
var connectionString = 'mongodb://localhost/kestreldb';

debug('connecting to database');
mongoose.connect(connectionString);

autoIncrement.initialize(mongoose.connection);

mongoose.connection.once('open', function() {
	debug('connected to kestreldb');
});

mongoose.connection.on('error', console.error.bind(console, 'connection error:'));

mongoose.connection.on('close', function() {
	debug('connection closed');
});

// models
var reportModel = require('./report');
var eventModel = require('./event');

module.exports.Report = reportModel;
module.exports.Event = eventModel;
