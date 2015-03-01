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

router.post('/nuke', function(req, res) {
    //DON'T POST HERE UNLESS YOU WANT TO DESTROY EVERYTHING
    if (req.body.password == undefined) {
        res.status(400).end('Bad request');
    }

    if (req.body.password == 'ahmedamer') {
        User.find({}).remove().exec();
        Post.find({}).remove().exec();
        res.status(200).end('Nuked!');
    }
    else
        res.status(200).end('Not nuked (bad password)');
});

router.post('/authenticate', passport.authenticate('local'), function(req, res) {
    if (req.phone != undefined) {
        var token = jwt.sign(req.phone, 'dontstealmygrapes');
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
	var loc = rekt.body.loc;
	var grape = rekt.body.grape;
	if(loc == undefined || grape == undefined)
	{
            res.status(400);
            return res.end("bad request gtfo");
	}
	else
	{
		var time = Date.now() - 86400000;

		Post.find({"_timestamp" : { $gt: time }, _parent: undefined, _location: loc, _grape:grape}, {}, {sort: {'_timestamp' : -1 }}, function(err, posts)
		{
			if (err)
				debug(err);
			return res.json({posts:posts});
		});
	}
});

router.post('/submit' , function(req, res)
{
	var text = req.body.text;
	var grape = req.body.grape;
	var loc = req.body.loc;
	var token = req.body.token;

	if(text == undefined || grape == undefined || loc == undefined || token == undefined)
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
					res.status(400).end('invalid token');
				}
				var userID = decoded._id;
				Post.create({ _location: loc, _grape: grape, _text: text, _userID: userID}, function(err)
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

router.post('/comment' , function(req, res)
{
	var text = req.body.text;
	var grape = req.body.grape;
	var loc = req.body.loc;
	var parentID = req.body.parentID;
	var rootID = req.body.rootID;
	var token = req.body.token;

	if(text == undefined 
		|| grape 	== undefined 
		|| loc 		== undefined 
		|| token	== undefined 
		|| parentID 	== undefined
		|| rootID	== undefined
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

				var userID = 
					decoded._id;
					Post.create({_parent: parentID, 
					_root: rootID, _location: loc, 
					_grape: grape, _text: text, 
					_userID: userID}, 
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

//see any posts
//see comments
//create posts
//fetch location

module.exports = router;
