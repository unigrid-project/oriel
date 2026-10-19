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

import java.util.function.Supplier;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.unigrid.oriel.server.action.Actions;
import org.unigrid.oriel.server.action.View;
import org.unigrid.oriel.server.template.Templates;
import org.unigrid.oriel.server.window.WindowControl;

public final class Routes {
	/* Where the application's files and Oriel's own scripts are, on the classpath and in the address. */
	private static final String ASSETS = "static/";
	private static final String ASSET_PATH = "/static";
	private static final String SCRIPTS = "oriel/";
	private static final String SCRIPT_PATH = "/oriel";
	private static final String WINDOW_PATH = "/window";
	private static final String ACTION_PATH = "/action";

	private Routes() {
	}

	public static Handler create(final Templates templates, final SessionToken token, final View index) {
		return create(templates, token, WindowControl.NONE, Actions.of(), index);
	}

	public static Handler create(final Templates templates, final SessionToken token,
		final WindowControl window, final Actions actions, final View index) {

		return create(templates, token, window, actions, () -> index);
	}

	public static Handler create(final Templates templates, final SessionToken token,
		final WindowControl window, final Actions actions, final Supplier<? extends View> index) {

		/* Anything the file handlers do not recognise falls through to the pages,
		   so an unknown path renders the interface rather than a Jetty error page. */
		return new Guard(new Handler.Sequence(
			new ContextHandler(files(ASSETS), ASSET_PATH),
			new ContextHandler(files(SCRIPTS), SCRIPT_PATH),
			new ContextHandler(new WindowHandler(window), WINDOW_PATH),
			new ContextHandler(new ActionHandler(actions, templates), ACTION_PATH),
			new UiHandler(templates, index)
		), token);
	}

	/* The scripts the pages run sit under a root of their own, because the classpath answers with the first
	   folder of a name it finds, and the application keeps a static folder of its own. */
	private static ResourceHandler files(final String root) {
		final ResourceHandler files = new ResourceHandler();

		files.setBaseResource(ResourceFactory.of(files).newClassLoaderResource(root));
		files.setDirAllowed(false);
		return files;
	}
}
