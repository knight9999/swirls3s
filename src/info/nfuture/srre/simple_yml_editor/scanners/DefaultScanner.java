package info.nfuture.srre.simple_yml_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;
import info.nfuture.eclipse.lib.sub_rules.CharacterRule;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;


public class DefaultScanner extends SubScanner {

	public DefaultScanner() {
		super();
		IToken other = getToken( SWT.COLOR_DARK_CYAN );
		setDefaultReturnToken(other);
		
		List<IRule> rules = new ArrayList<IRule>();
		
		IToken colon = getToken( SWT.COLOR_BLACK );
		rules.add( new CharacterRule(':',colon));
		setRules(rules);
	}

}
