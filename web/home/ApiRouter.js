const app = require("express").Router(),
    user = require("./models/User"),
    md5 = require("md5"),
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

module.exports = app;