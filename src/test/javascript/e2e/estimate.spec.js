/**
 * Created by jukka on 11.4.2016.
 */
'use strict';
describe('estimate spec', function() {
    beforeAll(function () {
        var el = element(by.css('h1'));
        //console.log(by.css('h1'));
        browser.get('/');
        //browser.driver.wait(protractor.until.elementIsVisible(el));
        element(by.model('username')).sendKeys('admin');
        element(by.model('password')).sendKeys('admin');
        element(by.css('button[type=submit]')).click();
    });

    beforeEach(function() {
        element.all(by.css('[ui-sref="project"]')).first().click();
        element.all(by.css('[ui-sref*="project.detail"]')).first().click();
    });

    it('should create an estimate', function() {
        element(by.css('[ui-sref*="estimate.new"]')).click();
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        element.all(by.css('[ui-sref*="estimate.detail"]')).first().click();
        expect(element(by.css('h2')).getText()).toMatch(/Estimate/);
    });

    it('should create & delete an estimate', function() {
        element(by.css('[ui-sref*="estimate.new"]')).click();
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        element.all(by.css('[ui-sref*="estimate.detail"]')).first().click();
        expect(element(by.css('h2')).getText()).toMatch(/Estimate \d/);
        element(by.css('[ui-sref*="estimate.delete"]')).click();
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+/);
    });

    it('should edit an estimate', function() {
        element.all(by.css('[ui-sref*="estimate.detail"]')).first().click();
        element(by.css('[ui-sref*="estimate.edit"]')).click();
        element(by.model('estimate.workdaysInMonth')).clear().sendKeys('31');
        element(by.model('estimate.dailyPrice')).clear().sendKeys('100');
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        expect(element(by.css('[test-workdays]')).getText()).toBe('31');
        expect(element(by.css('[test-total-price]')).getText()).toBe('100');
    });

    it('should clone an estimate', function() {
        element.all(by.css('[ui-sref*="estimate.detail"]')).first().click();
        element(by.css('[ng-click*="copyEstimate"]')).click();
    });

    it('should attempt creating an estimate but cancel', function() {
        element.all(by.css('[ui-sref*="estimate.new"]')).first().click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/new/);
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+/);
    });

    it('should attempt editing an estimate but cancel', function() {
        element.all(by.css('[ui-sref*="estimate.detail"]')).first().click();
        element(by.css('[ui-sref*="estimate.edit"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/edit/);
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);

    });

    it('should attempt deleting an estimate but cancel', function() {
        element.all(by.css('[ui-sref*="estimate.detail"]')).first().click();
        element(by.css('[ui-sref*="estimate.delete"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/delete/);
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);

    });

    afterAll(function () {
        element(by.id('account-menu')).click();
        element(by.id('logout')).click();
    });

});
