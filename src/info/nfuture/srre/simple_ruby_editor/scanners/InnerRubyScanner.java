package info.nfuture.srre.simple_ruby_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;
import info.nfuture.eclipse.lib.sub_rules.StartAndEndRule;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;


public class InnerRubyScanner extends SubScanner {
	public InnerRubyScanner() {
		super();
		setDefaultReturnToken( getToken(SWT.COLOR_BLACK) );

		IToken startAndEnd = getToken(SWT.COLOR_DARK_GRAY);
	
		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new StartAndEndRule("#{","}",startAndEnd) );
		rules.addAll( info.nfuture.srre.simple_ruby_editor.scanners.RubyScanner.getBasicRules() );	
		setRules(rules);
		
//		IRule[] rules = new IRule[1];
//		rules[0] = new StartAndEndRule("#{","}",startAndEnd);
//		setRules(rules);
	}
}
