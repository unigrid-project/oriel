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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import org.unigrid.oriel.server.action.ActionFailed;
import org.unigrid.oriel.server.action.Actions;
import org.unigrid.oriel.server.action.Form;
import org.unigrid.oriel.server.action.View;
import org.unigrid.oriel.server.template.Templates;

@Slf4j
class ActionHandler extends Handler.Abstract {
	private static final String HTML = "text/html;charset=utf-8";

	private final Actions actions;
	private final Templates templates;

	ActionHandler(final Actions actions, final Templates templates) {
		this.actions = actions;
		this.templates = templates;
	}

	@Override
	public boolean handle(final Request request, final Response response, final Callback callback)
		throws Exception {

		final String name = Request.getPathInContext(request).substring(1);

		/* Unknown actions are refused rather than passed on. A page falling back to the whole
		   interface when a swap was expected hides the mistake instead of showing it. */
		if (!actions.knows(name)) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
			callback.succeeded();
			return true;
		}

		final Optional<View> shown;

		try {
			shown = actions.invoke(name, Form.parse(Content.Source.asString(request)));
		} catch (ActionFailed e) {
			/* The page only learns that something failed. Why is kept here, where it can be found afterwards. */
			log.error("The action {} failed", name, e.getCause());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			callback.succeeded();
			return true;
		}

		if (shown.isEmpty()) {
			response.setStatus(HttpStatus.NO_CONTENT_204);
			callback.succeeded();
			return true;
		}

		final byte[] body = templates.render(shown.get()).getBytes(StandardCharsets.UTF_8);

		response.setStatus(HttpStatus.OK_200);
		response.getHeaders().put(HttpHeader.CONTENT_TYPE, HTML);
		response.write(true, ByteBuffer.wrap(body), callback);
		return true;
	}
}
