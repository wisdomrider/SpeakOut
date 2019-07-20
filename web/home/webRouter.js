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
    utils.render(res, "dash", {title: "Dashboard", active: "home"})
});

function handleError(res, e) {
    console.log(e);
    res.cookie(utils.constants.errorMessage, "Something went wrong !")
        .redirect("/web/dash");
}

app.route("/org/add").get((req, res, next) => {
    utils.render(res, "add_org", {title: "Add organization", active: "add_org"})
}).post((req, res, next) => {
    req.body.role = utils.constants.org;
    req.body.password = md5(req.body.email + req.body.password);
    user.create(req.body)
        .then((user, err) => {
            if (err) handleError(res, err);
            else {
                res.cookie(utils.constants.successMessage, "Organization added successfully !")
                    .redirect("/web/org/list");
            }

        }).catch(e => handleError(res, e));
});
app.get("/org/list", (req, res, next) => {
    user.find({role: "Org"}, (err, data) => {
        if (err) handleError(res, err);
        else {
            utils.render(res, "list_org", {data: data})
        }
    });
});
app.route("/org/edit/:id").get((req, res, next) => {
    user.findOne({_id: req.params.id}, (err, data) => {
        if (err) handleError(res, err);
        else {
            utils.render(res, "edit_org", {data: data})
        }
    });
}).post((req, res, next) => {
    user.findOneAndUpdate({_id: req.params.id}, req.body, (err, data) => {
        if (err) handleError(res, err);
        else {
            res.cookie(utils.constants.successMessage, "Organization added successfully !")
                .redirect("/web/org/list");
        }
    })
});
app.route("/prob/add").get((req, res, next) => {
    utils.render(res, "add_prob", {title: "Add Problem", active: "add_prob"})
});
app.get("/prob/list", (req, res, next) => {
    utils.render(res, "list_prob", {title: "Add Problem", active: "list_prob"})
});


module.exports = app;