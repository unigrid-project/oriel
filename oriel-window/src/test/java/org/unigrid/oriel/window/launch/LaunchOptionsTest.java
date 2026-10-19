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

package org.unigrid.oriel.window.launch;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LaunchOptionsTest {
	@Test
	public void shouldDrawOnX11AndLeaveEverythingToTheApplicationWhenNothingIsSaid() {
		final LaunchOptions options = LaunchOptions.of("--ledger", "books.csv");

		assertEquals(WindowSystem.X11, options.windowSystem());
		assertEquals(List.of("--ledger", "books.csv"), options.remaining());
	}

	@Test
	public void shouldTakeTheWindowSystemWithOrWithoutAnEqualsSign() {
		assertEquals(WindowSystem.WAYLAND, LaunchOptions.of("--window-system=wayland").windowSystem());
		assertEquals(WindowSystem.WAYLAND, LaunchOptions.of("--window-system", "wayland").windowSystem());
	}

	@Test
	public void shouldTakeTheWindowSystemInAnyCase() {
		assertEquals(WindowSystem.AUTO, LaunchOptions.of("--window-system=Auto").windowSystem());
		assertEquals(WindowSystem.X11, LaunchOptions.of("--window-system", "X11").windowSystem());
	}

	@Test
	public void shouldTakeOutOnlyItsOwnAndKeepTheRestInOrder() {
		final LaunchOptions options = LaunchOptions.of("first.csv", "--window-system", "auto", "--verbose",
			"--window-system-extra=1", "second.csv");

		assertEquals(WindowSystem.AUTO, options.windowSystem());
		assertEquals(List.of("first.csv", "--verbose", "--window-system-extra=1", "second.csv"),
			options.remaining());
	}

	@Test
	public void shouldLeaveEverythingAfterTheEndOfTheOptionsToTheApplication() {
		final LaunchOptions options = LaunchOptions.of("--window-system=auto", "--", "--window-system=wayland");

		assertEquals(WindowSystem.AUTO, options.windowSystem());
		assertEquals(List.of("--", "--window-system=wayland"), options.remaining());
	}

	@Test
	public void shouldRefuseAWindowSystemItDoesNotKnow() {
		final IllegalArgumentException refused = assertThrows(IllegalArgumentException.class,
			() -> LaunchOptions.of("--window-system=mir"));

		assertTrue(refused.getMessage().contains("x11, wayland, auto"), refused.getMessage());
	}

	@Test
	public void shouldRefuseAWindowSystemWithoutAValue() {
		assertThrows(IllegalArgumentException.class, () -> LaunchOptions.of("--window-system"));
		assertThrows(IllegalArgumentException.class, () -> LaunchOptions.of("--window-system="));
	}

	@Test
	public void shouldRefuseTheWindowSystemTwice() {
		assertThrows(IllegalArgumentException.class,
			() -> LaunchOptions.of("--window-system=x11", "--window-system=auto"));
	}

	@Test
	public void shouldNotKeepTheApplicationsListLinkedToTheArgumentsGiven() {
		final String[] args = {"books.csv"};
		final LaunchOptions options = LaunchOptions.of(args);

		args[0] = "changed.csv";

		assertEquals(List.of("books.csv"), options.remaining());
		assertThrows(UnsupportedOperationException.class, () -> options.remaining().add("more.csv"));
	}

	@Test
	public void shouldOfferAHelpLineWhereTheWindowSystemCanBeChosen() {
		final String help = LaunchOptions.help(true).orElseThrow();

		assertTrue(help.startsWith(LaunchOptions.WINDOW_SYSTEM), help);
		assertTrue(help.contains("defaults to x11"), help);
	}

	@Test
	public void shouldOfferNoHelpLineWhereTheWindowSystemCannotBeChosen() {
		assertTrue(LaunchOptions.help(false).isEmpty());
	}
}
