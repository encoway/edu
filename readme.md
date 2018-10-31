Event Driven Updates
====================

[![Build Status](https://travis-ci.org/encoway/edu.svg?branch=master)](https://travis-ci.org/encoway/edu)

Event Driven Updates (EDU) helps decoupling components of AJAX heavy JSF UIs.

EDU does so by allowing components to request being re-rendered/updated based on an **event**.
An event in terms of EDU is just a name through which it can be referenced by a triggering component,
e.g., *configuration-changed*.

## Demo

There is an integration test based on [Mojarra](https://github.com/encoway/edu/tree/master/edu-it-mojarra).
It can be launched running the maven command `mvn wildfly:run` from within the module folder and viewed in a browser 
at http://localhost:8080/edu-it-mojarra.  

## Usage

For usage instructions see module [`edu`](edu).

## Integration

In order to use EDU in a web application the `edu.jar` must be present on the classpath.

EDU has **no transitive dependencies**. It expects `javax.javaee-web-api` to be present on the class path.

### Maven

Add the following dependency to the `<dependencies>` section of your `pom.xml`:

```xhtml
<dependency>
    <groupId>com.encoway</groupId>
    <artifactId>edu</artifactId>
    <version>X.Y.Z</version>
</dependency>
```

Releases are hosted on [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ccom.encoway).
Additionally releases and snapshots are hosted by [Sonatype](http://central.sonatype.org/)
at `https://oss.sonatype.org/content/groups/public`.
In order to use snapshots make sure their repository is defined in the `<repositories>` section of your `pom.xml`:

```xml
<repository>  
    <id>Sonatype repository</id>  
    <name>Sonatype's Maven repository</name>  
    <url>http://oss.sonatype.org/content/groups/public</url>  
    <releases>
        <enabled>true</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```

## License

EDU is licensed under Apache 2.0.
