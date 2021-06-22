# Gremlin Take Home Assignment

The Gremlin take home assignment, submitted by Kenneth Worden!

## Usage

This project makes use of Gradle to build, run, and execute tests.

### Getting a quote

To get a quote, you can simply run the project. You can achieve this with:

```./gradlew run --args='--language [english, russian]'```

For example:

```./gradlew run --args='--language English'```

The weird syntax is because Gradle is the program being run. You can generate
a more traditional application, see **Building the application** below.

### Running tests

Use the `test` Gradle task:

```./gradlew test```

### Building the application

This project can generate an application which can be run more traditionally:

```./gremlin run --args='--language [english, russian]'```

To build this, run:

```./gradlew installDist```

This should generate a `gremlin` executable shell script in `build/install/gremlin/bin/`
which can be run as its own application.

To simply compile classes, a normal build will do:

```./gradlew build```


## Design

Because the project is so small, I left the package structure flat.

I decided to abstract away the actual quote API call into `ForismaticDriver`. This would
help by keeping networking logic largely abstracted away from the application's logic.

The `QuoteRetriever` is the star of the show. Trying to adhere to the Single-Responsibility-Principle,
the class would only be responsible for obtaining quotes, given the desired language. `ForismaticDriver`
was made a dependency of this class with the idea of allowing extension into additional quote services. I
envisioned a scenario where a future developer would like to use a different or multiple drivers-
perhaps one could later make a driver for quotes stored in a local flat file, or ones that live in different
APIs.

I did not get the chance to realize/stub out what this driver extension might look like, but ideally it would
start with an `QuoteDriver` interface that could be implemented for different sources.

The `App` class holds the entrypoint to the application.

## External libraries

I used JUnit and Mockito for testing. I also use Apache commons libraries for the CLI and for HTTP request handling.

## Where to go from here

As mentioned above, I would have liked to design a `QuoteDriver` interface to allow for new quote sources to be easily
added to the application. I would have also liked to add a dependency-injection library like Dagger; the want to use
DI informed my class design. However, adding Dagger seemed to be overkill for such a small project, so I manually
instantiated the classes in `App`.

Overall, I'm pleased with the project and I think it showcases how I would design a larger application.