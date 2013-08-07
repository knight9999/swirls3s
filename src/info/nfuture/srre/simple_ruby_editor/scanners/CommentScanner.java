package info.nfuture.srre.simple_ruby_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;


public class CommentScanner extends SubScanner {

	public CommentScanner() {
		super();
		IToken defaultToken = getToken( SWT.COLOR_DARK_GREEN );
		setDefaultReturnToken( defaultToken );
	}
}
