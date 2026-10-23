# Oriel

**Oriel is to Java what Electron is to JavaScript.**

Oriel is a library for building desktop applications in Java with an interface written in HTML and CSS. It is the Java equivalent of [Electron](https://www.electronjs.org/) and takes its inspiration from there: an embedded Chromium draws the window, while the application itself stays an ordinary Java program with full access to the JVM and everything that runs on it.

Where Electron pairs Chromium with Node.js, Oriel pairs it with Java. The pages are rendered on the server side with [Thymeleaf](https://www.thymeleaf.org/) and made interactive with [htmx](https://htmx.org/), so an interface needs no JavaScript framework and no build step of its own.

## Building

Oriel needs Java 25. Build and test it with the Maven wrapper:

```sh
./mvnw verify
```

## Using it

Releases are made from tags, and an application fetches them through [JitPack](https://jitpack.io/#unigrid-project/oriel), which builds the tag it is asked for:

```xml
<repositories>
	<repository>
		<id>jitpack</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.unigrid-project.oriel</groupId>
	<artifactId>oriel-window</artifactId>
	<version>v0.0.1</version>
</dependency>
```

While working on Oriel itself, `./mvnw install` puts it in the local Maven repository instead, as `org.unigrid.oriel` at the version in the pom.

## License

Oriel is free software, licensed under the [GNU Lesser General Public License, version 3](COPYING.LESSER).
