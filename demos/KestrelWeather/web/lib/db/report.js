var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var autoIncrement = require('mongoose-auto-increment');

var ReportSchema = new Schema({
	userid: String,
	latitude: Number,
	longitude: Number,

	updatedat: { type: Date, default: Date.now },
	createdat: { type: Date, default: Date.now },

	title: String,

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
		content: String,
		size: Number,
		createdat: { type: Date, default: Date.now }
	}],
	images: [{
		filename: String,
		url: String,
		size: Number,
		date: { type: Date, default: Date.now }
	}],
	audio: [{
		filename: String,
		url: String,
		size: Number,
		date: { type: Date, default: Date.now }
	}],
	video: [{
		filename: String,
		url: String,
		size: Number,
		date: { type: Date, default: Date.now }
	}]
}, {
	versionKey: false
});

ReportSchema.plugin(autoIncrement.plugin, {
	model: 'Report',
	startAt: 1
});

ReportSchema.statics.findAll = function(callback) {
	this.find({}, callback);
};

module.exports = mongoose.model('Report', ReportSchema);
