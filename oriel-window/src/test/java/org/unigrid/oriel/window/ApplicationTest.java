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

import java.awt.Dimension;
import java.awt.Image;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationTest {
	private static final Dimension OPENING = new Dimension(1240, 800);
	private static final Dimension MINIMUM = new Dimension(900, 640);

	@Test
	public void shouldTakeWhatTheApplicationSaysAboutItself() {
		final List<Image> icons = WindowIcon.images(WindowIconTest.class, WindowIconTest.ICONS);
		final Application application = new Application("Ledger", icons, OPENING, MINIMUM);

		assertEquals("Ledger", application.name());
		assertEquals(icons, application.icons());
		assertEquals(OPENING, application.size());
		assertEquals(MINIMUM, application.minimumSize());
	}

	@Test
	public void shouldRefuseAnApplicationWithoutAName() {
		assertThrows(IllegalArgumentException.class, () -> new Application(" ", List.of(), OPENING, MINIMUM));
		assertThrows(IllegalArgumentException.class, () -> new Application(null, List.of(), OPENING, MINIMUM));
	}

	@Test
	public void shouldAssumeNothingTheApplicationLeavesOut() {
		assertThrows(NullPointerException.class, () -> new Application("Ledger", null, OPENING, MINIMUM));
		assertThrows(NullPointerException.class, () -> new Application("Ledger", List.of(), null, MINIMUM));
		assertThrows(NullPointerException.class, () -> new Application("Ledger", List.of(), OPENING, null));
	}

	@Test
	public void shouldRefuseToOpenSmallerThanItsMinimum() {
		assertThrows(IllegalArgumentException.class,
			() -> new Application("Ledger", List.of(), new Dimension(800, 600), MINIMUM));
	}

	@Test
	public void shouldKeepItsSizesWhateverHappensToTheOnesHandedInOrOut() {
		final Dimension opening = new Dimension(OPENING);
		final Application application = new Application("Ledger", List.of(), opening, MINIMUM);

		opening.width = 1;
		application.size().width = 1;

		assertEquals(OPENING, application.size());
	}
}
