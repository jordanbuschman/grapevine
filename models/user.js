var mongoose              = require('mongoose');
var bcrypt                = require('bcrypt');
var Schema                = mongoose.Schema;

var User = new Schema({
    phoneNumber: { type: Number, required: true},
    username: { type: String, required: true, unique: true},
});

User.pre('save', function(next) {
    var user = this;
    // only hash the phoneNumber if it has been modified (or is new)
    if (!user.isModified('phoneNumber')) return next();
 
    // generate a salt
    bcrypt.genSalt(10, function(err, salt) {
        if (err != undefined) return next(err);
        console.log('SALT: ' + salt);
        console.log('PHONENO: ' + user.phoneNumber);
          
            // hash the phoneNumber along with our new salt
        bcrypt.hash(user.phoneNumber, salt, function(err, hash) {
            // override the cleartext phoneNumber with the hashed one
            user.phoneNumber = hash;
            next();
        });
    });
});


User.methods.comparePhoneNumber = function(candidatePhoneNumber, cb) {
    bcrypt.compare(candidatePhoneNumber, this.phoneNumber, function(err, isMatch) {
        if (err) return cb(err);
        cb(null, isMatch);
    });
};

User.path('phoneNumber').validate(function (phoneNumber) {
    if (phoneNumber == undefined)
        return false;

    var regex = /^\d{11}$/
    return regex.test(phoneNumber);
}, 'Phone number must be in format 12345678901.')

module.exports = mongoose.model('User', User);
