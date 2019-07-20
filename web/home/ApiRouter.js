const app = require("express").Router(),
    user = require("./models/User"),
    md5 = require("md5"),
    problem = require("./models/Problem"),
    utils = require("./Utils").data;


app.post("/register", (req, res, next) => {
    if (req.body.password !== undefined && req.body.email !== undefined)
        req.body.password = md5(req.body.email + req.body.password);
    req.body.token = md5(Date.now());
    user.create(req.body)
        .then((user, err) => {
            if (err) utils.handleError(res, err);
            else {
                res.json({
                    success: true,
                    data: {
                        token: req.body.token
                    }
                })
            }

        }).catch(e => utils.handleError(res, e));
});
app.post("/login", (req, res, next) => {
    user.findOne({$or: [{email: req.body.username}, {phoneNumber: req.body.username}]})
        .then((user, err) => {
            if (err) utils.handleError(res, err);
            else if (user) {
                if (user.password === md5(user.email + req.body.password)) {
                    res.json({
                        success: true,
                        data: {
                            token: user.token
                        }
                    })
                } else res.status(406).json({success: false, message: "Username/Password not matched !"})
            } else res.status(406).json({success: false, message: "Username/Password not matched !"});
        });
});

function basicAuth(req, res, next) {
    req.params.type = req.params.type.toLowerCase();
    let auth = req.headers["authorization"];
    if (auth.split("Bearer ").length < 2)
        auth = null;
    if (auth == null) {
        res.status(406).json({success: false, message: "Unauthorized access !"});
        return
    }
    utils.models.user.findOne({token: auth.split("Bearer ")[1]}, (err, user) => {
        if (user) {
            next.user = user;
            next();
        } else
            res.status(406).json({success: false, message: "Unauthorized access !"})
    });
}


app.post("/problem", basicAuth, (req, res, next) => {
    problem.create(req.body).then((data, err) => {
        if (err) utils.handleError(res, err);
        else {
            res.json({
                success: true
            })
        }
    });
});

module.exports = app;