# TenderWork

This application was generated using JHipster, you can find documentation and help at [https://jhipster.github.io](https://jhipster.github.io).

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Gulp][] as our build system. Install the Gulp command-line tool globally with:

    npm install -g gulp

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    gulp

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

# Building for production

Tenderwork is configured to be used with SQL-server, the default JDBC driver used is [JTDS][].

To optimize the TenderWork client for production, run:

    mvn -Pprod clean package -Dmaven.test.skip=true -Dyo.test.skip -U

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

# Script for server

	- Example script to be used with Jenkins / Artifactory CI-pipe
	- Replace server-ip's, ports, usernames, passwords, databaseName etc. with your own values


``` bash
#!/bin/bash
cat << "EOF"                                                                                                                                                                                                                    $
                                                                     dddddddd
TTTTTTTTTTTTTTTTTTTTTTT                                              d::::::d
T:::::::::::::::::::::T                                              d::::::d
T:::::::::::::::::::::T                                              d::::::d
T:::::TT:::::::TT:::::T                                              d:::::d
TTTTTT  T:::::T  TTTTTTeeeeeeeeeeee    nnnn  nnnnnnnn        ddddddddd:::::d     eeeeeeeeeeee    rrrrr   rrrrrrrrr
        T:::::T      ee::::::::::::ee  n:::nn::::::::nn    dd::::::::::::::d   ee::::::::::::ee  r::::rrr:::::::::r
        T:::::T     e::::::eeeee:::::een::::::::::::::nn  d::::::::::::::::d  e::::::eeeee:::::eer:::::::::::::::::r
        T:::::T    e::::::e     e:::::enn:::::::::::::::nd:::::::ddddd:::::d e::::::e     e:::::err::::::rrrrr::::::r
        T:::::T    e:::::::eeeee::::::e  n:::::nnnn:::::nd::::::d    d:::::d e:::::::eeeee::::::e r:::::r     r:::::r
        T:::::T    e:::::::::::::::::e   n::::n    n::::nd:::::d     d:::::d e:::::::::::::::::e  r:::::r     rrrrrrr
        T:::::T    e::::::eeeeeeeeeee    n::::n    n::::nd:::::d     d:::::d e::::::eeeeeeeeeee   r:::::r
        T:::::T    e:::::::e             n::::n    n::::nd:::::d     d:::::d e:::::::e            r:::::r
      TT:::::::TT  e::::::::e            n::::n    n::::nd::::::ddddd::::::dde::::::::e           r:::::r
      T:::::::::T   e::::::::eeeeeeee    n::::n    n::::n d:::::::::::::::::d e::::::::eeeeeeee   r:::::r
      T:::::::::T    ee:::::::::::::e    n::::n    n::::n  d:::::::::ddd::::d  ee:::::::::::::e   r:::::r
      TTTTTTTTTTT      eeeeeeeeeeeeee    nnnnnn    nnnnnn   ddddddddd   ddddd    eeeeeeeeeeeeee   rrrrrrr
WWWWWWWW                           WWWWWWWW                                 kkkkkkkk
W::::::W                           W::::::W                                 k::::::k
W::::::W                           W::::::W                                 k::::::k
W::::::W                           W::::::W                                 k::::::k
 W:::::W           WWWWW           W:::::W ooooooooooo   rrrrr   rrrrrrrrr   k:::::k    kkkkkkk
  W:::::W         W:::::W         W:::::Woo:::::::::::oo r::::rrr:::::::::r  k:::::k   k:::::k
   W:::::W       W:::::::W       W:::::Wo:::::::::::::::or:::::::::::::::::r k:::::k  k:::::k
    W:::::W     W:::::::::W     W:::::W o:::::ooooo:::::orr::::::rrrrr::::::rk:::::k k:::::k
     W:::::W   W:::::W:::::W   W:::::W  o::::o     o::::o r:::::r     r:::::rk::::::k:::::k
      W:::::W W:::::W W:::::W W:::::W   o::::o     o::::o r:::::r     rrrrrrrk:::::::::::k
       W:::::W:::::W   W:::::W:::::W    o::::o     o::::o r:::::r            k:::::::::::k
        W:::::::::W     W:::::::::W     o::::o     o::::o r:::::r            k::::::k:::::k
         W:::::::W       W:::::::W      o:::::ooooo:::::o r:::::r           k::::::k k:::::k
          W:::::W         W:::::W       o:::::::::::::::o r:::::r           k::::::k  k:::::k
           W:::W           W:::W         oo:::::::::::oo  r:::::r           k::::::k   k:::::k
            WWW             WWW            ooooooooooo    rrrrrrr           kkkkkkkk    kkkkkkk     `
EOF
# Variables
PACKAGE_LOCATION="http://ip-adress/artifactory/libs-release-local/tenderwork/tenderwork/1.0.0-RELEASE/tenderwork-1.0.0-RELEASE.war"
APPLICATION_WAR_NAME="tenderwork-1.0.0-RELEASE.war"
APPLICATION_FOLDER="tenderwork"
BREAKER="========================================================================================"
echo
echo $BREAKER
echo Update in progress, shutdown old instance, remove it, upgrade the binaries and startup.
echo Shutting down old instance...
pkill java
echo Shutdown done.
echo
echo $BREAKER
echo Removing old installation... ~/$APPLICATION_FOLDER
cd ~
rm -rf $APPLICATION_FOLDER
echo Removal done.
echo Removal done.
echo
echo $BREAKER
echo Retrieving new package...
wget $PACKAGE_LOCATION -O $APPLICATION_WAR_NAME
chmod 777 $APPLICATION_WAR_NAME
file $APPLICATION_WAR_NAME
ls -lha $APPLICATION_WAR_NAME
echo
echo $BREAKER
echo Moving the new downloaded package to ~/$APPLICATION_FOLDER
mkdir $APPLICATION_FOLDER
mv ~/$APPLICATION_WAR_NAME $APPLICATION_FOLDER/
echo Moving the file is done.
echo
echo $BREAKER
echo Running new package... $APPLICATION_WAR_NAME
cd $APPLICATION_FOLDER
java -jar $APPLICATION_WAR_NAME --spring.profiles.active=prod --debug --datasource.url=jdbc:jtds:sqlserver://server_ip:port;databaseName=databaseName;username=username;password=password --spring.data.elasticsearch.cluster-node$
# java -jar $APPLICATION_WAR_NAME --spring.profiles.active=dev --debug > tenderwork.log 2>&1 &
echo
echo $BREAKER
echo
echo Update ends
```

# Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript` and can be run with:

    gulp test

UI end-to-end tests are powered by [Protractor][], which is built on top of WebDriverJS. They're located in `src/test/javascript/e2e`
and can be run by starting Spring Boot in one terminal (`mvn spring-boot:run`) and running the tests (`grunt itest`) in a second one.

# Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `TenderWork`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/TenderWork.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* Build
    * Invoke Maven / Tasks: `-Pprod clean package -Dmaven.test.skip=true -Dyo.test.skip -U`
    * Execute Shell / Command:
        ````
        mvn spring-boot:run &
        bootPid=$!
        sleep 30s
        grunt itest
        kill $bootPid
        ````
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml,build/reports/e2e/*.xml`

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Gulp]: http://gulpjs.com/
[JTDS]: http://jtds.sourceforge.net/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
