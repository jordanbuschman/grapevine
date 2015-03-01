var mongoose              = require('mongoose');
var Schema                = mongoose.Schema;

var Menu = new Schema({
    group: [{
        name: String,
        description: String,
        _id: false,
        item: [{
            name: { type: String, required: true },
            description: String,
            image: String,
            price: { type: Number, required: true }, 
            _id: false,
            step: [{
                text: { type: String, required: true },
                required: { type: Boolean, required: true },
                maxOptions: { type: Number, required: true },
                _id: false,
                option: [{
                    text: String,
                    priceModifier: { type: Number, default: 0 }, 
                    _id: false,
                }]
            }]
        }]
    }]
});
