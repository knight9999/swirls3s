package info.nfuture.eclipse.lib.sub_rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class KeywordRule implements IRule {
	protected IToken fToken;

	public KeywordRule(IToken token) {
		fToken = token;
	}
	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		int count = 0;
		while ( Character.isLetterOrDigit(c) || c == '_') {
			c = scanner.read();
			count++;
		}
		scanner.unread();
		if (count>0) {
			return fToken;
		}
		return Token.UNDEFINED;
	}

}
