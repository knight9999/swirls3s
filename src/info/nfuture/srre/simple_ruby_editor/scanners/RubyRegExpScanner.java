package info.nfuture.srre.simple_ruby_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.swt.SWT;


public class RubyRegExpScanner extends SubScanner {
	public RubyRegExpScanner() {
		super();
		setDefaultReturnToken( getToken( SWT.COLOR_DARK_MAGENTA  ));
//		setDefaultReturnToken(new Token(new TextAttribute( Display.getCurrent().getSystemColor(SWT.COLOR_BLUE))) );
//		IRule[] rules = new IRule[0];
//		setRules(rules);
	}
}
