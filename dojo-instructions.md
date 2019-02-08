# Test-driven chaos with WireMock

Tuesday's coding dojo is "Test-driven chaos with WireMock". You'll learn:

* Some common failure scenarios occurring in HTTP based apps and services
* How to write tests for them (using WireMock)
* Fixes and mitigations


The tech stack we'll be using is:

* WireMock
* RestAssured
* JUnit
* Spring Boot
* Resilience4j

You'll need to bring a laptop with the following installed:

* Git
* JDK 8
* An IDE, preferably IntelliJ IDEA (the community edition is fine)


I strongly recommend running through the basic setup before the session. This shouldn't take more than about 5 minutes:

* Clone the exercises repository:
  https://github.com/wiremock/wiremock-resilience-examples
* Import the project into your IDE (either by importing from the Gradle build, or running `./gradlew idea` then opening the project)
* Run the test case in `ExampleTest.java`