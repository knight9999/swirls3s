package info.nfuture.srre.simple_rhtml_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;
import info.nfuture.eclipse.lib.sub_rules.NumberRule;
import info.nfuture.eclipse.lib.sub_rules.StartAndEndRule;
import info.nfuture.eclipse.lib.sub_rules.VariableRule;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


public class RubyScanner extends SubScanner {
	public RubyScanner() {
		super();

		setDefaultReturnToken(getToken( SWT.COLOR_BLACK ));

		// ‘OŒã‚Ì<% %>—p
		IToken startAndEnd = getToken( SWT.COLOR_DARK_MAGENTA );

		
		List<IRule> rules = new ArrayList<IRule>();
		rules.add( new StartAndEndRule("<%","%>",startAndEnd) );
		rules.addAll( info.nfuture.srre.simple_ruby_editor.scanners.RubyScanner.getBasicRules() );	
		setRules(rules);
	}

}
