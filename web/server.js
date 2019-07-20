var express = require("express"),
    path = require("path"),
    ejs = require("ejs"),
    createError = require('http-errors'),
    config = require("./Config"),
    mongoose = require("mongoose"),
    utils = require("./home/Utils").data,
    cookie = require("cookie-parser"),
    app = express();

app.use(express.json());
app.use(cookie());
app.set("view engine", "ejs");
app.set('views', path.join(__dirname, 'views'));
app.use("/", express.static(path.join(__dirname, "public")));

app.get("/", (req, res, next) => {
    utils.render(res, "check", {});
});


app.use(function (req, res, next) {
    next(createError(404));
});

app.use(function (err, req, res, next) {
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};
    res.status(err.status || 500);
    res.render('error', {
        error: err
    });
});

// var conn = mongoose.connect(config.config, config.mongo);
//
// conn.then((db) => {
//     console.log("Connected...")
// }, (err) => console.log(err));



app.listen(process.env.port || 3000, () => {
    console.log("It's working fine !");
});



