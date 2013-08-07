package info.nfuture.srre.simple_ruby_editor;

import info.nfuture.eclipse.lib.DomainRule;
import info.nfuture.eclipse.lib.DomainScanner;
import info.nfuture.eclipse.lib.comparators.BraceEndComparator;
import info.nfuture.eclipse.lib.comparators.BraceStartComparator;
import info.nfuture.eclipse.lib.comparators.ExCharacterComparator;
import info.nfuture.eclipse.lib.comparators.HereDocumentEndComparator;
import info.nfuture.eclipse.lib.comparators.HereDocumentStartComparator;
import info.nfuture.eclipse.lib.comparators.ICheckCharacter;
import info.nfuture.eclipse.lib.comparators.LineStartCharacterComparator;
import info.nfuture.eclipse.lib.comparators.SimpleCharacterComparator;
import info.nfuture.eclipse.lib.comparators.SimpleCharacterComparatorWithException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;


public class SimpleRubyPartitionScanner extends DomainScanner {
	public final static String COMMENT = "__COMMENT";
	public final static String LINE_COMMENT = "__LINECOMMENT";
	public final static String RUBY_STRING = "__RUBY_STRING";
	public final static String RUBY_STRING_STATIC = "__RUBY_STRING_STATIC";
	public final static String RUBY_REG_EXP = "__RUBY_REG_EXP";
//	public final static String RUBY_REG_EXP2 = "__RUBY_REG_EXP2";
	public final static String INNER_RUBY = "__INNER_RUBY";
	public final static String HEREDOCUMENT = "__HEREDOCUMENT";
	public final static String HEREDOCUMENT_STATIC = "__HEREDOCUMENT_STATIC";

	public final static String RUBY_STRING_VAL = "__RUBY_STRING_VAL";
	public final static String RUBY_STRING_VAL0 = "__RUBY_STRING_VAL0";
	public final static String RUBY_STRING_STATIC_VAL = "__RUBY_STRING_STATIC_VAL";
	public final static String RUBY_REG_EXP_VAL = "__RUBY_REG_EXP_VAL";
	
	public static List<String> contentTypes() {
		List<String> list = new ArrayList<String>();
		list.add(COMMENT);
		list.add(LINE_COMMENT);
		list.add(RUBY_STRING);
		list.add(RUBY_STRING_STATIC);
		list.add(RUBY_REG_EXP);
		list.add(INNER_RUBY);
		list.add(HEREDOCUMENT);
		list.add(HEREDOCUMENT_STATIC);
		
		list.add(RUBY_STRING_VAL0);
		list.add(RUBY_STRING_VAL);
		list.add(RUBY_STRING_STATIC_VAL);
		list.add(RUBY_REG_EXP_VAL);
		
		return list;
	}
	
