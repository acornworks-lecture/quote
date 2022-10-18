const assert = require('assert');
const { When, Then } = require('@cucumber/cucumber');
const fetch = require('node-fetch');

When('An user ask to get a price ratio for {string} from the system', async (ticker) => {
    const response = await fetch('http://localhost:65080/quote/ratio/' + ticker);
    this.responseData = data = await response.json();
});

When('An user ask to get a price for {string} from the system', async (ticker) => {
    const response = await fetch('http://localhost:65080/quote/' + ticker);
    this.responseData = data = await response.json();
});

Then('The user receives {string}', (rateStr) => {
    if (Object.keys(this.responseData).includes('ratio')) {
        assert.equal(parseFloat(rateStr), this.responseData['ratio']);
    } else {
        assert.equal(parseFloat(rateStr), this.responseData['price']);
    }
});

