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

package io.sarl.lang.controlflow;

import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.xbase.XAbstractFeatureCall;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XbasePackage;
import org.eclipse.xtext.xbase.typesystem.util.ExtendedEarlyExitComputer;

import com.google.inject.Singleton;

/** Compute the early-exit flag for the SARL statements.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Singleton
public class SARLExtendedEarlyExitComputer extends ExtendedEarlyExitComputer {

	/** {@inheritDoc}
	 */
	@Override
	public boolean isDefiniteEarlyExit(XExpression expression) {
		if (super.isDefiniteEarlyExit(expression)) {
			return true;
		}
		return isEarlyExitSARLStatement(expression);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isIntentionalEarlyExit(XExpression expression) {
		if (super.isIntentionalEarlyExit(expression)) {
			return true;
		}
		return isEarlyExitSARLStatement(expression);
	}

	/** Replies if the given expression is a early-exit SARL statement.
	 *
	 * @param expression - the expression to test.
	 * @return <code>true</code> if the given expression is a SARL early-exit
	 * statement, <code>false</code> otherwise.
	 * @see SARLEarlyExitComputerUtil#isEarlyExitAnnotatedElement(Object)
	 */
	@SuppressWarnings("static-method")
	protected boolean isEarlyExitSARLStatement(XExpression expression) {
		if (expression instanceof XAbstractFeatureCall) {
			// Do not call expression.getFeature() since the feature may be unresolved.
			// The type resolution at this point causes exceptions in the reentrant type resolver.
			// The second parameter (false) forces to ignore feature resolution.
			Object element = expression.eGet(
					XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE,
					false);
			return SARLEarlyExitComputerUtil.isEarlyExitAnnotatedElement(element);
		}
		return false;
	}

	/** Replies if the given type reference is a early-exit event.
	 *
	 * @param expression - the expression to test.
	 * @return <code>true</code> if the given expression is a SARL early-exit
	 * statement, <code>false</code> otherwise.
	 * @see SARLEarlyExitComputerUtil#isEarlyExitEvent(JvmParameterizedTypeReference)
	 */
	@SuppressWarnings("static-method")
	public boolean isEarlyExitEvent(JvmParameterizedTypeReference expression) {
		return SARLEarlyExitComputerUtil.isEarlyExitEvent(expression);
	}

}

