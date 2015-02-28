var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res) {
  res.render('index', { title: 'Express' });
});

//see any posts
//see comments
//create posts
//fetch location

module.exports = router;
