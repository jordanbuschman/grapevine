var express      = require('express');
var engine       = require('ejs-locals');
var path         = require('path');
var logger       = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser   = require('body-parser');
var mongoose     = require('mongoose');
var flash        = require('connect-flash');
var debug        = require('debug')('grapevine:app.js');

var routes = require('./routes/index');

var app = express();

//Database setup
databaseUrl = 'mongodb://' + process.env.DB_USER_GV + ':' + process.env.DB_PASSWORD_GV + '@ds030817.mongolab.com:30817/grapevine';
var db = mongoose.connection;
db.on('error', console.error);

mongoose.connect(databaseUrl, null, function(err) {
    if (err)
        debug(err);
    else
        debug('Connected to database.');
});

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.engine('ejs', engine);
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(__dirname + '/public/favicon.ico'));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', routes);

app.use(flash());

// catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
    app.use(function(err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
        message: err.message,
        error: {}
    });
});


module.exports = app;
