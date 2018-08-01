<<<<<<< HEAD
**Description**

=======
Description
>>>>>>> a9f9ba863f0baafc8e2f89196683c7d146f47ef3
This project is a test automation framework for the AV Platform API’s.

Test cases are written for testing the endpoints of AV APIs as per the design documentation/specification.
<<<<<<< HEAD

**Technologies**

*Rest Assured

*JUnit

*Serenity BDD

**Prerequisites / Requirements**

=======
Git Location : https://github.com/PhysicalGraph/avplatformtests
Technologies used
Rest Assured
JUnit
Serenity BDD

Prerequisites / Requirements
>>>>>>> a9f9ba863f0baafc8e2f89196683c7d146f47ef3
Java 

Maven

IntelliJ IDEA 
<<<<<<< HEAD

**Test setup**

Create a Samsung Account
 
=======
Test setup
Create a Samsung Account 
>>>>>>> a9f9ba863f0baafc8e2f89196683c7d146f47ef3
Create a personal access token(User Token)

Onboard two cameras(referred as Sources)

Create source tokens for the two cameras(Sources)
<<<<<<< HEAD

The tests are currently configured to run in Staging environment on real devices/cameras

**Note** : This setup is not required to clone and run the project.

**Instructions**

=======
The tests are currently configured to run in Staging environment on real devices/cameras.
Note : This setup is not required to clone and run the project. 
Instructions
>>>>>>> a9f9ba863f0baafc8e2f89196683c7d146f47ef3
Download and install JDK on your machine

Set JAVA_HOME environment variable

Download and install Maven

Set M2_HOME, M2, MAVEN_OPTS environment variables
<<<<<<< HEAD

**Test Run**

=======
Test Run
>>>>>>> a9f9ba863f0baafc8e2f89196683c7d146f47ef3
Clone the repo and import to IntelliJ

In IntelliJ select Run -> Edit Configurations -> Maven

Working directory :  “Add Project path” 

Command line  : “clean verify serenity:aggregate -e”

Note : For successful build ensure that both sources/cameras are online.
<<<<<<< HEAD

**Test Reports**

=======
Test Reports
>>>>>>> a9f9ba863f0baafc8e2f89196683c7d146f47ef3
 Once the test is run, detailed HTML test reports can be found at /avplatformtests/target/site/serenity/index.html


