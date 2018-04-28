# Domain Driven Design using Dependency Injection

This project was made as an example of this post: https://www.linkedin.com/pulse/adapting-customer-demands-using-configurable-system-manny-lavery/

<br>
This project is an extremely basic representation on how to create an application who's behaviour can be configured through simple state changes.

Using such systems we can create ever more complicated but easily adjustable systems compared to the conventional large anemic systems that dominate today.

The use of Domain Driven Design assists in this because it consolidates both behaviour and data to the objects that represent the domain, in our case Customers and Companies.

This allows us to change the behaviour of these domains individually using a smaller number of objects. In keeping the overall footprint of the application small allows us to iterate more quickly or if necessary change directions completely.

## Running the Application
1. gradlew build
2. gradlew bootRun

Note: If you run into an issue such as: **de.flapdoodle.embed.process.exceptions.DistributionException** it may be because Java does not have internet access.
## Changing behaviour
In DddiApplication:65 you can simply change the dao used to store/retrive the Customer objects. It will generate random data for that and when the application later calls to retrieve all the stored objects you will see them reflected there (except RestDao).
<br>

## Technology
### RxJava
RxJava is my standard reactive library on any personal application. This library ensures the application follows reactive principals. Pull in rxjava-scheduler-spring-boot-starter if you need more control over the RxJava scheduling.

### Spring Repositories
Easy to setup, test and use so personal projects I prefer them. In my professional career I use the Java MongoDB driver because it is more up to date and gives me the control I find necessary to make the database performant.

### Spring Boot
Because its so simple to create, configure, test and run.