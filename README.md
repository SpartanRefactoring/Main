# Spartan [![Build Status](https://travis-ci.org/SpartanRefactoring/spartan.svg?branch=master)](https://travis-ci.org/SpartanRefactoring/spartan) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/il.org.spartan/spartan/badge.svg)](https://maven-badges.herokuapp.com/maven-central/il.org.spartan/spartan/)

Spartan is a collection of tools designed to assist with Java source code analysis and manipulation. It is the basis for the [Spartan Refactoring](https://github.com/SpartanRefactoring/spartan-refactoring) Eclipse plugin.

## Obtaining the library
The library is available in Maven Central, so you can easily embed it in your projects.

#### Maven

For Maven projects, add this to your pom.xml:

```
<dependencies>
    <dependency>
        <groupId>il.org.spartan</groupId>
        <artifactId>spartan</artifactId>
        <version>1.0</version>
    </dependency>
    ...
</dependencies>
```

#### Gradle

For Gradle projects, add this to your build.gradle:
```
compile 'il.org.spartan:spartan:1.0'
```

#### Compiling from source

To compile the library from source, clone this repository to your local computer and run:
```
mvn package
```

Then, include the generated .jar file in your project.

## License

This library is an open source project and is available under the [MIT License](https://opensource.org/licenses/MIT)
