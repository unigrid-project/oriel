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

import java.awt.Image;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WindowIconTest {
	static final String ICONS = "icons/test-%d.png";

	@Test
	public void shouldReadTheIconAtEveryStandardSize() {
		assertEquals(
			List.of(16, 24, 32, 48, 64, 128, 256),
			WindowIcon.images(WindowIconTest.class, ICONS).stream().map(image -> image.getWidth(null)).toList()
		);
	}

	@Test
	public void shouldReadEverySizeSquare() {
		for (Image image : WindowIcon.images(WindowIconTest.class, ICONS)) {
			assertEquals(image.getWidth(null), image.getHeight(null));
		}
	}

	@Test
	public void shouldSayWhichIconIsMissing() {
		final IllegalArgumentException missing = assertThrows(IllegalArgumentException.class,
			() -> WindowIcon.images(WindowIconTest.class, "icons/none-%d.png"));

		assertEquals("There is no icon icons/none-16.png beside " + WindowIconTest.class.getName(),
			missing.getMessage());
	}
}
