var mongoose              = require('mongoose');
var Schema                = mongoose.Schema;


var Post = new Schema({
	_parent		 : {type: Schema.Types.ObjectId, ref: 'Post' },
	_root		 : {type: Schema.Types.ObjectId, ref: 'Post' },
	_location	 : {type: String, required: true },
	_grove		 : {type: Number, required: true },
	_timestamp	 : {type: Number},
	_text		 : {type: String, required: true},
	_title		 : {type: String},
	_views	 	 : {type: Number, default: 0},
    _phoneNumber : {type: String, required: true},
	_username	 : {type: String },
});

Post.pre('save', function(next) {
    now = new Date().getTime();
    if (this._timestamp == undefined) {
        this._timestamp = now;
    }
    next();
});

module.exports = mongoose.model('Post', Post);
