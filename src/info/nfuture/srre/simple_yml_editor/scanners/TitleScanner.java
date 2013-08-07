package info.nfuture.srre.simple_yml_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


public class TitleScanner extends SubScanner {
	public TitleScanner() {
		super();
		IToken other = 
			
			new Token( 
					new TextAttribute( new Color(Display.getCurrent(), 255,128, 0), null , SWT.BOLD)
				);
//			getToken( SWT.COLOR_GREEN, null, SWT.BOLD);
		setDefaultReturnToken(other);
	}

}
