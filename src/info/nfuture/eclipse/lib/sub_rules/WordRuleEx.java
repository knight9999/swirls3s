package info.nfuture.eclipse.lib.sub_rules;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

public class WordRuleEx extends WordRule {
	protected char [] ignorePrevChars;
	protected char [] ignoreNextChars;
	
	public WordRuleEx(IWordDetector detector, IToken defaultToken,
			boolean ignoreCase,char [] ignorePrevChars,char [] ignoreNextChars) {
		super(detector, defaultToken, ignoreCase);
		this.ignorePrevChars = ignorePrevChars;
		this.ignoreNextChars = ignoreNextChars;
	}

	public WordRuleEx(IWordDetector detector, IToken defaultToken, char [] ignorePrevChars,char [] ignoreNextChars) {
		super(detector, defaultToken);
		this.ignorePrevChars = ignorePrevChars;
		this.ignoreNextChars = ignoreNextChars;
	}

	public WordRuleEx(IWordDetector detector, char [] ignorePrevChars,char [] ignoreNextChars) {
		super(detector);
		this.ignorePrevChars = ignorePrevChars;
		this.ignoreNextChars = ignoreNextChars;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner0) {
		SubScanner scanner = (SubScanner) scanner0;
		int c;
		scanner.unread();
		if (scanner.getFRangeStart() == scanner.getTokenOffset()) {
			scanner.read(); // 元の位置に戻すためのダミー
			IToken result = super.evaluate(scanner);
			if (result != Token.UNDEFINED) {
				int v = scanner.read();
				boolean x = checkIncluded(ignoreNextChars,v);
				if (!x) {
					scanner.unread();
					return result;
				}
				scanner.unread();
				unreadBuffer(scanner);
			}
		} else {
			c = scanner.read();
			if (! checkIncluded(ignorePrevChars,c) && ! Character.isLetterOrDigit(c)) {
				IToken result = super.evaluate(scanner);
				if (result != Token.UNDEFINED) {
					int v = scanner.read();
					boolean x = checkIncluded(ignoreNextChars,v);
					if (!x) {
						scanner.unread();
						return result;
					}
					scanner.unread();
					unreadBuffer(scanner);
				}
			}
		}
		return Token.UNDEFINED;
	}
	
	public boolean checkIncluded(char [] chars,int c) {
		for (int i=0;i<chars.length;i++) {
			if (chars[i]==c) {
				return true;
			}
		}
		return false;
	}

}
