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
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class UiServer {
	/* The wallet holds keys, so the interface is never reachable from off the machine.
	   Port zero keeps it off a predictable address that another local process could
	   guess ahead of time. */

	private final Server server = new Server();
	private final ServerConnector connector = new ServerConnector(server);

	public UiServer(final Handler handler) {
		connector.setHost(Constants.LOOPBACK);
		connector.setPort(Constants.EPHEMERAL_PORT);
		server.addConnector(connector);
		server.setHandler(handler);
	}

	public URI start() throws Exception {
		server.start();
		return URI.create("http://" + Constants.LOOPBACK + ":" + connector.getLocalPort() + "/");
	}

	public void stop() throws Exception {
		server.stop();
	}

	public void join() throws InterruptedException {
		server.join();
	}
}
