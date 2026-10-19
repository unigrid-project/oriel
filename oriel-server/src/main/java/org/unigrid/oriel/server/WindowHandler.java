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
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.Fields;
import org.unigrid.oriel.server.WindowControl.Edge;

class WindowHandler extends Handler.Abstract {
	private final WindowControl window;

	WindowHandler(final WindowControl window) {
		this.window = window;
	}

	@Override
	public boolean handle(final Request request, final Response response, final Callback callback) {
		final String command = Request.getPathInContext(request).substring(1);
		final Fields query = Request.extractQueryParameters(request);

		if (Constants.CHOOSE_FILE.equals(command)) {
			return answerWith(window.chooseFile(title(query)), response, callback);
		}

		if (Constants.SAVE_FILE.equals(command)) {
			final String name = value(query, Constants.FILE_NAME_PARAMETER);

			return answerWith(window.saveFile(title(query), name), response, callback);
		}

		/* Unknown commands are refused rather than passed on, as unknown actions are. Falling through to
		   the page would answer a mistake in the markup with 200 and the whole interface. */
		if (!perform(command)) {
			response.setStatus(HttpStatus.NOT_FOUND_404);
			callback.succeeded();
			return true;
		}

		response.setStatus(HttpStatus.NO_CONTENT_204);
		callback.succeeded();
		return true;
	}

	private static String title(final Fields query) {
		return value(query, Constants.DIALOG_TITLE_PARAMETER);
	}

	private static String value(final Fields query, final String name) {
		return Objects.requireNonNullElse(query.getValue(name), "");
	}

	private static boolean answerWith(final Optional<Path> chosen, final Response response, final Callback callback) {
		if (chosen.isEmpty()) {
			response.setStatus(HttpStatus.NO_CONTENT_204);
			callback.succeeded();
			return true;
		}

		response.setStatus(HttpStatus.OK_200);
		response.getHeaders().put(HttpHeader.CONTENT_TYPE, Constants.TEXT);
		response.write(true, ByteBuffer.wrap(chosen.get().toString().getBytes(StandardCharsets.UTF_8)), callback);
		return true;
	}

	private boolean perform(final String command) {
		if (command.startsWith(Constants.RESIZE_START)) {
			return beginResize(command.substring(Constants.RESIZE_START.length()));
		}

		switch (command) {
			case "minimise" -> window.minimise();
			case "maximise" -> window.toggleMaximise();
			case "close" -> window.close();
			case "move/start" -> window.beginMove();
			case "move/end" -> window.endMove();
			case "resize/end" -> window.endResize();
			default -> {
				return false;
			}
		}

		return true;
	}

	private boolean beginResize(final String name) {
		final Optional<Edge> edge = Edge.named(name);

		edge.ifPresent(window::beginResize);
		return edge.isPresent();
	}
}
