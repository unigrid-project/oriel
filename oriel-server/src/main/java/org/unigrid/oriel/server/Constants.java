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

package org.unigrid.oriel.server;

/** The fixed values the interface server and its actions are built on, kept in one place. */
public final class Constants {
	/** The cookie the session token is kept in once the first page has loaded. */
	public static final String SESSION_COOKIE = "janus-session";

	/** The query parameter the first page load carries the session token in. */
	public static final String SESSION_PARAMETER = "t";

	/** The number of random bytes in a session token. */
	public static final int SESSION_TOKEN_BYTES = 32;

	/* Only this computer can reach the server, on a port the system picks free each start. */
	public static final String LOOPBACK = "127.0.0.1";
	public static final int EPHEMERAL_PORT = 0;

	/* Where the application's files and Oriel's own scripts are, on the classpath and in the address. */
	public static final String ASSET_ROOT = "static/";
	public static final String ASSET_PATH = "/static";
	public static final String SCRIPT_ROOT = "oriel/";
	public static final String SCRIPT_PATH = "/oriel";
	public static final String WINDOW_PATH = "/window";
	public static final String ACTION_PATH = "/action";

	/* How a view names its template: the file under the template root, then optionally a fragment of it. */
	public static final String TEMPLATE_ROOT = "templates/";
	public static final String TEMPLATE_SUFFIX = ".html";
	public static final String FRAGMENT_SELECTOR = "::";
	public static final String VIEW_VARIABLE = "view";

	/* The window commands that take more than their name, and what their dialogs are told. */
	public static final String RESIZE_START = "resize/start/";
	public static final String CHOOSE_FILE = "choose-file";
	public static final String SAVE_FILE = "save-file";
	public static final String DIALOG_TITLE_PARAMETER = "title";
	public static final String FILE_NAME_PARAMETER = "name";

	/* A submitted form is name=value pairs joined by ampersands. */
	public static final String FORM_PAIRS = "&";
	public static final String FORM_ASSIGN = "=";

	public static final String HTML = "text/html;charset=utf-8";
	public static final String TEXT = "text/plain;charset=utf-8";

	private Constants() {
	}
}
