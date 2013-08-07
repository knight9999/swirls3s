package info.nfuture.eclipse.lib.sub_rules;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class VariableRule implements IRule {
	protected IToken fToken;
	protected String invalidHead;
	protected String prev;

	
	public VariableRule(String invalidHead,String prev,IToken token) {
		fToken = token;
		this.invalidHead = invalidHead;
		this.prev = prev;
		
	}
	
	public IToken evaluate(ICharacterScanner scanner0) {
		SubScanner scanner = (SubScanner) scanner0;
		if (scanner.getFRangeStart() != scanner.getTokenOffset()) {
			scanner.unread();
			int c = scanner.read();
			for (int i=0;i<invalidHead.length();i++) {
				if (c == invalidHead.charAt(i)) {
					return Token.UNDEFINED;
				}
			}
		} 
		int c;
		for (int i=0;i<prev.length();i++) {
			c = scanner.read();
			if (c != prev.charAt(i)) {
				for (int j=0;j<=i;j++) {
					scanner.unread();
				}
				return Token.UNDEFINED;
			}
		}
		int count = 0;
		c = scanner.read();
		while ( ! isSymbolTerminal(c) ){
			c = scanner.read();
			count++;
		}
		if (count ==0) { // ƒ{ƒfƒB‚ª‚Ü‚Á‚½‚­‚È‚¢ê‡‚ÍŽ¸”s
			for (int i=0;i<prev.length();i++) {
				scanner.unread();
			}
			scanner.unread();
			return Token.UNDEFINED;
		}
		scanner.unread();
		return fToken;
	}

	public boolean isSymbolTerminal(int c) {
		if ( Character.isLetterOrDigit(c) ) {
			return false;
		}
		if ( c == '_') {
			return false;
		}
		return true; 
	}

}
