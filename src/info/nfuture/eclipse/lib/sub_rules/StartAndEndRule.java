package info.nfuture.eclipse.lib.sub_rules;

import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class StartAndEndRule implements IRule {
	protected IToken fToken;
	protected String start;
	protected String end;
	
	public StartAndEndRule(String start,String end,IToken token) {
		fToken = token;
		this.start = start;
		this.end = end;
	}
	
	public IToken evaluate(ICharacterScanner scanner0) {
		SubScanner scanner = (SubScanner) scanner0;
		if (scanner.getFRangeStart()==scanner.getTokenOffset()) {
			IToken token = evaluateStart(scanner);
			if (token != null) {
				return fToken;
			}
		} 
		if (scanner.getFRangeEnd()==scanner.getTokenOffset()+end.length()) {
			IToken token = evaluateEnd(scanner);
			if (token != null) {
				return fToken;
			}
		}
		
		return Token.UNDEFINED;
	}
	
	public IToken evaluateStart(SubScanner scanner) {
		int count = 0;
		for (int i=0;i<start.length();i++) {
			int c = scanner.read();
			count ++;
			if (c!=start.charAt(i)) {
				for (int j=0;j<count;j++) {
					scanner.unread();
				}
				return null;
			}
		}
		return fToken;
	}
	
	public IToken evaluateEnd(SubScanner scanner) {
		int count = 0;
		for (int i=0;i<end.length();i++) {
			int c = scanner.read();
			count ++;
			if (c!=end.charAt(i)) {
				for (int j=0;j<count;j++) {
					scanner.unread();
				}
				return null;
			}
		}
		return fToken;
	}

}
