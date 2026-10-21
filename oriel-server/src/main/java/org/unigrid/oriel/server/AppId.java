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

package org.unigrid.oriel.server;

import java.util.regex.Pattern;

/**
 * The short name an application goes by wherever Oriel names something for it: the cookie its session is kept in,
 * the property its installer names the browser engine's folder with, and the folder the engine is downloaded to.
 * It ends up in a file name, so it is lowercase letters, digits and hyphens, starting with a letter, such as
 * {@code "ledger-book"}. The application creates it once and gives the same one to the server and the window.
 */
public record AppId(String value) {
	private static final Pattern ALLOWED = Pattern.compile("[a-z][a-z0-9-]*");

	public AppId {
		if (value == null || !ALLOWED.matcher(value).matches()) {
			throw new IllegalArgumentException("An application id is lowercase letters, digits and hyphens, "
				+ "starting with a letter, such as \"ledger-book\"; " + value + " is not one");
		}
	}

	public static AppId of(final String value) {
		return new AppId(value);
	}
}
