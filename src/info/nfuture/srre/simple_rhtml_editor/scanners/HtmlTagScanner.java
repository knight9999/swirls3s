package info.nfuture.srre.simple_rhtml_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;
import info.nfuture.eclipse.lib.sub_rules.KeywordRule;
import info.nfuture.eclipse.lib.sub_rules.TagColorRule;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


public class HtmlTagScanner extends SubScanner {
	public HtmlTagScanner() {
		super();
		IToken other = getToken( SWT.COLOR_BLUE); 
		IToken tag = getToken( SWT.COLOR_DARK_RED); 
		IToken keyword = getToken( SWT.COLOR_RED); 
		
		setDefaultReturnToken(other);

		List<IRule> rules = new ArrayList<IRule>();
		rules.add( new TagColorRule(tag) );
		rules.add( new KeywordRule(keyword) );
		setRules(rules);
	}
}
