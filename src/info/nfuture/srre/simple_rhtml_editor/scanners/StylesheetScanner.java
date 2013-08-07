package info.nfuture.srre.simple_rhtml_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;


public class StylesheetScanner extends SubScanner {
	public StylesheetScanner() {
		super();
		IToken other = getToken( SWT.COLOR_DARK_YELLOW );
		setDefaultReturnToken(other);
		
	}
}
