/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.hyphen;

import com.lowagie.text.pdf.BaseFont;

//FIXME parcit mai
public class DummyHyphenationManager implements IHyphenationManager
{

	public Hyphenation getHyphenation( String word )
	{
        return new Hyphenation(word, new int[]{0, word.length( )});
	}

	public Hyphenation getHyphenation(String word, BaseFont font,
			float fontSize, float remainingWidth, boolean forceHyphenation) {
		return null;
	}

	public Hyphenation getHyphenation(String word, BaseFont font,
			float fontSize, float remainingWidth) {
		return null;
	}

	public String getHyphenSymbol() {
		return "";
	}
}
