package info.nfuture.srre.simple_rhtml_editor;

import info.nfuture.eclipse.lib.DomainRule;
import info.nfuture.eclipse.lib.DomainScanner;
import info.nfuture.eclipse.lib.comparators.HereDocumentEndComparator;
import info.nfuture.eclipse.lib.comparators.HereDocumentStartComparator;
import info.nfuture.eclipse.lib.comparators.SimpleCharacterComparator;
import info.nfuture.eclipse.lib.comparators.TagCharacterComparator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;


public class SimpleRHTMLPartitionScanner extends DomainScanner {
	public final static String JAVASCRIPT = "__JAVASCRIPT";
	public final static String STYLESHEET = "__STYLESHEET";
	public final static String RUBY = "__RUBY";
	public final static String RUBY_STRING = "__RUBY_STRING";
	public final static String RUBY_STRING_STATIC = "__RUBY_STRING_STATIC";
	public final static String HTML_COMMENT = "__HTML_COMMENT";
	public final static String HTML_TAG = "__HTML_TAG";
	public final static String HTML_VALUE = "__HTML_VALUE";

	public final static String HEREDOCUMENT = "__HEREDOCUMENT";
	public final static String HEREDOCUMENT_STATIC = "__HEREDOCUMENT_STATIC";

	public SimpleRHTMLPartitionScanner() {
		IToken javascript = new Token(JAVASCRIPT);
		IToken stylesheet = new Token(STYLESHEET);
		IToken ruby       = new Token(RUBY);
		IToken ruby_string = new Token(RUBY_STRING);
		IToken ruby_string_static = new Token(RUBY_STRING_STATIC);
		IToken htmlComment = new Token(HTML_COMMENT);
		IToken htmlTag    = new Token(HTML_TAG);
		IToken htmlValue  = new Token(HTML_VALUE);

		IToken hereDocument = new Token(HEREDOCUMENT);
		IToken hereDocumentStatic = new Token(HEREDOCUMENT_STATIC);

		List<DomainRule> rules = new ArrayList<DomainRule>();
		
		DomainRule javascriptRule = new DomainRule(
				new TagCharacterComparator("script"),
				new TagCharacterComparator("/script"),
				javascript,(char)0,false);
	
		DomainRule stylesheetRule = new DomainRule(
				new TagCharacterComparator("style"),
				new TagCharacterComparator("/style"),
				stylesheet,(char)0,false);
	
		DomainRule rubyRule = new DomainRule(
				new SimpleCharacterComparator("<%"),
				new SimpleCharacterComparator("%>"),
				ruby,(char)0,false);
		
		DomainRule rubyStringRule = new DomainRule(
				new SimpleCharacterComparator("\""),
				new SimpleCharacterComparator("\""),
				ruby_string,'\\',false);
		rubyRule.addSubRule( rubyStringRule);
		rubyStringRule.addSuperRule( rubyRule );
		
		DomainRule rubyStringStaticRule = new DomainRule(
				new SimpleCharacterComparator("'"),
				new SimpleCharacterComparator("'"),
				ruby_string_static,'\\',false);
		rubyRule.addSubRule( rubyStringStaticRule );
		rubyStringStaticRule.addSuperRule( rubyRule );
		
		DomainRule htmlCommentRule = new DomainRule(
				new SimpleCharacterComparator("<!--"),
				new SimpleCharacterComparator("-->"),
				htmlComment,(char)0,false);
		htmlCommentRule.addSubRule(rubyRule );
		
		DomainRule htmlTagRule = new DomainRule(
				new SimpleCharacterComparator("<"),
				new SimpleCharacterComparator(">"),
				htmlTag,(char)0,false);
		htmlTagRule.addSubRule( rubyRule );
	
		DomainRule htmlValueRule = new DomainRule(
				new SimpleCharacterComparator("\""),
				new SimpleCharacterComparator("\""),
				htmlValue,(char)0,false);
		htmlValueRule.addSuperRule( htmlTagRule);
		htmlTagRule.addSubRule(htmlValueRule);
		htmlValueRule.addSubRule( rubyRule );
		
		
		javascriptRule.addSubRule( rubyRule );
		stylesheetRule.addSubRule( rubyRule );
		

		DomainRule hereDocumentRule = new DomainRule(
				new HereDocumentStartComparator("<<",'"',false),
				new HereDocumentEndComparator(),
				hereDocument , (char)0 , false);
		hereDocumentRule.setFHereDocument(true);
		
		rubyRule.addSubRule( hereDocumentRule );
		hereDocumentRule.addSuperRule( rubyRule );

		DomainRule hereDocumentStaticRule = new DomainRule(
				new HereDocumentStartComparator("<<",'\'',true),
				new HereDocumentEndComparator(),
				hereDocumentStatic , (char)0 , false);
		hereDocumentStaticRule.setFHereDocument(true);
		
		rubyRule.addSubRule( hereDocumentStaticRule );
		hereDocumentStaticRule.addSuperRule( rubyRule );
		
		
		rules.add(javascriptRule);
		rules.add(stylesheetRule);
		rules.add(rubyRule);
		rules.add(rubyStringRule);
		rules.add(rubyStringStaticRule);
		rules.add(htmlCommentRule);
		rules.add(htmlTagRule);
		rules.add(htmlValueRule);
		rules.add(hereDocumentRule);
		rules.add(hereDocumentStaticRule);
		
		setDomainRules(rules);
	}
}
