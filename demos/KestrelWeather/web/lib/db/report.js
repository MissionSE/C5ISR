var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var autoIncrement = require('mongoose-auto-increment');

var ReportSchema = new Schema({
	userId: String,
	latitude: Number,
	longitude: Number,
	updated_at: { type: Date, default: Date.now },
	created_at: { type: Date, default: Date.now },
	weatherId: { type: Number, min: 0 }
}, {
	versionKey: false
});

ReportSchema.plugin(autoIncrement.plugin, 'Report');

ReportSchema.statics.getAll = function(callback) {
	this.find({}, callback);
};

module.exports = mongoose.model('Report', ReportSchema);
