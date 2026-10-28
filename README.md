# Oriel
<img align="right" width="300px" height="auto" src="documentation/oriel-logo.png" alt="Oriel">

[![Oriel build status](https://github.com/unigrid-project/oriel/actions/workflows/maven.yml/badge.svg)](https://github.com/unigrid-project/oriel/actions/workflows/maven.yml)
[![Test coverage](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2Funigrid-project%2Foriel%2Fbadges%2Fcoverage.json)](https://github.com/unigrid-project/oriel/actions/workflows/maven.yml)

**Oriel is to Java what Electron is to JavaScript.**

Oriel is a library for building desktop applications in Java with an interface written in HTML and CSS. It is the Java equivalent of [Electron](https://www.electronjs.org/) and takes its inspiration from there: an embedded Chromium draws the window, while the application itself stays an ordinary Java program with full access to the JVM and everything that runs on it.

Where Electron pairs Chromium with Node.js, Oriel pairs it with Java. The pages are rendered on the server side with [Thymeleaf](https://www.thymeleaf.org/) and made interactive with [htmx](https://htmx.org/), so an interface needs no JavaScript framework and no build step of its own.

## Documentation

The [wiki](https://github.com/unigrid-project/oriel/wiki) explains Oriel and how to build an application with it:

- [Getting started](https://github.com/unigrid-project/oriel/wiki/Getting-started): the smallest application, with one view, one action and one window
- [Actions](https://github.com/unigrid-project/oriel/wiki/Actions) and [views and templates](https://github.com/unigrid-project/oriel/wiki/Views-and-templates)
- [The server and its security](https://github.com/unigrid-project/oriel/wiki/Server-and-security)
- [The window](https://github.com/unigrid-project/oriel/wiki/The-window): the frame, resizing, file dialogs, X11 and Wayland
- [Testing an application](https://github.com/unigrid-project/oriel/wiki/Testing) with the test-jar helpers
- [Building and packaging](https://github.com/unigrid-project/oriel/wiki/Building)

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
	<version>0.0.1</version>
</dependency>
```

While working on Oriel itself, `./mvnw install` puts it in the local Maven repository instead, as `org.unigrid.oriel` at the version in the pom.

## License

Oriel is free software, licensed under the [GNU Lesser General Public License, version 3](COPYING.LESSER).
