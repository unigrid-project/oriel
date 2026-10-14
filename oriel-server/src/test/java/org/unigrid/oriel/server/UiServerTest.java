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

import java.net.http.HttpResponse;
import net.jqwik.api.Example;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UiServerTest extends ServedTest {
	@Example
	public void shouldServeTheRenderedPage() throws Exception {
		final HttpResponse<String> response = admitted().get("/");

		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("<h1>Janus</h1>"), response.body());
	}

	@Example
	public void shouldBindToLoopbackOnly() {
		assertEquals("127.0.0.1", base().getHost());
		assertTrue(base().getPort() > 0, "an ephemeral port should have been assigned");
	}
}
