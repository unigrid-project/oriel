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
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartFailureTest {
	private static final File ENGINE = new File("/opt/unigrid/lib/app/jcef");
	private static final Application LEDGER = new Application("Ledger", List.of(), new Dimension(1240, 800),
		new Dimension(900, 640)
	);

	@Test
	public void shouldNameTheFolderTheEngineWasLoadedFrom() {
		final String message = StartFailure.message(LEDGER, ENGINE,
			new UnsatisfiedLinkError("libcef.so: cannot open shared object file"));

		assertTrue(message.contains(ENGINE.toString()), message);
	}

	@Test
	public void shouldSayWhatWentWrong() {
		final String message = StartFailure.message(LEDGER, ENGINE,
			new UnsatisfiedLinkError("libcef.so: cannot open shared object file"));

		assertTrue(message.contains("libcef.so: cannot open shared object file"), message);
	}

	@Test
	public void shouldNameTheApplicationThatCouldNotStart() {
		final String message = StartFailure.message(LEDGER, ENGINE, new IllegalStateException("x"));

		assertTrue(message.startsWith("Ledger could not start"), message);
		assertTrue(message.contains("Install Ledger again"), message);
	}

	@Test
	public void shouldStillSpeakWhenTheFailureHasNoMessage() {
		final String message = StartFailure.message(LEDGER, ENGINE, new IllegalStateException());

		assertTrue(message.contains("IllegalStateException"), message);
	}
}
