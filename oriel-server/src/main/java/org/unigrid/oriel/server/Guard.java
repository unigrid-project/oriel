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

import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

/**
 * Refuses anything that cannot prove it is the interface this process opened. A loopback port is
 * reachable by every other program on the machine, and this one can move money.
 */
class Guard extends Handler.Wrapper {
	private static final String COOKIE_SUFFIX = "-session";

	private final String cookieName;
	private final SessionToken token;

	Guard(final Handler handler, final AppId id, final SessionToken token) {
		super(handler);
		this.cookieName = cookie(id);
		this.token = token;
	}

	/* Named after the application, so that two of them open in one browser do not overwrite each other's. */
	static String cookie(final AppId id) {
		return id.value() + COOKIE_SUFFIX;
	}

	@Override
	public boolean handle(final Request request, final Response response, final Callback callback)
		throws Exception {

		if (foreignOrigin(request)) {
			return refuse(response, callback);
		}

		if (holdsTheToken(request)) {
			return super.handle(request, response, callback);
		}

		/* The very first navigation carries the token in the query, because there is nowhere
		   else to put it before a cookie exists. It is exchanged for a cookie immediately so
		   that it does not stay in the address or in referrers. */
		if (token.matches(Request.extractQueryParameters(request).getValue(SessionToken.PARAMETER))) {
			return adopt(request, response, callback);
		}

		return refuse(response, callback);
	}

	private boolean adopt(final Request request, final Response response, final Callback callback) {
		response.getHeaders().put(HttpHeader.SET_COOKIE,
			cookieName + "=" + token.value() + "; Path=/; HttpOnly; SameSite=Strict"
		);

		Response.sendRedirect(request, response, callback, Request.getPathInContext(request));
		return true;
	}

	private boolean refuse(final Response response, final Callback callback) {
		response.setStatus(HttpStatus.FORBIDDEN_403);
		callback.succeeded();
		return true;
	}

	/*
	 * Every cookie is looked at, not only the first: the page may set cookies of its own, and cookies do not keep
	 * to a port, so any other page on this address can add one, even one named like the session cookie. A cookie
	 * that is not the token neither admits nor refuses; only the token admits.
	 */
	private boolean holdsTheToken(final Request request) {
		for (final HttpCookie cookie : Request.getCookies(request)) {
			if (cookieName.equals(cookie.getName()) && token.matches(cookie.getValue())) {
				return true;
			}
		}

		return false;
	}

	/* A page in the user's own browser can reach this port. It cannot read the responses, but
	   without this it could still fire requests that act. */
	private boolean foreignOrigin(final Request request) {
		final String origin = request.getHeaders().get(HttpHeader.ORIGIN);

		return origin != null && !origin.equals(expectedOrigin(request));
	}

	private String expectedOrigin(final Request request) {
		return "http://" + request.getHeaders().get(HttpHeader.HOST);
	}
}
