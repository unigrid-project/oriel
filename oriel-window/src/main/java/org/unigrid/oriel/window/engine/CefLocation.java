/*
    Oriel
    Copyright © 2026 Stiftelsen The Unigrid Foundation

    This library is free software: you can redistribute it and/or modify it under the terms of the
    GNU Lesser General Public License as published by the Free Software Foundation, version 3 of the
    License (see COPYING.LESSER and COPYING).

    This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
    even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License with this library.
    If not, see <http://www.gnu.org/licenses/> and <https://github.com/unigrid-project/oriel>.
 */

package org.unigrid.oriel.window.engine;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import org.unigrid.oriel.server.AppId;

/**
 * Where the browser engine lives. An installed application carries it inside its own folder and names that folder
 * through a property named after it, such as {@code -Dledger-book.jcef=...}; a development run has no such property
 * and keeps the engine in a folder of the application's own in the home folder, such as {@code ~/.ledger-book/jcef}.
 * Each application keeps its own, so that one moving to another version of the engine leaves the others alone.
 */
public class CefLocation {
	private static final String PROPERTY_SUFFIX = ".jcef";
	private static final String ENGINE_FOLDER = "jcef";

	private final String bundled;
	private final Path downloaded;

	public CefLocation(final AppId id) {
		this(Objects.requireNonNull(id, "The engine is looked for by the application it belongs to"),
			System.getProperty(id.value() + PROPERTY_SUFFIX), Path.of(System.getProperty("user.home"))
		);
	}

	CefLocation(final AppId id, final String bundled, final Path home) {
		this.bundled = bundled;
		this.downloaded = home.resolve("." + id.value()).resolve(ENGINE_FOLDER);
	}

	public boolean isBundled() {
		return Objects.nonNull(bundled) && !bundled.isBlank();
	}

	public File directory() {
		return isBundled() ? new File(bundled) : downloaded.toFile();
	}
}
