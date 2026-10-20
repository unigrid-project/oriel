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
   where its log is, so the page says it in data-failure-message on the body. */
document.addEventListener("htmx:responseError", () => {
	const host = document.querySelector("main > .card") ?? document.querySelector("main");
	const notice = host.querySelector("[data-failure]") ?? host.appendChild(document.createElement("p"));

	notice.className = "step__error";
	notice.dataset.failure = "";
	notice.setAttribute("role", "alert");
	notice.textContent = document.body.dataset.failureMessage ?? "That did not work. The reason is in the log.";
});
