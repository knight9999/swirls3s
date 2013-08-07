package info.nfuture.srre.simple_rhtml_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;


public class DefaultScanner extends SubScanner {
	public DefaultScanner() {
		super();
		IToken other = getToken( SWT.COLOR_BLACK );
		setDefaultReturnToken(other);
	}
}
