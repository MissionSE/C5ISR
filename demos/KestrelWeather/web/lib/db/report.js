var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var autoIncrement = require('mongoose-auto-increment');

var ReportSchema = new Schema({
	userId: String,
	latitude: Number,
	longitude: Number,
	updatedAt: { type: Date, default: Date.now },
	createdAt: { type: Date, default: Date.now },
	weather: { 
		temperature: Number,
		humidity: Number,
		pressure: Number,
		pressureTrend: String,
		heatIndex: Number,
		windSpeed: Number,
		windDirection: Number,
		windChill: Number,
		dewPoint: Number
	},
	notes: [String],
	images: [String],
	audio: [String],
	video: [String]
}, {
	versionKey: false
});

ReportSchema.plugin(autoIncrement.plugin, 'Report');

ReportSchema.statics.getAll = function(callback) {
	this.find({}, callback);
};

module.exports = mongoose.model('Report', ReportSchema);
