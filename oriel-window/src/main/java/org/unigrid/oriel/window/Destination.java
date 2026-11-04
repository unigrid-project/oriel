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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Set;

/**
 * Where an address a page leads to belongs. The window shows the application's own address only, the one it was
 * opened on, so that a link to anywhere else cannot take the window and the controls its page draws. Web and mail
 * addresses go to the desktop's own programs instead, and anything else, such as a file, a script or a scheme of
 * another program, goes nowhere.
 */
enum Destination {
	/** The address loads in the window, since it is the application's own. */
	WINDOW,

	/** The address is offered to the desktop's browser or mail program, and the window stays where it is. */
	DESKTOP,

	/** The address is followed nowhere, since it is neither the application's nor one for the desktop. */
	NOWHERE;

	/* Chromium starts every frame on an empty page of its own before it loads anything. */
	private static final String ABOUT_BLANK = "about:blank";
	private static final String ABOUT_SRCDOC = "about:srcdoc";
	private static final Set<String> EMPTY_PAGES = Set.of(ABOUT_BLANK, ABOUT_SRCDOC);

	/** The scheme of a mail address, which the desktop opens in its mail program rather than its browser. */
	static final String MAILTO = "mailto";

	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static final Set<String> OUTSIDE_SCHEMES = Set.of(HTTP, HTTPS, MAILTO);

	/* The ports an address uses when it names none, which an address read from text gives as -1. */
	private static final int NO_PORT = -1;
	private static final int HTTP_PORT = 80;
	private static final int HTTPS_PORT = 443;

	/**
	 * Tells where an address a page leads to belongs, given the address the window was opened on. An empty page of
	 * Chromium's own and the application's own address stay in the window, a web or mail address goes to the
	 * desktop, and anything else, including what cannot be read as an address, goes nowhere.
	 *
	 * @param application the address the window was opened on
	 * @param address the address the page leads to, as the browser engine gives it, or null
	 * @return where the address belongs, never null
	 */
	static Destination of(final URI application, final String address) {
		if (address == null || address.isBlank()) {
			return NOWHERE;
		}

		if (EMPTY_PAGES.contains(address.toLowerCase(Locale.ROOT))) {
			return WINDOW;
		}

		final URI target;

		try {
			target = new URI(address);
		} catch (URISyntaxException ex) {
			return NOWHERE;
		}

		if (target.getScheme() == null) {
			return NOWHERE;
		}

		if (sameOrigin(application, target)) {
			return WINDOW;
		}

		return OUTSIDE_SCHEMES.contains(target.getScheme().toLowerCase(Locale.ROOT)) ? DESKTOP : NOWHERE;
	}

	/**
	 * Tells whether an address is on the same origin as the application's: the same scheme, host and port, so another
	 * port or another name for the same machine counts as somewhere else.
	 *
	 * @param application the address the window was opened on
	 * @param target the address the page leads to
	 * @return true if both are on the same origin
	 */
	private static boolean sameOrigin(final URI application, final URI target) {
		return application.getScheme().equalsIgnoreCase(target.getScheme()) && target.getHost() != null
			&& application.getHost().equalsIgnoreCase(target.getHost()) && port(application) == port(target);
	}

	/**
	 * Gives the port an address uses, filling in the scheme's usual one when the address names none.
	 *
	 * @param address the address to read the port of
	 * @return the port it names, or 443 for https and 80 otherwise
	 */
	private static int port(final URI address) {
		if (address.getPort() != NO_PORT) {
			return address.getPort();
		}

		return HTTPS.equalsIgnoreCase(address.getScheme()) ? HTTPS_PORT : HTTP_PORT;
	}
}
