package info.nfuture.srre.simple_ruby_editor.scanners;

import info.nfuture.eclipse.lib.SubScanner;
import info.nfuture.eclipse.lib.sub_rules.NumberRule;
import info.nfuture.eclipse.lib.sub_rules.VariableRule;
import info.nfuture.eclipse.lib.sub_rules.WordRuleEx;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.WhitespaceRule;


public class RubyScanner extends SubScanner {

	public static String[] RUBY_KEYWORDS = new String[] {
		"BEGIN" , "END" , "alias" , "and" , "begin" , "break" , "case" ,
		"class" , "def" , "defined?" , "do" , "else" , "elsif" , "end" ,
		"ensure" , "false" , "for" , "if" , "in" , "module" , "next" ,
		"nil" , "not" , "or" , "redo" , "rescue" , "retry" , "return" ,
		"self" , "super" , "then" , "true" , "undef" , "unless" , "until" ,
		"when" , "while" , "yield"
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
		// �L�[���[�h
		String[] keywords = RUBY_KEYWORDS;
		// �����I�ɂ́A$,@,@@�̌��Adef�̂��ƁA.�̌�̏ꍇ�́A�����L�[���[�h�ł����Ă�
		// �F���t���Ȃ��悤�ɕύX����K�v����B

		// �f�t�H���g�g�[�N��
		IToken other = getToken( SWT.COLOR_BLACK );
		// �L�[���[�h���܂ރg�[�N��
		IToken keyword = getToken( SWT.COLOR_DARK_MAGENTA, null, SWT.BOLD );
		// �V���{���̃g�[�N��
		IToken symbol = getToken( SWT.COLOR_RED, null, SWT.BOLD );
		// �C���X�^���X�ϐ��̃g�[�N��
		IToken instanceVariable = getToken( SWT.COLOR_DARK_BLUE, null, SWT.BOLD );
		// �N���X�ϐ��̃g�[�N��
		IToken classVariable = getToken( SWT.COLOR_DARK_CYAN, null, SWT.BOLD );
		// �O���[�o���ϐ��̃g�[�N��
		IToken globalVariable = getToken( SWT.COLOR_DARK_YELLOW );
		// ���̃g�[�N��
		IToken number = getToken( SWT.COLOR_BLUE );

		WordRuleEx wordRule = new WordRuleEx(new MyWordDetector(), other ,
			new char [] { '.' , '_' , ':' } , new char [] { '_' });
		for (int i=0;i<keywords.length;i++) 
			wordRule.addWord(keywords[i],keyword); // ���[�h�̒ǉ�

		List<IRule> rules = new ArrayList<IRule>();
		rules.add( wordRule );
		rules.add( new VariableRule(":",":",symbol) ); 
		rules.add( new VariableRule("@","@",instanceVariable) );
		rules.add( new VariableRule("@","@@",classVariable) );
		rules.add( new VariableRule("$","$",globalVariable) );
		rules.add( new NumberRule(number) );
		rules.add( new WhitespaceRule(
				new IWhitespaceDetector() {
					public boolean isWhitespace(char c) {
						return (c == ' ' || c == '\t' || c == '\n' || c=='\r');
					}
				}));
		return rules;
	}

	public RubyScanner() {
		super();
		
		setDefaultReturnToken(getToken( SWT.COLOR_BLACK ));

		List<IRule> rules = getBasicRules();
		setRules(rules);
	}
	
}
