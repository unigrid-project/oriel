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

import java.time.Duration;

/** Ends the process once a shutdown has had its time, for the times it never finishes on its own. */
final class ShutdownDeadline {
	private ShutdownDeadline() {
	}

	/* A daemon, so a shutdown that does finish in time is never held up by the wait. */
	static void start(final Duration grace, final Runnable end) {
		Thread.ofPlatform().name("shutdown-deadline").daemon().start(() -> {
			try {
				Thread.sleep(grace);
				end.run();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
	}
}
