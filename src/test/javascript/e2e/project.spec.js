/**
 * Created by jukka on 11.4.2016.
 */

'use strict';

describe('project', function() {
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
    });

    it('should load projects', function() {
        expect(element.all(by.css('h2')).first().getText()).toMatch(/Projects/);
    });

    it('should create a project', function() {
        element(by.css('[ui-sref="project.new"]')).click();
        element(by.model('project.name')).sendKeys('Testiprojekti');
        element(by.model('project.description')).sendKeys('Testiprojektin kuvaus');
        element(by.model('project.client')).sendKeys('Testiasiakas');
        element(by.model('project.deadline')).sendKeys('2016-03-03');
        element(by.model('project.docLocation')).sendKeys('http://www.google.com');
        element(by.model('project.stateDescription')).sendKeys('Ihan mahtava');
        element(by.css('.tags-input')).element(by.model('newTag.text')).click().sendKeys('Tagi, Toinen, Kolmas,');
        element(by.css('button[type=submit]')).click();
        expect(element(by.css('h2')).getText()).toMatch(/Testiprojekti/);
    });

    it('should edit a project', function() {
        element.all(by.css('[ui-sref*="project.detail"]')).first().click();
        element(by.css('[ui-sref*="project.edit"]')).click();
        element(by.model('project.name')).sendKeys(' - muokattu');
        element(by.css('.tags-input')).element(by.model('newTag.text')).click().sendKeys('Yksi,');
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        expect(element(by.css('h2')).getText()).toMatch(/Testiprojekti - muokattu/);
    });

    it('should create & delete a project', function() {
        element(by.css('[ui-sref="project.new"]')).click();
        element(by.model('project.name')).sendKeys('Deletointiprojekti');
        element(by.model('project.description')).sendKeys('Deletointiprojektin kuvaus');
        element(by.model('project.client')).sendKeys('Testiasiakas');
        element(by.model('project.deadline')).sendKeys('2016-03-03');
        element(by.model('project.docLocation')).sendKeys('http://www.google.com');
        element(by.model('project.stateDescription')).sendKeys('Ihan mahtava');
        element(by.css('button[type=submit]')).click();
        element(by.css('[ui-sref*="project.delete"]')).click();
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        expect(element.all(by.css('td')).first().getText()).not.toMatch(/Deletointiprojekti/);
    });

    it('should attempt creating a project but cancel', function() {
        element(by.css('[ui-sref="project.new"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch("/projects/new");
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch("/projects");
    });

    it('should attempt editing a project but cancel', function() {
        element.all(by.css('[ui-sref*="project.detail"]')).first().click();
        element(by.css('[ui-sref*="project.edit"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/\d+\/edit/);
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+/);
    });

    it('should attempt deleting a project but cancel', function() {
        element.all(by.css('[ui-sref*="project.detail"]')).first().click();
        element(by.css('[ui-sref*="project.delete"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/\d+\/delete/);
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+/);
    });


    afterAll(function () {
        element(by.id('account-menu')).click();
        element(by.id('logout')).click();
    });

});
