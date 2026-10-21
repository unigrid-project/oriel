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

package org.unigrid.oriel.server.template;

import net.jqwik.api.Example;
import org.unigrid.oriel.server.Page;
import org.unigrid.oriel.server.Status;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemplatesTest {
	private final Templates templates = new Templates(false);

	@Example
	public void shouldResolveTemplateFromClasspath() {
		final String html = templates.render(new Page("Ledger"));

		assertTrue(html.contains("<!DOCTYPE html>"), html);
	}

	@Example
	public void shouldEvaluateExpressionsRatherThanEchoTheFile() {
		final String html = templates.render(new Page("Unigrid"));

		assertTrue(html.contains("<h1>Unigrid</h1>"), html);
		assertTrue(!html.contains("th:text"), "the th: attributes should be consumed during rendering");
	}

	@Example
	public void shouldRenderOnlyTheNamedBlockOfATemplate() {
		final String html = templates.render(new Status("Alice"));

		assertTrue(html.contains("unlocked by <span>Alice</span>"), html);
		assertTrue(!html.contains("<!DOCTYPE html>"), "a block should not carry the whole page: " + html);
	}
}
