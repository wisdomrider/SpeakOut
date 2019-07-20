const app = require("express").Router(),
    user = require("./models/User"),
    md5 = require("md5"),
    utils = require("./Utils").data;


function notMatched(res, message = "Username/Password not matched !") {
    res.cookie(utils.constants.errorMessage, message)
        .redirect("/");
}

app.post("/login", (req, res, next) => {
    user.findOne({$or: [{email: req.body.username}, {phoneNumber: req.body.username}]})
        .then((user, err) => {
            if (err) utils.handleError(res, err);
            else if (user) {
                if (user.password === md5(user.email + req.body.password) && user.role === "admin") {
                    res.cookie("token", user.token, true)
                        .redirect("/web/dash")
                } else if (user.role !== "admin") {
                    notMatched(res, "You must be admin to login !");
                } else notMatched(res)
            } else notMatched(res)
        });
});
app.get("/dash", utils.checkforAuth, (req, res, next) => {
    utils.render(res, "dash", {title: "Dashboard"})
});


module.exports = app;