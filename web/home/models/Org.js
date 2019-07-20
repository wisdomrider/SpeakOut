var mongoose = require('mongoose');
var Schema = mongoose.Schema;


var Org = new Schema({
    org_name: {
        type: String,
        required: true
    },
    org_number: {
        type: String,
        required: true
    },
    location: {
        type: String,
        required: true
    }
});
module.exports = mongoose.model('Org', Org);