var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var autoIncrement = require('mongoose-auto-increment');

var ReportSchema = new Schema({
	userid: String,
	latitude: Number,
	longitude: Number,

	updatedat: { type: Date, default: Date.now },
	createdat: { type: Date, default: Date.now },

	kestrel: { 
		temperature: Number,
		humidity: Number,
		pressure: Number,
		pressuretrend: String,
		heatindex: Number,
		windspeed: Number,
		winddirection: Number,
		windchill: Number,
		dewpoint: Number
	},

	weather: {
		conditioncode: Number,
		description: String
	},

	notes: [{
		title: String,
		content: String
	}],
	
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
