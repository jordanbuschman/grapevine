var mongoose              = require('mongoose');
var Schema                = mongoose.Schema;
var passportLocalMongoose = require('passport-local-mongoose');

var User = new Schema({
    username: String
});

User.plugin(passportLocalMongoose, {
    usernameField: 'phoneNumber'
});

User.path('phoneNumber').validate(function (phoneNumber) {
    if (phoneNumber == undefined)
        return true;

    var regex = /^\d{10}$/
    return regex.test(phoneNumber);
}, 'Phone number must be in format 1234567890.')

module.exports = mongoose.model('User', User);
