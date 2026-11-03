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

package org.unigrid.oriel.server;

import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.jqwik.api.Example;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityHeadersTest extends ServedTest {
	/* Where htmx builds the style it adds; its minified names change with its version, and then this has to too. */
	private static final Pattern HTMX_BUILDS = Pattern.compile("`<style\\$\\{e}>`\\+(.+?)\\+\"</style>\"");

	@Example
	public void shouldSendThePolicyWithThePage() throws Exception {
		assertCarriesTheHeaders(admitted().get("/"));
	}

	@Example
	public void shouldSendThePolicyWithAFileAndAScript() throws Exception {
		final Client client = admitted();

		assertCarriesTheHeaders(client.get("/static/oriel-test/testing.css"));
		assertCarriesTheHeaders(client.get("/oriel/window.js"));
	}

	@Example
	public void shouldSendThePolicyWithARefusal() throws Exception {
		final HttpResponse<String> refused = anonymous().get("/");

		assertEquals(403, refused.statusCode());
		assertCarriesTheHeaders(refused);
	}

	@Example
	public void shouldLoadNothingFromElsewhereAndRunNothingWrittenIntoThePage() {
		assertTrue(SecurityHeaders.POLICY.contains("default-src 'self'"), SecurityHeaders.POLICY);
		assertTrue(SecurityHeaders.POLICY.contains("script-src 'self';"), SecurityHeaders.POLICY);
		assertTrue(SecurityHeaders.POLICY.contains("img-src 'self'"), SecurityHeaders.POLICY);
		assertTrue(SecurityHeaders.POLICY.contains("font-src 'self'"), SecurityHeaders.POLICY);
		assertTrue(SecurityHeaders.POLICY.contains("object-src 'none'"), SecurityHeaders.POLICY);
	}

	@Example
	public void shouldNameAStyleByTheHashABrowserComputes() {
		assertEquals("sha256-faU7yAF8NxuMTNEwVmBz+VcYeIoBQ2EMHW3WaVxCvnk=",
			SecurityHeaders.hash()
		);
	}

	@Example
	public void shouldAllowExactlyTheStyleTheServedHtmxAdds() throws Exception {
		final Matcher built = HTMX_BUILDS.matcher(admitted().get("/oriel/htmx.min.js").body());

		assertTrue(built.find(), "htmx no longer builds its indicator style the way this test reads it");
		assertEquals(SecurityHeaders.HTMX_STYLE, built.group(1).replace("`+`", "").replace("`", "")
			.replace("${t}", "htmx-indicator").replace("${n}", "htmx-request")
		);
	}

	private static void assertCarriesTheHeaders(final HttpResponse<String> response) {
		assertEquals(SecurityHeaders.POLICY,
			response.headers().firstValue("content-security-policy").orElse(""), response.headers().toString()
		);
		assertEquals("nosniff", response.headers().firstValue("x-content-type-options").orElse(""));
		assertEquals("no-referrer", response.headers().firstValue("referrer-policy").orElse(""));
	}
}
