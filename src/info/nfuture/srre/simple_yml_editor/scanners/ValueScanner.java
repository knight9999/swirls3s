package info.nfuture.srre.simple_yml_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;


public class ValueScanner extends SubScanner {
	public ValueScanner() {
		super();
		IToken other = getToken( SWT.COLOR_BLACK );
		setDefaultReturnToken(other);
	}
}
