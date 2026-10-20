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

package org.unigrid.oriel.window;

import java.io.File;
import java.util.Objects;

/**
 * What a person sees when the window cannot be opened. A launched application has no console, so without
 * this a damaged browser engine would leave a server running and nothing on the screen.
 */
public final class StartFailure {
	private StartFailure() {
	}

	/** The message for an engine that could not be started from the folder it was looked for in. */
	public static String message(final Application application, final File engine, final Throwable cause) {
		final String reason = Objects.requireNonNullElse(cause.getMessage(), cause.getClass().getSimpleName());

		return application.name() + " could not start its browser engine from " + engine + ".\n\n"
			+ reason + "\n\nInstall " + application.name() + " again to restore it.";
	}
}
