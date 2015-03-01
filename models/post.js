var mongoose              = require('mongoose');
var Schema                = mongoose.Schema;


var Post = new Schema({
	_id			: {type: String, required: true },
	_parent		: {type: Schema.Types.ObjectId, ref: 'Post', required: true, default: -1},
	_root		: {type: Schema.Types.ObjectId, ref: 'Post', required: true, default: -1},
	_timestamp	: {type: Date, default: new Date() },
	_text		: {type: String, required: true},
	_views		: {type: Number, default: 0}
});


module.exports = mongoose.model('User', User);