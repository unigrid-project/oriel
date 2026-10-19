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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import org.unigrid.oriel.server.action.View;
import org.unigrid.oriel.server.template.Templates;

class UiHandler extends Handler.Abstract {
	private static final String HTML = "text/html;charset=utf-8";

	private final Templates templates;
	private final Supplier<? extends View> index;

	UiHandler(final Templates templates, final Supplier<? extends View> index) {
		this.templates = templates;
		this.index = index;
	}

	@Override
	public boolean handle(final Request request, final Response response, final Callback callback) {
		final ByteBuffer body = ByteBuffer.wrap(templates.render(index.get()).getBytes(StandardCharsets.UTF_8));

		response.setStatus(HttpStatus.OK_200);
		response.getHeaders().put(HttpHeader.CONTENT_TYPE, HTML);
		response.write(true, body, callback);
		return true;
	}
}
