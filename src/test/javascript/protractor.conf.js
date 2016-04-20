var HtmlScreenshotReporter = require("protractor-jasmine2-screenshot-reporter");
var JasmineReporters = require('jasmine-reporters');

exports.config = {
    seleniumServerJar: '../../../node_modules/protractor/selenium/selenium-server-standalone-2.47.1.jar',
    chromeDriver: '../../../node_modules/protractor/selenium/chromedriver.exe',
    allScriptsTimeout: 20000,

    specs: [
        'e2e/project.spec.js',
        'e2e/estimate.spec.js',
        'e2e/requirement.spec.js',
        'e2e/task.spec.js',
        'e2e/clone.spec.js'
    ],

    capabilities: {
        'browserName': 'chrome',
        'phantomjs.binary.path': require('phantomjs').path,
        'phantomjs.ghostdriver.cli.args': ['--loglevel=DEBUG']
    },

    directConnect: true,

    baseUrl: 'http://localhost:8080/',

    framework: 'jasmine2',

    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 30000
    },

    onPrepare: function() {
        browser.driver.manage().window().setSize(1280, 1024);
        jasmine.getEnv().addReporter(new JasmineReporters.JUnitXmlReporter({
            savePath: 'target/reports/e2e',
            consolidateAll: false
        }));
        jasmine.getEnv().addReporter(new HtmlScreenshotReporter({
            dest: "target/reports/e2e/screenshots"
        }));
        require('protractor-uisref-locator')(protractor);
    }
};
