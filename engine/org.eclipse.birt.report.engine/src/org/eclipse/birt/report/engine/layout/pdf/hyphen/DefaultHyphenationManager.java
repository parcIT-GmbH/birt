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

import java.util.Locale;

import org.eclipse.birt.report.engine.layout.PDFConstants;

import com.ibm.icu.text.BreakIterator;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.HyphenationAuto;

/**
 * Benutzt die Klasse HyphenationAuto. FIXME parcit mai
 */
public class DefaultHyphenationManager implements IHyphenationManager {
	private HyphenationAuto ha;
	final private Locale loc;

	@SuppressWarnings("unused")
	public DefaultHyphenationManager(Locale locale) {
		if (locale == null) {
			loc = Locale.getDefault();
		} else {
			loc = locale;
		}
		ha = new HyphenationAuto(loc.getLanguage(), loc.getCountry(), 2, 2);
		// laedt die Datei
		//HyphenationTree resourceHyphenationTreeTest = getResourceHyphenationTree();
	}

	public DefaultHyphenationManager() {
		this(Locale.getDefault());
	}

	public Hyphenation getHyphenation(String word, BaseFont font,
			float fontSize, float remainingWidth) {
		return getHyphenation(word, font, fontSize, remainingWidth, true);
	}

	public Hyphenation getHyphenation(String word, BaseFont font,
			float fontSize, float remainingWidth, boolean forceHyphenation) {
		Hyphenation hyph = getITextHyphenation(word, font, fontSize,
				remainingWidth/PDFConstants.LAYOUT_TO_PDF_RATIO);

		// Test whether iText could hyphenate the word
		if (hyph.getHyphenationPoints()[1] == -1) {
			hyph = getITextWordHyphenation(word, font, fontSize, remainingWidth);
		}

		if ((hyph.getHyphenationPoints()[1] == -1) && forceHyphenation) {
			hyph = getHyphenation(word);
		}

		return hyph;
	}


	  public Hyphenation getITextHyphenation(String word, BaseFont font, float fontSize, float remainingWidth)
	  {
	    String pre = null;
	    try
	    {
	      pre = this.ha.getHyphenatedWordPre(word, font, fontSize,
	        remainingWidth);
	    }
	    catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
	    {
	      int[] indexes = new int[2];
	      indexes[0] = -1;
	      indexes[1] = -1;
	      return new Hyphenation(word, indexes);
	    }
	    int[] indexes = new int[2];
	    indexes[0] = 0;
	    indexes[1] = (pre.length() - 1);
	    return new Hyphenation(word, indexes);
	  }

	  public Hyphenation getITextWordHyphenation(String word, BaseFont font, float fontSize, float remainingWidth)
	  {
	    float hyphenSymbolWidth = font.getWidthPoint(this.ha.getHyphenSymbol(),
	      fontSize) * 1000.0F;

	    BreakIterator bi = BreakIterator.getWordInstance(this.loc);
	    bi.setText(word);
	    int end = bi.last();
	    int start = bi.previous();

	    String pre = "";
	    while (start != -1)
	    {
	      String part = new String(word.substring(start, end));

	      float width = font.getWidthPoint(
	        word.substring(0, word.indexOf(part)), fontSize) * PDFConstants.LAYOUT_TO_PDF_RATIO;
	      try
	      {
	        pre = this.ha.getHyphenatedWordPre(part, font, fontSize * PDFConstants.LAYOUT_TO_PDF_RATIO, remainingWidth -
	          width - hyphenSymbolWidth);
	      }
	      catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
	      {
	        int[] indexes = new int[2];
	        indexes[0] = -1;
	        indexes[1] = -1;
	        return new Hyphenation(word, indexes);
	      }
	      if (pre.length() != 0) {
	        break;
	      }
	      end = bi.current();
	      start = bi.previous();
	    }
	    int[] indexes = new int[2];
	    indexes[0] = 0;
	    if (!pre.equals("")) {
	      indexes[1] =
	        (word.indexOf(pre.substring(0, pre.length() - 1)) + pre.length() - 1);
	    } else {
	      indexes[1] = -1;
	    }
	    return new Hyphenation(word, indexes);
	  }

	public Hyphenation getHyphenation(String word) {
		int length = word.length();
		int[] indexes = new int[length + 1];
		for (int i = 0; i <= length; i++) {
			indexes[i] = i;
		}
		return new Hyphenation(word, indexes);
	}

	public String getHyphenSymbol() {
		return ha.getHyphenSymbol();
	}
}
