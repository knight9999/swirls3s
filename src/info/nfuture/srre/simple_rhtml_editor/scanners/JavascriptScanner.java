package info.nfuture.srre.simple_rhtml_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;
import info.nfuture.eclipse.lib.sub_rules.WordRuleEx;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;


public class JavascriptScanner extends SubScanner {

	public static String[] JAVASCRIPT_KEYWORDS = new String[] {
		"abstract" , "boolean" , "break" , "byte" , "case" , "catch" , "char" ,
		"class" , "const" , "continue" , "default" , "do" , "double" , "else" ,
		"extends" , "false" , "final" , "finally" , "float" , "for" , "function" ,
		"goto" , "if" , "implements" , "import" , "in" , "instanceof" , "int" ,
		"interface" , "long" , "native" , "new" , "null" , "package" , "private" ,
		"protected" , "prototype" , "public" , "return" , "short" , "static" ,
		"super" , "switch" , "synchronized" , "this" , "throw" , "throws" , 
		"transient" , "true" , "try" , "var" , "void" , "while" , "with"
	};

	static class MyWordDetector implements IWordDetector {

		public boolean isWordPart(char c) {
			return Character.isLetter(c);
		}

		public boolean isWordStart(char c) {
			return Character.isLetter(c);
		}
		
	}
	
	public static List<IRule> getBasicRules() {
		// キーワード
		String[] keywords = JAVASCRIPT_KEYWORDS;
		IToken other = getToken( SWT.COLOR_DARK_GREEN );
		IToken keyword = getToken( SWT.COLOR_DARK_GREEN, null, SWT.BOLD );
		WordRuleEx wordRule = new WordRuleEx(new MyWordDetector(), other ,
			new char [] { '.' , '_' , ':' } , new char [] { '_' });
		for (int i=0;i<keywords.length;i++) 
			wordRule.addWord(keywords[i],keyword); // ワードの追加

		List<IRule> rules = new ArrayList<IRule>();
		rules.add( wordRule );
		rules.add( new WhitespaceRule(
				new IWhitespaceDetector() {
					public boolean isWhitespace(char c) {
						return (c == ' ' || c == '\t' || c == '\n' || c=='\r');
					}
				}));
		return rules;
	}
	
	public JavascriptScanner() {
		super();
		IToken other = getToken( SWT.COLOR_DARK_GREEN );
		setDefaultReturnToken(other);
		
		List<IRule> rules = getBasicRules();
		setRules(rules);
	}

}