	public SimpleRubyPartitionScanner() {
		IToken comment            = new Token(COMMENT);
		IToken line_comment       = new Token(LINE_COMMENT);
		IToken ruby_string        = new Token(RUBY_STRING);
		IToken ruby_string_static = new Token(RUBY_STRING_STATIC);
		IToken ruby_reg_exp       = new Token(RUBY_REG_EXP);
		IToken inner_ruby         = new Token(INNER_RUBY);
		
		IToken hereDocument       = new Token(HEREDOCUMENT);
		IToken hereDocumentStatic = new Token(HEREDOCUMENT_STATIC);
		
		IToken rubyStringVal0      = new Token(RUBY_STRING_VAL0);
		IToken rubyStringVal       = new Token(RUBY_STRING_VAL);
		IToken rubyStringStaticVal = new Token(RUBY_STRING_STATIC_VAL);
		IToken rubyRegExpVal       = new Token(RUBY_REG_EXP_VAL);
		
		List<DomainRule> rules = new ArrayList<DomainRule>();
		
		DomainRule commentRule = new DomainRule(
				new LineStartCharacterComparator("=begin"),
				new LineStartCharacterComparator("=end"),
				comment,(char)0,false);
		DomainRule lineCommentRule = new DomainRule(
				new SimpleCharacterComparator("#"),
				null,
				line_comment,(char)0,true);
		DomainRule rubyStringRule = new DomainRule(
				new SimpleCharacterComparator("\""),
				new SimpleCharacterComparator("\""),
				ruby_string,'\\',false);

		DomainRule rubyStringStaticRule = new DomainRule(
				new SimpleCharacterComparatorWithException("'",'$'),
				new SimpleCharacterComparator("'"),
				ruby_string_static,'\\',false);
		
		DomainRule rubyRegExpRule = new DomainRule(
				new ExCharacterComparator("/",
					new ICheckCharacter() { 
						public boolean isTrueCharacter(int c) {
							if (Character.isLetterOrDigit(c) || c == '_' || c == '?' || c == '!' ) {
								return false;
							}
							return true;
						}
					}, 
					new ICheckCharacter() { 
						public boolean isTrueCharacter(int c) {
							if (Character.isSpaceChar(c) ) {
								return false;
							}
							return true;
						}
					}				
				),
				new ExCharacterComparator("/"),
				ruby_reg_exp,'\\',false);

		
		DomainRule innerRubyRule = new DomainRule(
				new SimpleCharacterComparator("#{"),
				new SimpleCharacterComparator("}"),
				inner_ruby,(char)0,false);
		
		DomainRule hereDocumentRule = new DomainRule(
				new HereDocumentStartComparator("<<",'"',false),
				new HereDocumentEndComparator(),
				hereDocument , (char)0 , false);
		hereDocumentRule.setFHereDocument(true);
		
		DomainRule hereDocumentStaticRule = new DomainRule(
				new HereDocumentStartComparator("<<",'\'',true),
				new HereDocumentEndComparator(),
				hereDocumentStatic , (char)0 , false);
		hereDocumentStaticRule.setFHereDocument(true);

		DomainRule rubyStringVal0Rule = new DomainRule(
				new BraceStartComparator("%"),
				new BraceEndComparator() ,
				rubyStringVal0,'\\',false);

		DomainRule rubyStringValRule = new DomainRule(
				new BraceStartComparator("%Q"),
				new BraceEndComparator() ,
				rubyStringVal,'\\',false);

		DomainRule rubyStringStaticValRule = new DomainRule(
				new BraceStartComparator("%q"),
				new BraceEndComparator() ,
				rubyStringStaticVal,'\\',false);

		DomainRule rubyRegExpValRule = new DomainRule(
				new BraceStartComparator("%r"),
				new BraceEndComparator() ,
				rubyRegExpVal,'\\',false);

		rubyStringRule.addSubRule( innerRubyRule);
		innerRubyRule.addSuperRule( rubyStringRule);
		
		rubyRegExpRule.addSubRule( innerRubyRule );
		innerRubyRule.addSuperRule( rubyRegExpRule );

		// Ruby•¶Žš—ñ‚Ì’†‚Ìruby‚ÅARuby•¶Žš—ñ‚ðŽg‚¤ê‡
		innerRubyRule.addSubRule( rubyStringRule );
		innerRubyRule.addSubRule( rubyStringStaticRule );
		
		hereDocumentRule.addSubRule( innerRubyRule);
		innerRubyRule.addSuperRule( hereDocumentRule );

		rubyRegExpValRule.addSubRule( innerRubyRule );
		innerRubyRule.addSuperRule( rubyRegExpValRule );

		
		rules.add(commentRule);
		rules.add(lineCommentRule);
		rules.add(rubyRegExpRule);
		rules.add(rubyStringRule);
		rules.add(rubyStringStaticRule);
		rules.add(innerRubyRule);
		rules.add(hereDocumentRule);
		rules.add(hereDocumentStaticRule);
		
		rules.add(rubyStringVal0Rule);
		rules.add(rubyStringValRule);
		rules.add(rubyStringStaticValRule);
		rules.add(rubyRegExpValRule);
		
		setDomainRules(rules);
	}
}
