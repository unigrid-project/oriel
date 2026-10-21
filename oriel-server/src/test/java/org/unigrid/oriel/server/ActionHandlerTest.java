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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import net.jqwik.api.Example;
import org.eclipse.jetty.server.Handler;
import org.slf4j.LoggerFactory;
import org.unigrid.oriel.server.action.Action;
import org.unigrid.oriel.server.action.Actions;
import org.unigrid.oriel.server.action.Form;
import org.unigrid.oriel.server.window.WindowControl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActionHandlerTest extends ServedTest {
	private final Ledger ledger = new Ledger();

	public class Ledger {
		private final List<String> unlocked = new ArrayList<>();

		@Action("lock-ledger")
		public void onClickLockLedger() {
			unlocked.clear();
		}

		@Action("unlock-ledger")
		public Status onClickUnlockLedger(final Form form) {
			unlocked.add(form.get("passphrase"));
			return new Status(form.get("passphrase"));
		}

		@Action("break-ledger")
		public void onClickBreakLedger() {
			throw new IllegalStateException("The ledger file is damaged");
		}

		@Action("misreport-ledger")
		public String onClickMisreportLedger() {
			return "not a view";
		}

		public List<String> unlocked() {
			return unlocked;
		}
	}

	@Override
	protected Handler routes() {
		return Routes.create(id(), templates(), token(), WindowControl.NONE, Actions.of(ledger), new Page("Ledger"));
	}

	@Example
	public void shouldCallTheAnnotatedMethod() throws Exception {
		assertEquals(204, admitted().post("/action/lock-ledger").statusCode());
	}

	@Example
	public void shouldHandTheFormToTheMethod() throws Exception {
		final HttpResponse<String> response = admitted()
			.submit("/action/unlock-ledger", "passphrase=open+sesame%21");

		assertEquals(200, response.statusCode());
		assertEquals(List.of("open sesame!"), ledger.unlocked());
	}

	@Example
	public void shouldRenderTheViewThatWasReturned() throws Exception {
		final HttpResponse<String> response = admitted()
			.submit("/action/unlock-ledger", "passphrase=x");

		assertTrue(response.body().contains("Ledger is unlocked"), response.body());
		assertTrue(!response.body().contains("<!DOCTYPE html>"),
			"a fragment should be returned rather than the whole page: " + response.body()
		);
	}

	@Example
	public void shouldRefuseAnActionItDoesNotHave() throws Exception {
		assertEquals(404, admitted().post("/action/shred-ledger").statusCode());
	}

	@Example
	public void shouldNotActForAnUnknownCaller() throws Exception {
		assertEquals(403, anonymous().post("/action/lock-ledger").statusCode());
	}

	@Example
	public void shouldReportAFailedActionRatherThanPretendItWorked() throws Exception {
		assertEquals(500, admitted().post("/action/break-ledger").statusCode());
	}

	@Example
	public void shouldLogWhyAnActionFailed() throws Exception {
		final Logger logger = (Logger) LoggerFactory.getLogger(ActionHandler.class);
		final ListAppender<ILoggingEvent> logged = new ListAppender<>();

		logged.start();
		logger.addAppender(logged);

		try {
			admitted().post("/action/break-ledger");
		} finally {
			logger.detachAppender(logged);
		}

		assertEquals(1, logged.list.size());
		assertEquals(Level.ERROR, logged.list.get(0).getLevel());
		assertTrue(logged.list.get(0).getFormattedMessage().contains("break-ledger"));
		assertEquals("The ledger file is damaged", logged.list.get(0).getThrowableProxy().getMessage());
	}

	@Example
	public void shouldReportAnActionThatAnswersWithSomethingOtherThanAView() throws Exception {
		assertEquals(500, admitted().post("/action/misreport-ledger").statusCode());
	}
}
