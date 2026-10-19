/*
    Oriel
    Copyright © 2021-2026 Stiftelsen The Unigrid Foundation

    This library is free software: you can redistribute it and/or modify it under the terms of the
    GNU Lesser General Public License as published by the Free Software Foundation, version 3 of the
    License (see COPYING.LESSER and COPYING).

    This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
    even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License with this library.
    If not, see <http://www.gnu.org/licenses/> and <https://github.com/unigrid-project/oriel>.
 */

package org.unigrid.oriel.window;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Where the browser engine lives. An installed Janus carries it inside the application folder and names that
 * folder through a property; a development run has no such property and keeps the engine in the home folder.
 */
public class CefLocation {
	private final String bundled;
	private final Path home;

	public CefLocation() {
		this(System.getProperty(Constants.ENGINE_FOLDER_PROPERTY), Path.of(System.getProperty("user.home")));
	}

	CefLocation(final String bundled, final Path home) {
		this.bundled = bundled;
		this.home = home;
	}

	public boolean isBundled() {
		return Objects.nonNull(bundled) && !bundled.isBlank();
	}

	public File directory() {
		return isBundled() ? new File(bundled) : home.resolve(Constants.ENGINE_HOME_FOLDER).toFile();
	}
}
