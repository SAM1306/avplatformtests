**Description**

* This project is a test automation framework for the AV Platform API’s.

* Test cases are written for testing the endpoints of AV APIs as per the design documentation/specification.


**Technologies used**

* Rest Assured

* JUnit

* Serenity BDD

**Prerequisites / Requirements**

* Java 

* Maven

* IntelliJ IDEA
 
**Test setup**

* Create a Samsung Account
 
* Create a personal access token(User Token)

* Onboard two cameras(referred as Sources)

* Create source tokens for the two cameras(Sources)

* The tests are currently configured to run in Staging environment on real devices/cameras.

**Note** : This setup is not required to clone and run the project. 

**Instructions**

* Download and install JDK on your machine

* Set JAVA_HOME environment variable

* Download and install Maven

* Set M2_HOME, M2, MAVEN_OPTS environment variables

**Test Run**

* Clone the repo and import to IntelliJ

* In IntelliJ select Run -> Edit Configurations -> Maven

* Working directory :  “Add Project path” 

* Command line  : “clean verify serenity:aggregate -e”

**Note** : For successful build ensure that both sources/cameras are online.

**Test Reports**
 Once the test is run, detailed HTML test reports can be found at /avplatformtests/target/site/serenity/index.html
`