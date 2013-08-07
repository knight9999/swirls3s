package info.nfuture.srre.simple_yml_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;


public class CommentScanner extends SubScanner {
	public CommentScanner() {
		super();
		IToken other = getToken( SWT.COLOR_DARK_MAGENTA );
		setDefaultReturnToken(other);
		
	}
}
