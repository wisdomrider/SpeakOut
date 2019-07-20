h = {};


h.handleError = (res, err) => {
    res.status(406).json({success: false, message: err.message});
};

h.constants = {
    successMessage: "successMessage",
    errorMessage: "errorMessage",
    warningMessage: "warningMessage"
};


h.render = (res, page, vars = {}) => {
    let successMessage = res.req.cookies[h.constants.successMessage];
    let errorMessage = res.req.cookies[h.constants.errorMessage];
    let warningMessage = res.req.cookies[h.constants.warningMessage];
    if (errorMessage !== undefined)
        vars[h.constants.errorMessage] = errorMessage;
    if (successMessage !== undefined)
        vars[h.constants.successMessage] = successMessage;
    if (warningMessage !== undefined)
        vars[h.constants.warningMessage] = warningMessage;
    res.clearCookie(h.constants.successMessage);
    res.clearCookie(h.constants.warningMessage);
    res.clearCookie(h.constants.errorMessage);
    res.render(page, vars);
};


module.exports.data = h;