/**
 * Created by jukka on 11.4.2016.
 */
describe('requirement spec', function() {
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

    it('should create a requirement', function() {
        element(by.css('[ui-sref*="requirement.new"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/requirement\/new/);
        element(by.model('requirement.name')).sendKeys("Testirequirement");
        element(by.model('requirement.description')).sendKeys("Tsetirequirementin kuvaus");
        element(by.css('.tags-input')).element(by.model('newTag.text')).click().sendKeys('Reqtag, Tokareq, Kolmasreq,');
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });

    it('should create and delete a requirement', function() {
        element(by.css('[ui-sref*="requirement.new"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/requirement\/new/);
        element(by.model('requirement.name')).sendKeys("Testirequirement");
        element(by.model('requirement.description')).sendKeys("Tsetirequirementin kuvaus");
        element(by.css('.tags-input')).element(by.model('newTag.text')).click().sendKeys('Reqtag, Tokareq, Kolmasreq,');
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        element(by.css('[ui-sref*="requirement.delete"]')).click();
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });

    it('should edit a requirement', function() {
        element(by.css('[ui-sref*="requirement.edit"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/\d+\/requirement\/edit/);
        element(by.model('requirement.name')).sendKeys(" - muokattu");
        element(by.css('.tags-input')).element(by.model('newTag.text')).click().sendKeys('Reqyks');
        element(by.css(".modal-footer")).element(by.css('button[type=submit]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });

    it('should attempt to edit a requirement but cancel', function() {
        element(by.css('[ui-sref*="requirement.edit"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/\d+\/requirement\/edit/);
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });

    it('should attempt to delete a requirement but cancel', function() {
        element(by.css('[ui-sref*="requirement.delete"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/\d+\/requirement\/delete/);
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });

    it('should attempt to create requirement but cancel', function() {
        element(by.css('[ui-sref*="requirement.new"]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+\/requirement\/new/);
        element(by.css(".modal-footer")).element(by.css('button[type=button]')).click();
        expect(browser.getLocationAbsUrl()).toMatch(/\/projects\/project\/\d+\/estimate\/\d+/);
    });

    afterAll(function() {
        element(by.id('account-menu')).click();
        element(by.id('logout')).click();
    });
});
