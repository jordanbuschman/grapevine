var express  = require('express');
var passport = require('passport');
var jwt      = require('jsonwebtoken');
var debug    = require('debug')('grapevine:index.js');
var User     = require('../models/user');
var Post	 = require('../models/post');
var mongoose = require('mongoose');

var router = express.Router();

/* GET home page. */
router.get('/', function(req, res) {
  res.render('index', { title: 'Express' });
});

router.post('/register', function(req, res) {
    var phoneNumber = req.body.phoneNumber;
    var password = req.body.password;

    if (phoneNumber == undefined || password == undefined)
        return res.status(400).end('Bad request');

    User.create({ phoneNumber: phoneNumber, password: password }, function(err, newUser) {
        if (err) {
            res.status(400);
            return res.end(JSON.stringify({ err: err }) );
        }
        debug ('Added user ' + newUser.phoneNumber + ' to users');
        var token = jwt.sign(newUser, 'dontstealmygrapes');
        return res.end(token);
    });
});

router.post('/nuke', function(req, res) {
    //DON'T POST HERE UNLESS YOU WANT TO DESTROY EVERYTHING
    if (req.body.password == undefined) {
        res.status(400).end('Bad request');
    }

    if (req.body.password == 'ahmedamer') {
        mongoose.connection.db.dropDatabase(function(err, done) {});
        res.status(200).end('Nuked!');
    }
    else
        res.status(200).end('Not nuked (bad password)');
});

router.post('/authenticate', function(req, res) {
    if (req.body.phoneNumber == undefined || req.body.password == undefined)
        return res.status(400).end('Bad request');

    User.findOne({ phoneNumber: req.body.phoneNumber }, function(err, user) {
        if (err) {
            debug(err);
            res.status(400).end(err);
        }
        else if (!user) {
            res.status(401).end('Invalid username or password');
        }
        else {
            user.comparePassword(req.body.password, function(err, authenticated) {
                if (err) {
                    debug(err);
                    res.status(400).end(err);
                }
                else {
                    var token = jwt.sign(user, 'dontstealmygrapes');
                    return res.end(token);
                }
            });
        }
    });
});

router.post('/location', function(rekt, res)
{
    var loc = rekt.body.loc.replace(/["']/g,'');
    var grape = rekt.body.grape.replace(/["']/g,'');
    if(loc == undefined || grape == undefined)
    {
        res.status(400);
        return res.end("bad request gtfo");
    }
    else
    {
        var time = Date.now() - 86400000;

        Post.find({ "_timestamp" : { $gt: time }, _parent: undefined, _location: loc, _grape:grape }, {}, {sort: {'_timestamp' : -1 }}, function(err, posts)
        {
            if (err)
                debug(err);
            return res.end(posts);
        });
    }
});

router.post('/submit' , function(req, res)
{
    var text = req.body.text;
    var grape = req.body.grape;
    var loc = req.body.loc;
    var token = req.body.token;
	var user = req.body.user;

    if(text == undefined || grape == undefined || loc == undefined || token == undefined || user == undefined)
    {
        res.status(400);
        return res.end("Could not retrieve text");
    }
    
    else
    {
        jwt.verify(token, 'dontstealmygrapes', function(err, decoded) {
            if (err) {
                debug(err);
                return res.status(400).end('you dun goofed');
            }
            else {
                if (decoded._id == undefined) {
                    res.status(403).end('Forbidden: Invalid token');
                }
                var userID = decoded._id;
                Post.create({ _location: loc, _grape: grape, _text: text, _userID: userID, _username: user}, function(err, post)
                {
                    if (err)
                    {
                        debug(err);
                        return res.status(400).end("Invalid post");
                    }
                    console.log(post);
                    return res.status(201).end("Success!");
                });

            }
        });
    }
});

router.post('/comment' , function(req, res)
{
    var text = req.body.text;
    var grape = req.body.grape;
    var loc = req.body.loc;
    var parentID = req.body.parentID;
    var rootID = req.body.rootID;
    var token = req.body.token;

    if(text == undefined 
        || grape     == undefined 
        || loc       == undefined 
        || token     == undefined 
        || parentID  == undefined
        || rootID    == undefined
    )
    {
        res.status(400);
        return res.end("Could not retrieve text");
    }
    else
    {
        jwt.verify(token, 'dontstealmygrapes', function(err, decoded) {
            if (err) {
                debug(err);
                return res.status(400).end('you dun goofed');
            }
            else 
            {
                if (decoded._id == undefined) 
                {
                    res.status(400).end('invalid token');
                }

                var phoneNumber = decoded._phoneNumber;
                Post.create({_parent: parentID, 
                    _root: rootID, _location: loc, 
                    _grape: grape, _text: text, 
                    _phoneNumber: _phoneNumber}, 
                    function(err)
                    {
                        if (err)
                        {
                            debug(err);
                            return res.status(400).end("Invalid post");
                        }
                        
                        return res.status(201).end("Success!");
                    });

            }
        });
    }
});



router.post('/pwm', function(req, res)
{
	var password = req.body.password;
	var token = req.body.token;
	
	if(token == undefined)
	{
		res.status(400);
		return res.end("Undefined token");
	}
	else
	{
		jwt.verify(token, 'dontstealmygrapes', function(err, decoded)
		{
			if (err)
			{
				debug(err);
				return res.status(400).end('You guffed');
			}
			else
			{
				if(decoded._id == undefined)
					return res.status(400).end('Invalid Token for Password Change');
				//Set up twilio and text characters to phone
			}
		});
	}
});

//see any posts
//see comments
//create posts
//fetch location

module.exports = router;
