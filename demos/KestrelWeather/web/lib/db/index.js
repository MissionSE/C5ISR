// modules
var mongoose = require('mongoose');
var autoIncrement = require('mongoose-auto-increment');

// connection setup
var connectionString = 'mongodb://localhost/kestreldb';

console.log('connecting to database...');
mongoose.connect(connectionString);

autoIncrement.initialize(mongoose.connection);

mongoose.connection.once('open', function() {
	console.log('connected to kestreldb');
});

mongoose.connection.on('error', console.error.bind(console, 'connection error:'));

mongoose.connection.on('close', function() {
	console.log('db connection closed');
});

// models
var reportModel = require('./report');
var eventModel = require('./event');

module.exports.Report = reportModel;
module.exports.Event = eventModel;
