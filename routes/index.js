var express  = require('express');
var passport = require('passport');
var jwt      = require('jsonwebtoken');
var debug    = require('debug')('grapevine:index.js');
var User     = require('../models/user');
var Post	 = require('../models/post');

var router = express.Router();

/* GET home page. */
router.get('/', function(req, res) {
  res.render('index', { title: 'Express' });
});

router.post('/register', function(req, res) {
    var username = req.body.username;
    var password = req.body.password;
    var phoneNumber = req.body.phoneNumber;

    User.register(new User({ username: username, phoneNumber: phoneNumber}), password, function(err, newUser) {
        if (err) {
            res.status(400);
            return res.end(JSON.stringify({ err: err }) );
        }
        debug ('Added user ' + newUser.username + ' to users');
        res.status(201);
        return res.end(JSON.stringify({ status: '201', message: 'Created' }) );
    });
});

router.post('/authenticate', passport.authenticate('local'), function(req, res) {
    if (req.user != undefined) {
        var token = jwt.sign(req.user, 'dontstealmygrapes');
        return res.json({ token: token });
    }
    else if (req.body.id != undefined) {
        //TODO: Homeless person id authentication
    }
    else {
        return res.send(401, 'Invalid username or password');
    }
});

router.post('/location', function(rekt, res)
{
	var location = rekt.body.location;
	if(location == undefined)
	{
            res.status(400);
            return res.end("bad request gtfo");
	}
	else
	{
		var time = Date.now();

		Post.find({"_timestamp" : { $gt: time }, _parent: undefined }, {}, {sort: {'_timestamp' : -1 }}, function(err, posts)
		{
			if (err)
				debug(err);
			return res.json({posts:posts});
		});
	}
});


//see any posts
//see comments
//create posts
//fetch location

module.exports = router;
