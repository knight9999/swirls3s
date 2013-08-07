package info.nfuture.srre.simple_ruby_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;

public class HereDocumentStaticScanner extends SubScanner {
	public HereDocumentStaticScanner() {
		super();
//		setDefaultReturnToken(new Token(new TextAttribute( Display.getCurrent().getSystemColor(SWT.COLOR_BLUE))) );
//		IRule[] rules = new IRule[0];
//		setRules(rules);
		IToken other = getToken(SWT.COLOR_BLUE);
		setDefaultReturnToken(other);
	}

}
