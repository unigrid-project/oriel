# Oriel
<img align="right" width="300px" height="auto" src="documentation/oriel-logo.png" alt="Oriel">

[![Oriel build status](https://github.com/unigrid-project/oriel/actions/workflows/maven.yml/badge.svg)](https://github.com/unigrid-project/oriel/actions/workflows/maven.yml)

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

Releases are published to GitHub Packages.

## License

Oriel is free software, licensed under the [GNU Lesser General Public License, version 3](COPYING.LESSER).
