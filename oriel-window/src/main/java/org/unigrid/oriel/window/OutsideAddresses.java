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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hands an address that leads off the application to the desktop's browser or mail program, once the person agrees.
 * The whole address is shown, so that a link named after one place but leading to another is seen before anything
 * opens. One question is asked at a time, so a page asking again and again cannot bury the window in them.
 */
final class OutsideAddresses {
	private static final Logger LOG = LoggerFactory.getLogger(OutsideAddresses.class);
	private static final String OPEN = "Open";
	private static final String CANCEL = "Cancel";

	/* The buttons in the order they are shown; the dialog answers with the place of the one chosen. */
	private static final List<String> CHOICES = List.of(OPEN, CANCEL);
	private static final int OPEN_CHOSEN = CHOICES.indexOf(OPEN);

	/* The question, with the application's name and the whole address filled in. */
	private static final String QUESTION = "Open this address outside %s?\n\n%s";

	private final JFrame frame;
	private final String name;
	private final AtomicBoolean asking = new AtomicBoolean();

	/**
	 * Asks over the given window, under the application's name.
	 *
	 * @param frame the window the question is shown over
	 * @param name the application's name, used as the question's title and in its text
	 */
	OutsideAddresses(final JFrame frame, final String name) {
		this.frame = frame;
		this.name = name;
	}

	/**
	 * Offers an address to the desktop: asks the person, and opens it if they agree. It returns at once, since the
	 * browser engine calls it from threads that must not wait on a question, and it drops the address when another
	 * one is already being asked about.
	 *
	 * @param address the address to offer, a web or mail address
	 */
	void offer(final URI address) {
		if (!asking.compareAndSet(false, true)) {
			LOG.info("Not offering {} while another address is being offered", address);
			return;
		}

		SwingUtilities.invokeLater(() -> {
			try {
				if (agreed(address)) {
					Thread.startVirtualThread(() -> open(address));
				}
			} finally {
				asking.set(false);
			}
		});
	}

	/**
	 * Shows the whole address and asks whether to open it outside the application; cancelling is the default.
	 *
	 * @param address the address to show
	 * @return true if the person chose to open it
	 */
	private boolean agreed(final URI address) {
		final String question = QUESTION.formatted(name, address.toASCIIString());

		return JOptionPane.showOptionDialog(frame, question, name, JOptionPane.DEFAULT_OPTION,
			JOptionPane.QUESTION_MESSAGE, null, CHOICES.toArray(), CANCEL) == OPEN_CHOSEN;
	}

	/**
	 * Opens the address in the desktop's mail program for a mail address, and in its browser otherwise. The desktop
	 * may take its time to answer, so it runs on a thread of its own, and only logs what the desktop cannot do.
	 *
	 * @param address the address to open
	 */
	private static void open(final URI address) {
		if (!Desktop.isDesktopSupported()) {
			LOG.warn("This desktop cannot open {}", address);
			return;
		}

		final Desktop desktop = Desktop.getDesktop();

		try {
			final boolean mail = Destination.MAILTO.equalsIgnoreCase(address.getScheme());

			if (mail && desktop.isSupported(Desktop.Action.MAIL)) {
				desktop.mail(address);
			} else if (desktop.isSupported(Desktop.Action.BROWSE)) {
				desktop.browse(address);
			} else {
				LOG.warn("This desktop has no browser to open {} in", address);
			}
		} catch (IOException | UnsupportedOperationException | SecurityException ex) {
			LOG.warn("The desktop could not open {}", address, ex);
		}
	}
}
