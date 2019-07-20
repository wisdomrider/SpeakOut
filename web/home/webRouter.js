const app = require("express").Router(),
    user = require("./models/User"),
    org = require("./models/Org"),
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
    utils.render(res, "dash", {title: "Dashboard", active: "home"})
});
app.route("/org/add").get((req, res, next) => {
    utils.render(res, "add_org", {title: "Add organization", active: "add_org"})
}).post((req, res, next) => {
    org.create(req.body)
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
app.get("/org/list", (req, res, next) => {
    utils.render(res, "list_org", {title: "Add organization", active: "list_org"})
});
app.route("/prob/add").get((req, res, next) => {
    utils.render(res, "add_prob", {title: "Add Problem", active: "add_prob"})
});
app.get("/prob/list", (req, res, next) => {
    utils.render(res, "list_prob", {title: "Add Problem", active: "list_prob"})
});


module.exports = app;