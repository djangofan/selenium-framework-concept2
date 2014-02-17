selenium-framework-concept2
===========================

Selenium framework concept.  Demonstrates the following concepts:

| # | Feature | Description          |
| ------------- | ----------- | ---------- |
| 1 | TestNG | Project uses TestNG. |
| 2 | Maven Surefire | Running TestNG from a Maven Surefire goal. |
| 3 | Custom HTML Report | Generating a customized report from a TestNG report listener that can contains any custom result related content that you wish. |
| 4 | Multi-Threaded | Running each test class in separate threads.  This sample project runs 4 threads. |
| 5 |Lazy Loaded Pages | Lazily loaded page objects on demand. Does not use pre-loaded locators with PageFactory for page objects.   |
| 6 | Lazy Loaded WebDriver Instance | Lazily loaded WebDriver.  Does not initialize WebDriver until @Test starts. |
| 7 |Builder Design Pattern | WebDriver instance contained within a SeHelper class using the "Builder" design pattern. |
| 8 | TestNG DataProvider | Implemented a DataProvider that loads parameterized data from the TestNG XML file/context itself. |
| 9 | Improved HTML Report | HTML test report shows human readable time values.  See CustomReport.html after tests finish. |
| 10 | SauceLabs Integration | Project works with Selenium Grid, SauceLabs, or as standalone WebDriver.  Uploads test result to SauceLabs. |

#### Setup

This project will run with or without a Selenium grid.  When running as a Grid, this project also supports SauceLabs grid.   Configuration of this is found in the testng.xml file.

To run this project:

1.  Run grid hub with 'startGridHub.bat'
2.  Run grid node with 'startGridNode.bat'
3.  Run tests with 'mvn surefire:test' or 'mvn test' and look at HTML report generated under "target/surefire-reports" OR run tests as a TestNG project and look at report generated under "/test-output".
