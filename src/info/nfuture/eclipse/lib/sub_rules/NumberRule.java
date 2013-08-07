package info.nfuture.eclipse.lib.sub_rules;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class NumberRule implements IRule {
	protected IToken fToken;

	public NumberRule(IToken token) {
		fToken = token;
	}
	
	public IToken evaluate(ICharacterScanner scanner0) {
		SubScanner scanner = (SubScanner) scanner0;
		int c;

		scanner.unread();
		if (scanner.getFRangeStart() == scanner.getTokenOffset()) {
			scanner.read(); // 元の位置に戻すためのダミー
		} else {
			c = scanner.read();
		
			if (Character.isLetterOrDigit(c) || c == '_') {
				// 1つ前が文字やアンダーバーなら、数字ではない。
				return Token.UNDEFINED;
			}
		}

		
		int count =0;
		c = scanner.read();
		if ( ! Character.isDigit(c) ) {
			// １文字目が数字でなければ、数字ではない。
			scanner.unread(); 
			return Token.UNDEFINED;
		}
		while ( Character.isLetterOrDigit(c) ) {
			c = scanner.read();
			count++;
		}
		scanner.unread();
		return fToken;
	}

}
