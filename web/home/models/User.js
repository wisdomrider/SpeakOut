var mongoose = require('mongoose');
var Schema = mongoose.Schema;


var User = new Schema({
    name: {
        type: String,
        required: true
    },
    phoneNumber: {
        type: String,
        required: true,
        unique: true
    },
    token: {
        type: String,
        required: false
    }
});
module.exports = mongoose.model('User', User);