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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

/**
 * Tells the browser what a page may load and run. The pages act for the person using the application, so text from
 * outside that ever reached one unescaped must not be able to run as script, restyle the page or reach beyond the
 * application: scripts, styles, images and fonts come from the application's own address only, and nothing written
 * into the page runs. Two things are let through: the style htmx adds for its request indicators, by its exact
 * hash, and style attributes, which can only change the element they are on.
 */
final class SecurityHeaders extends Handler.Wrapper {
	/* What htmx adds to the head when it loads; a test keeps it in step with the htmx Oriel serves. */
	static final String HTMX_STYLE = ".htmx-indicator{opacity:0;visibility: hidden} "
		+ ".htmx-request .htmx-indicator, .htmx-request.htmx-indicator{opacity:1;visibility: visible;"
		+ "transition: opacity 200ms ease-in}";

	static final String POLICY = String.join("; ",
		"default-src 'self'",
		"script-src 'self'",
		"style-src 'self' '" + hash() + "'",
		"style-src-attr 'unsafe-inline'",
		"img-src 'self'",
		"font-src 'self'",
		"connect-src 'self'",
		"object-src 'none'",
		"base-uri 'none'",
		"form-action 'self'",
		"frame-ancestors 'none'"
	);

	SecurityHeaders(final Handler handler) {
		super(handler);
	}

	@Override
	public boolean handle(final Request request, final Response response, final Callback callback)
		throws Exception {

		final HttpFields.Mutable headers = response.getHeaders();

		headers.put("Content-Security-Policy", POLICY);
		headers.put("X-Content-Type-Options", "nosniff");
		headers.put("Referrer-Policy", "no-referrer");
		return super.handle(request, response, callback);
	}

	/* A policy names an inline style it allows by the SHA-256 of its text, in base64. */
	static String hash() {
		try {
			final byte[] digest = MessageDigest.getInstance("SHA-256")
				.digest(SecurityHeaders.HTMX_STYLE.getBytes(StandardCharsets.UTF_8));

			return "sha256-" + Base64.getEncoder().encodeToString(digest);
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("Every Java runtime has SHA-256", ex);
		}
	}
}
