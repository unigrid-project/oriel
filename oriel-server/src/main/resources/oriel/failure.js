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

/* A failed action leaves the page as it was, which makes a click that failed look like one that was ignored.
   The reason is written to the log, so the person is only told that there is one. Only the application knows
   where its log is, so the page says it in data-failure-message on the body. The page marks where the notice
   goes with data-failure-host, and styles it through [data-failure]. Of several marks the last one wins, so a
   mark inside another, such as on a card inside the main area, is the one used; with none the body is used. */
document.addEventListener("htmx:responseError", () => {
	const host = [...document.querySelectorAll("[data-failure-host]")].at(-1) ?? document.body;
	const notice = host.querySelector("[data-failure]") ?? host.appendChild(document.createElement("p"));

	notice.dataset.failure = "";
	notice.setAttribute("role", "alert");
	notice.textContent = document.body.dataset.failureMessage ?? "That did not work. The reason is in the log.";
});
