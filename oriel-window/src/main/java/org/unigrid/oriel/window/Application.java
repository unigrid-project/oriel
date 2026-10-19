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
import java.util.Objects;

/**
 * The application the window belongs to: the name it goes by in the title bar and when it cannot start, the icons
 * the desktop shows for it, and the size it opens at and cannot be made smaller than. Oriel assumes none of it; the
 * application gives all of it. An empty list of icons leaves the desktop to show its own.
 */
public record Application(String name, List<Image> icons, Dimension size, Dimension minimumSize) {
	/* Dimension can be changed by whoever holds it, so the record keeps copies of its own. */
	public Application {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("An application needs a name");
		}

		Objects.requireNonNull(icons, "An application gives its icons, or an empty list for the desktop's own");
		Objects.requireNonNull(size, "An application gives the size its window opens at");
		Objects.requireNonNull(minimumSize, "An application gives the size its window cannot be made smaller than");

		if (size.width < minimumSize.width || size.height < minimumSize.height) {
			throw new IllegalArgumentException("The window cannot open smaller than it is allowed to be");
		}

		icons = List.copyOf(icons);
		size = new Dimension(size);
		minimumSize = new Dimension(minimumSize);
	}

	@Override
	public Dimension size() {
		return new Dimension(size);
	}

	@Override
	public Dimension minimumSize() {
		return new Dimension(minimumSize);
	}
}
