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
package io.sarl.lang.util;

import io.sarl.lang.sarl.FormalParameter;

import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * Comparator of lists of formal parameters.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FormalParameterListComparator implements Comparator<EList<FormalParameter>> {

	/** Construct a comparator of formal parameter lists.
	 */
	public FormalParameterListComparator() {
		//
	}

	@Override
	public int compare(EList<FormalParameter> a, EList<FormalParameter> b) {
		if (a == b) {
			return 0;
		}
		if (a == null) {
			return Integer.MIN_VALUE;
		}
		if (b == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = Integer.compare(a.size(), b.size());
		if (cmp == 0) {
			Iterator<FormalParameter> i1 = a.iterator();
			Iterator<FormalParameter> i2 = b.iterator();
			while (cmp == 0 && i1.hasNext() && i2.hasNext()) {
				cmp = compare(i1.next(), i2.next());
			}
		}
		return cmp;
	}

	/** Compare the two formal parameters.
	 *
	 * @param p1 - the first parameter to compare.
	 * @param p2 - the second parameter to compare.
	 * @return A negative value if <code>p1</code> is
	 * lower than <code>p2</code>, a positive value if
	 * <code>p1</code> is greater than <code>p2</code>,
	 * otherwise <code>0</code>.
	 */
	public static int compare(FormalParameter p1, FormalParameter p2) {
		if (p1 != p2) {
			if (p1 == null) {
				return Integer.MIN_VALUE;
			}
			if (p2 == null) {
				return Integer.MAX_VALUE;
			}
			JvmTypeReference t1 = p1.getParameterType();
			JvmTypeReference t2 = p2.getParameterType();
			if (t1 != t2) {
				int cmp;
				if (t1 == null) {
					cmp = Integer.MIN_VALUE;
				} else if (t2 == null) {
					cmp = Integer.MAX_VALUE;
				} else {
					cmp = t1.getIdentifier().compareTo(t2.getIdentifier());
				}
				if (cmp != 0) {
					return cmp;
				}
			}
		}
		return 0;
	}

}
