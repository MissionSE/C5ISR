var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var autoIncrement = require('mongoose-auto-increment');

var EventSchema = new Schema({
	reportId: Number,
	eventType: String,
	timestamp: { type: Date, default: Date.now }
}, {
	versionKey: false
});

EventSchema.plugin(autoIncrement.plugin, 'Event');

module.exports = mongoose.model('Event', EventSchema);
