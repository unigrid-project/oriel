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
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * An application's icon at the sizes window managers and task bars ask for. Each size is drawn ahead of time,
 * because scaling one large image down at run time leaves the small ones blurred, so the application keeps one
 * file per size beside a class of its own and names them with a pattern such as {@code "icons/app-%d.png"}.
 */
public final class WindowIcon {
	private WindowIcon() {
	}

	public static List<Image> images(final Class<?> owner, final String pattern) {
		return Constants.ICON_SIZES.stream().map(size -> read(owner, pattern.formatted(size))).toList();
	}

	private static Image read(final Class<?> owner, final String name) {
		final URL resource = owner.getResource(name);

		if (resource == null) {
			throw new IllegalArgumentException("There is no icon " + name + " beside " + owner.getName());
		}

		try {
			return ImageIO.read(resource);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}
}
