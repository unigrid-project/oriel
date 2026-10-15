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
import java.util.concurrent.atomic.AtomicInteger;
import net.jqwik.api.Example;
import org.eclipse.jetty.server.Handler;
import org.unigrid.oriel.server.action.Actions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UiHandlerTest extends ServedTest {
	private final AtomicInteger visits = new AtomicInteger();

	@Override
	protected Handler routes() {
		return Routes.create(templates(), token(), WindowControl.NONE, Actions.of(),
			() -> new Page("Visit " + visits.incrementAndGet())
		);
	}

	@Example
	public void shouldAnswerWithTheWholePageAsHtml() throws Exception {
		final HttpResponse<String> page = admitted().get("/");
		final String type = page.headers().firstValue("Content-Type").orElse("");

		assertEquals(200, page.statusCode());
		assertTrue(type.startsWith("text/html"), type);
		assertTrue(page.body().contains("<!DOCTYPE html>"), page.body());
	}

	/* What the page opens on can change while Janus runs, such as once a wallet has been chosen. */
	@Example
	public void shouldWorkOutThePageAgainForEveryRequest() throws Exception {
		final Client client = admitted();

		assertTrue(client.get("/").body().contains("Visit 2"));
		assertTrue(client.get("/").body().contains("Visit 3"));
	}
}
