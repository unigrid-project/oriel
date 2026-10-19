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

import java.util.Set;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.unigrid.oriel.server.action.View;

public class Templates {
	private final TemplateEngine engine = new TemplateEngine();

	public Templates(final boolean cache) {
		final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();

		resolver.setPrefix(Constants.TEMPLATE_ROOT);
		resolver.setSuffix(Constants.TEMPLATE_SUFFIX);
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCharacterEncoding("UTF-8");

		/* Development switches caching off so an edited template appears on the next
		   request, which is the entire point of building the interface in a browser. */
		resolver.setCacheable(cache);
		engine.setTemplateResolver(resolver);
	}

	/**
	 * Renders what the view names, the way Thymeleaf names it: {@code page} or {@code page :: block}.
	 * The view itself is what the template reads as {@code view}.
	 */
	public String render(final View view) {
		final String specification = view.template();
		final int separator = specification.indexOf(Constants.FRAGMENT_SELECTOR);
		final Context context = new Context();

		context.setVariable(Constants.VIEW_VARIABLE, view);

		if (separator < 0) {
			return engine.process(specification.trim(), context);
		}

		return engine.process(specification.substring(0, separator).trim(),
			Set.of(specification.substring(separator + Constants.FRAGMENT_SELECTOR.length()).trim()), context
		);
	}
}
