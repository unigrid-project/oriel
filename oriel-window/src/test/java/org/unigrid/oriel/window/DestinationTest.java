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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DestinationTest {
	/* The address the window opens on: the first one still carries the session token. */
	private static final URI HOME = URI.create("http://127.0.0.1:53817/?t=token");

	@Test
	public void shouldKeepTheApplicationsOwnAddressesInTheWindow() {
		for (final String address : new String[] {"http://127.0.0.1:53817/", "http://127.0.0.1:53817/static/app.css",
			"http://127.0.0.1:53817/action/open?x=1", "HTTP://127.0.0.1:53817/window/close"}) {

			assertEquals(Destination.WINDOW, Destination.of(HOME, address), address);
		}
	}

	@Test
	public void shouldKeepChromiumsEmptyPagesInTheWindow() {
		assertEquals(Destination.WINDOW, Destination.of(HOME, "about:blank"));
		assertEquals(Destination.WINDOW, Destination.of(HOME, "about:srcdoc"));
	}

	@Test
	public void shouldSendWebAndMailAddressesToTheDesktop() {
		for (final String address : new String[] {"https://unigrid.org/", "http://example.com/a?b=c",
			"mailto:info@unigridfoundation.se"}) {

			assertEquals(Destination.DESKTOP, Destination.of(HOME, address), address);
		}
	}

	@Test
	public void shouldCountAnotherPortOrNameOfThisMachineAsElsewhere() {
		assertEquals(Destination.DESKTOP, Destination.of(HOME, "http://127.0.0.1:8080/"));
		assertEquals(Destination.DESKTOP, Destination.of(HOME, "http://localhost:53817/"));
		assertEquals(Destination.DESKTOP, Destination.of(HOME, "https://127.0.0.1:53817/"));
	}

	@Test
	public void shouldSendFilesScriptsAndOtherProgramsSchemesNowhere() {
		for (final String address : new String[] {"file:///etc/passwd", "javascript:alert(1)",
			"data:text/html,<p>x</p>", "chrome://settings", "ftp://example.com/", "steam://run/1"}) {

			assertEquals(Destination.NOWHERE, Destination.of(HOME, address), address);
		}
	}

	@Test
	public void shouldSendWhatIsNoAddressNowhere() {
		for (final String address : new String[] {null, "", "  ", "/action/open", "http://exa mple.com/"}) {
			assertEquals(Destination.NOWHERE, Destination.of(HOME, address), String.valueOf(address));
		}
	}
}
