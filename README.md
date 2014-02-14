selenium-framework-concept2
===========================

Selenium framework concept.  Demonstrates the following concepts:

| # | Feature | Description          |
| ------------- | ----------- | ---------- |
| 1 | TestNG | Project uses TestNG. |
| 2 | Maven Surefire | Running TestNG from a Maven Surefire goal. |
| 3 | Custom HTML Report | Generating a customized report from a TestNG report listener. |
| 4 | Multi-Threaded | Running each test class in separate threads.  This sample project runs 4 threads. |
| 5 |Lazy Loaded Pages | Lazily loaded page objects on demand. Does not use pre-loaded locators with PageFactory for page objects.   |
| 6 | Lazy Loaded WebDriver Instance | Lazily loaded WebDriver.  Does not initialize WebDriver until @Test starts. |
| 7 |Builder Design Pattern | WebDriver instance contained within a class using the "Builder" design pattern.  Still need to implement that. |
| 8 | TestNG DataProvider | Implemented a DataProvider that loads parameterized data from the TestNG XML context. |
| 9 | Improved HTML Report | HTML test report shows human readable time values.  See CustomReport.html after tests finish. |
| 10 | SauceLabs Integration | Project works with Selenium Grid, SauceLabs, or as standalone WebDriver.  Uploads test result to SauceLabs. |
