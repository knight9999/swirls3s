package info.nfuture.eclipse.lib.sub_rules;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class TagColorRule implements IRule {
	protected IToken fToken;

	public TagColorRule(IToken token) {
		fToken = token;
	}
	
	public IToken evaluate(ICharacterScanner scanner0) {
		SubScanner scanner = (SubScanner) scanner0;
		boolean flag = false;
		if (scanner.getFRangeStart()+1==scanner.getTokenOffset()) {
			scanner.unread();
			int c = scanner.read();
			if (c == '<') {
				flag = true;
			}
		} else if (scanner.getFRangeStart() + 2 ==scanner.getTokenOffset()) {
			scanner.unread();
			scanner.unread();
			int b = scanner.read();
			int c = scanner.read();
			if (b == '<' && c == '/') {
				flag = true;
			}
		}
		if (flag) {
			int count = 0;
			int c = scanner.read();
			count++;
			while ( Character.isLetterOrDigit(c) ) {
				c = scanner.read();
				count++;
			}
			scanner.unread();
			count--;
			if (count>0) {
				return fToken;
			}
		}
		return Token.UNDEFINED;
		
	}

}
