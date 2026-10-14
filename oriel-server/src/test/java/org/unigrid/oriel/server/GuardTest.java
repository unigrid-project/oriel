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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import net.jqwik.api.Example;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GuardTest extends ServedTest {
	@Example
	public void shouldRefuseARequestWithoutTheToken() throws Exception {
		final HttpResponse<String> response = anonymous().get("/");

		assertEquals(403, response.statusCode());
	}

	@Example
	public void shouldRefuseAWrongToken() throws Exception {
		final HttpResponse<String> response = anonymous().get("/?t=not-the-token");

		assertEquals(403, response.statusCode());
	}

	@Example
	public void shouldExchangeTheQueryTokenForACookie() throws Exception {
		final Client client = anonymous();
		final HttpResponse<String> bootstrap = client.get("/?" + SessionToken.PARAMETER + "=" + token().value());

		assertEquals(200, bootstrap.statusCode(), "the redirect should have been followed to the page");
		assertTrue(bootstrap.body().contains("<h1>Janus</h1>"), bootstrap.body());
		assertEquals(200, client.get("/").statusCode(), "the cookie should carry the next request");
	}

	/* The redirect is what moves the token out of the address, so it is looked at before anything follows it. */
	@Example
	public void shouldRedirectToTheSamePathWithTheTokenInACookieInstead() throws Exception {
		final HttpClient http = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();
		final HttpResponse<String> response = http.send(HttpRequest.newBuilder(
			base().resolve("/wallet?" + SessionToken.PARAMETER + "=" + token().value())
		).build(), HttpResponse.BodyHandlers.ofString());
		final URI location = URI.create(response.headers().firstValue("Location").orElse(""));
		final String cookie = response.headers().firstValue("Set-Cookie").orElse("");

		assertEquals(302, response.statusCode());
		assertEquals("/wallet", location.getPath());
		assertNull(location.getQuery(), location.toString());
		assertTrue(cookie.startsWith(SessionToken.COOKIE + "=" + token().value() + ";"), cookie);
		assertTrue(cookie.contains("HttpOnly"), cookie);
		assertTrue(cookie.contains("SameSite=Strict"), cookie);
	}

	@Example
	public void shouldAdmitARequestSentFromItsOwnPage() throws Exception {
		final HttpResponse<String> response = admitted()
			.header("Origin", "http://" + base().getAuthority())
			.get("/");

		assertEquals(200, response.statusCode());
	}

	@Example
	public void shouldRefuseARequestSentFromSomebodyElsesPage() throws Exception {
		final HttpResponse<String> response = admitted()
			.header("Origin", "https://unigrid.example")
			.get("/window/close");

		assertEquals(403, response.statusCode());
	}

	@Example
	public void shouldGuardTheAssetsToo() throws Exception {
		assertEquals(403, anonymous().get("/static/css/testing.css").statusCode());
		assertEquals(200, admitted().get("/static/css/testing.css").statusCode());
	}

	@Example
	public void shouldIssueADifferentTokenEachTime() {
		assertTrue(!SessionToken.random().value().equals(SessionToken.random().value()));
	}

	@Example
	public void shouldRejectAnAbsentCandidate() {
		assertTrue(!token().matches(null));
	}
}
