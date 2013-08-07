package info.nfuture.eclipse.lib.sub_rules;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class CharacterRule implements IRule {
	protected IToken fToken;
	protected int ch;
	
	public CharacterRule(int c,IToken token) {
		fToken = token;
		ch = c;
	}
	
	public IToken evaluate(ICharacterScanner scanner0) {
		SubScanner scanner = (SubScanner) scanner0;
		int c = scanner.read();
		if (c == ch) {
			return fToken;
		}
		scanner.unread();
		return Token.UNDEFINED;
	}

}
