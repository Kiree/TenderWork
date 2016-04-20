/**
 * Created by jukka on 12.4.2016.
 */

'use strict';

describe('task spec', function() {
    beforeAll(function() {
        browser.get('/');
        element(by.model('username')).sendKeys('admin');
        element(by.model('password')).sendKeys('admin');
        element(by.css('button[type=submit]')).click();
    });

    beforeEach(function() {
        element.all(by.css('[ui-sref="project"]')).first().click();
        element.all(by.css('[ui-sref*="project.detail"]')).first().click();
        element.all(by.css('[ui-sref*="estimate.detail"]')).first().click();
    });

    it('should create a task', function() {
        element.all(by.css('[ui-sref*="task.new"]')).first().click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/task\/new/);
        element(by.model('task.name')).sendKeys('Taski vainen');
        element(by.model('task.description')).sendKeys('Kuvaus vainen');
        element(by.model('task.estimateSpecification')).sendKeys('10');
        element(by.model('task.estimateImplementation')).sendKeys('10');
        element(by.model('task.estimateTesting')).sendKeys('10');
        element(by.css('.tags-input')).element(by.model('newTag.text')).click().sendKeys('tasktag, Tokatask, Kolmastask,');
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });

    it('should create and delete a task', function() {
        element.all(by.css('[ui-sref*="task.new"]')).first().click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/task\/new/);
        element(by.model('task.name')).sendKeys('Taski vainen');
        element(by.model('task.description')).sendKeys('Kuvaus vainen');
        element(by.model('task.estimateSpecification')).sendKeys('10');
        element(by.model('task.estimateImplementation')).sendKeys('10');
        element(by.model('task.estimateTesting')).sendKeys('10');
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
        //expect(element(by.css("jh-alert")).element(by.css(".alert-success")).getText()).toMatch(/A Task is created with identifier \d/);
        element(by.css('[ng-click*="deleteTask"]')).click();
        //expect(element(by.css("jh-alert")).element(by.css(".alert-success")).getText()).toMatch(/A Task is deleted with identifier \d/);
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });

    it('should edit a task', function() {
        var alertElement = element(by.css('.alert-success'));
        element(by.css('[ui-sref*="task.edit"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/\d+\/task\/edit/);
        element(by.model('task.name')).sendKeys(" - muokattu");
        element(by.css('.tags-input')).element(by.model('newTag.text')).click().sendKeys('taskyks');
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });


    it('should attempt to edit a task but cancel', function() {
        element(by.css('[ui-sref*="task.edit"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/\d+\/task\/edit/);
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });

    it('should attempt to create a task but cancel', function() {
        element(by.css('[ui-sref*="task.new"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/task\/new/);
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });

    afterAll(function() {
        element(by.id('account-menu')).click();
        element(by.id('logout')).click();
    });
});
