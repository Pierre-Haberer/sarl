/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2015 Sebastian RODRIGUEZ, Nicolas GAUD, Stéphane GALLAND.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.lang.ui.tests.highlighting;

import io.sarl.lang.ui.highlighting.SARLHighlightingCalculator;

import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.junit.Test;

import com.google.inject.Inject;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SARLHighlightingCalculatorTest extends AbstractSARLHighlightingCalculatorTest {

	@Inject
	private SARLHighlightingCalculator calculator;

	/** {@inheritDoc}
	 */
	@Override
	protected ISemanticHighlightingCalculator getCalculator() {
		return this.calculator;
	}

	/**
	 */
	@Test
	public void testOccurrence_0() {
		highlight(
				"agent A1 { on io.sarl.lang.core.Event { println(occurrence) } }") //$NON-NLS-1$
			.keyword(48,10);
	}

	/**
	 */
	@Test
	public void testOccurrence_1() {
		highlight(
				"agent A1 { on io.sarl.lang.core.Event [occurrence.fromMe] { } }") //$NON-NLS-1$
			.keyword(39,10);
	}

}
