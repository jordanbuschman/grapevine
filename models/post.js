var mongoose              = require('mongoose');
var Schema                = mongoose.Schema;


var Post = new Schema({
	_parent		: {type: Schema.Types.ObjectId, ref: 'Post' },
	_root		: {type: Schema.Types.ObjectId, ref: 'Post' },
	_location	: {type: String, required: true },
	_grape		: {type: Number, required: true },
	_timestamp	: {type: Date, default: new Date() },
	_text		: {type: String, required: true},
	_views		: {type: Number, default: 0},
	_userID		: {type: String, required: true },
	_username	: {type: String },
});


module.exports = mongoose.model('Post', Post);;
