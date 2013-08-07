package info.nfuture.eclipse.lib;

import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class SubScanner extends RuleBasedScanner {
	protected int fRangeStart;

	@Deprecated
	public void setRules(IRule[] rules) {
		throw new UnsupportedOperationException();
	}
	
	public void setRules(List<IRule> rules) {
		super.setRules( rules.toArray( new IRule[0]) );
	}
	
	@Override
	public void setRange(IDocument document, int offset, int length) {
		fRangeStart = offset;
		super.setRange(document, offset, length);
	}

	public int getFRangeStart() {
		return fRangeStart;
	}

	public int getFRangeEnd() {
		return fRangeEnd;
	}
	
	public static Color getSystemColor(Integer c) {
		if (c != null) {
			return Display.getCurrent().getSystemColor(c);
		}
		return null;
	}
	
	public static IToken getToken(Integer color,Integer bgColor,int style) {
		return new Token( 
				new TextAttribute( getSystemColor(color), getSystemColor(bgColor),style)
			);
	}
	
	public static IToken getToken(int color) {
		return new Token(
				new TextAttribute( getSystemColor(color))
				);
	}
}
