package info.nfuture.srre.simple_yml_editor;

import info.nfuture.eclipse.lib.DomainRule;
import info.nfuture.eclipse.lib.DomainScanner;
import info.nfuture.eclipse.lib.comparators.LineStartCharacterComparator;
import info.nfuture.eclipse.lib.comparators.LineStartComparator;
import info.nfuture.eclipse.lib.comparators.SimpleCharacterComparator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;


public class SimpleYMLPartitionScanner extends DomainScanner {
	public final static String SECTION_KEY = "___SECTION_KEY";
	public final static String TITLE_KEY = "__TITLE_KEY";
//	public final static String NORMAL_KEY = "__NORMAL_KEY";
	public final static String VALUE_KEY = "__VALUE_KEY";
	public final static String COMMENT = "__COMMENT";
	
	public SimpleYMLPartitionScanner() {
		IToken section = new Token(SECTION_KEY);
		IToken title = new Token(TITLE_KEY);
//		IToken normal = new Token(NORMAL_KEY);
		IToken value = new Token(VALUE_KEY);
		IToken comment = new Token(COMMENT);

		List<DomainRule> rules = new ArrayList<DomainRule>();
		
		DomainRule commentRule = new DomainRule(
				new SimpleCharacterComparator("#"),
				null,
				comment,(char)0,true);

		DomainRule sectionRule = new DomainRule(
				new LineStartCharacterComparator("---"),
				null,
				section,(char)0,true);
		
		DomainRule titleRule = new DomainRule(
				new LineStartComparator(),
				new SimpleCharacterComparator(":" , true),
				title,(char)0,false);
		
		DomainRule valueRule = new DomainRule(
				new SimpleCharacterComparator(": "),
				null,
				value,(char)0,true);

		rules.add(commentRule);
		rules.add(sectionRule);
		rules.add(titleRule);
		rules.add(valueRule);
		
		setDomainRules(rules);
	}
}
