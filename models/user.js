var mongoose              = require('mongoose');
var Schema                = mongoose.Schema;
var passportLocalMongoose = require('passport-local-mongoose');

var User = new Schema({
    phoneNumber: String
});

User.set('redisCache', true);

User.plugin(passportLocalMongoose);

User.path('phoneNumber').validate(function (username) {
    var regex = /^\d{10}$/
    console.log(username);
    return regex.test(username);
}, 'Phone number must be in format 123-456-7890.')

module.exports = mongoose.model('User', User);
