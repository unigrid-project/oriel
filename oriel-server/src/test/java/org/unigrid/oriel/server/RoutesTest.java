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

public class RoutesTest extends ServedTest {
	@Example
	public void shouldServeTheStylesheetAsCss() throws Exception {
		final HttpResponse<String> response = admitted().get("/static/oriel-test/testing.css");

		assertEquals(200, response.statusCode());
		assertTrue(response.headers().firstValue("content-type").orElse("").startsWith("text/css"),
			response.headers().toString()
		);
		assertTrue(response.body().contains("--accent"), response.body());
	}

	@Example
	public void shouldServeItsOwnScriptsBesideTheApplicationsFiles() throws Exception {
		final Client client = admitted();
		final HttpResponse<String> script = client.get("/oriel/window.js");

		assertEquals(200, script.statusCode());
		assertTrue(script.headers().firstValue("content-type").orElse("").contains("javascript"),
			script.headers().toString()
		);
		assertTrue(script.body().contains("/window/"), script.body());
		assertEquals(200, client.get("/oriel/htmx.min.js").statusCode());
		assertEquals(200, client.get("/static/oriel-test/testing.css").statusCode());
	}

	@Example
	public void shouldFallThroughToThePageForUnknownPaths() throws Exception {
		final Client client = admitted();

		assertTrue(client.get("/").body().contains("<h1>Janus</h1>"));
		assertTrue(client.get("/wallet").body().contains("<h1>Janus</h1>"));
	}
}
