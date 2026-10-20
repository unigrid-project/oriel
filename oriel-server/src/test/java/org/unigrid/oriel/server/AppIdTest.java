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

package org.unigrid.oriel.server;

import net.jqwik.api.Example;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppIdTest {
	@Example
	public void shouldTakeLowercaseLettersDigitsAndHyphens() {
		assertEquals("oriel-wallet2", AppId.of("oriel-wallet2").value());
	}

	@Example
	public void shouldRefuseWhatCannotBeAFileName() {
		for (final String value : new String[] {"", "Janus", "oriel wallet", "2wallet", "-wallet", "oriel.wallet",
			"../wallet", "oriel_wallet"}) {

			assertThrows(IllegalArgumentException.class, () -> AppId.of(value), value);
		}
	}

	@Example
	public void shouldRefuseNoId() {
		assertThrows(IllegalArgumentException.class, () -> AppId.of(null));
	}

	@Example
	public void shouldNameTheSessionCookieAfterTheApplication() {
		assertEquals("oriel-wallet-session", Guard.cookie(AppId.of("oriel-wallet")));
	}
}
