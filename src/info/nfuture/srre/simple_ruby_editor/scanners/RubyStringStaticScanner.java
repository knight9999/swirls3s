package info.nfuture.srre.simple_ruby_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;


public class RubyStringStaticScanner extends SubScanner {
	public RubyStringStaticScanner() {
		super();
		setDefaultReturnToken( getToken(SWT.COLOR_BLUE));
//		setDefaultReturnToken(new Token(new TextAttribute( Display.getCurrent().getSystemColor(SWT.COLOR_BLUE))));
//		IRule[] rules = new IRule[0];
//		setRules(rules);
	}
}
