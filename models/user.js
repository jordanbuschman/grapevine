var mongoose              = require('mongoose');
var bcrypt                = require('bcrypt');
var Schema                = mongoose.Schema;

var User = new Schema({
    phoneNumber: { type: Number, required: true, unique: true},
    password: { type: String, required: true }
});

User.pre('save', function(next) {
    var user = this;
    // only hash the password if it has been modified (or is new)
    if (!user.isModified('password')) return next();
 
    // generate a salt
    bcrypt.genSalt(10, function(err, salt) {
        if (err) return next(err);
          
            // hash the password along with our new salt
        bcrypt.hash(user.password, salt, function(err, hash) {
            if (err) return next(err);
             
            // override the cleartext password with the hashed one
            user.password = hash;
            next();
        });
    });
});


User.methods.comparePassword = function(candidatePassword, cb) {
    bcrypt.compare(candidatePassword, this.password, function(err, isMatch) {
        if (err) return cb(err);
        cb(null, isMatch);
    });
};

User.path('phoneNumber').validate(function (phoneNumber) {
    if (phoneNumber == undefined)
        return true;

    var regex = /^\d{10}$/
    return regex.test(phoneNumber);
}, 'Phone number must be in format 1234567890.')

module.exports = mongoose.model('User', User);
