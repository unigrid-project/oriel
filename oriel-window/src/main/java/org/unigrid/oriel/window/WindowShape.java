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
import java.awt.Frame;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Optional;

/**
 * The outline of the main window: rounded corners while it floats and square corners while it is
 * maximised, the way native windows behave.
 */
final class WindowShape {
	private WindowShape() {
	}

	static Optional<Shape> of(final int extendedState, final Dimension size) {
		if ((extendedState & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
			return Optional.empty();
		}

		return Optional.of(new RoundRectangle2D.Double(0, 0, size.width, size.height, Constants.CORNER_ARC,
			Constants.CORNER_ARC
		));
	}
}
