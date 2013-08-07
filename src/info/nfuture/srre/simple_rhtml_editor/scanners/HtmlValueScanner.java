package info.nfuture.srre.simple_rhtml_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;


public class HtmlValueScanner extends SubScanner {
	public HtmlValueScanner() {
		super();
//		Color color = 
//			Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
//		IToken other = new Token(new TextAttribute(color));
		IToken other = getToken( SWT.COLOR_BLUE );
		setDefaultReturnToken(other);
	}
}
