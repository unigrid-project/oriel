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

import java.awt.Dimension;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.unigrid.oriel.server.AppId;
import org.unigrid.oriel.window.launch.WindowSystem;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* All of it is decided before any window is made, so the test needs no screen. */
public class BrowserWindowTest {
	private static final AppId ID = AppId.of("ledger");
	private static final Application LEDGER = new Application(ID, "Ledger", List.of(), new Dimension(1240, 800),
		new Dimension(900, 640)
	);

	@Test
	public void shouldRefuseToOpenForNoApplication() {
		assertThrows(NullPointerException.class, () -> new BrowserWindow(null, WindowSystem.X11));
		assertThrows(NullPointerException.class, () -> new BrowserWindow(null));
	}

	@Test
	public void shouldRefuseToOpenWithoutAWindowSystem() {
		assertThrows(NullPointerException.class, () -> new BrowserWindow(LEDGER, null));
	}

	@Test
	public void shouldDrawThroughOpenGlOnAMacThatLeftTheChoiceOpen() {
		assertTrue(BrowserWindow.avoidsMetal("Mac OS X", null));
	}

	@Test
	public void shouldKeepTheChoiceAnApplicationMadeItself() {
		assertFalse(BrowserWindow.avoidsMetal("Mac OS X", "true"));
		assertFalse(BrowserWindow.avoidsMetal("Mac OS X", "false"));
	}

	@Test
	public void shouldLeaveOtherSystemsAlone() {
		assertFalse(BrowserWindow.avoidsMetal("Linux", null));
		assertFalse(BrowserWindow.avoidsMetal("Windows 11", null));
		assertFalse(BrowserWindow.avoidsMetal(null, null));
	}
}
