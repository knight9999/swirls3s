package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.DomainScanner;
import info.nfuture.eclipse.lib.ILineCharacterScanner;
import info.nfuture.eclipse.lib.SubScanner;

import org.eclipse.jface.text.rules.Token;


public class ExCharacterComparator extends SimpleCharacterComparator {
	protected ICheckCharacter beforeCheck;
	protected ICheckCharacter afterCheck;
	
	public ExCharacterComparator(String seq) {
		super(seq);
		beforeCheck = null;
		afterCheck = null;
	}

	public ExCharacterComparator(String seq,ICheckCharacter beforeCheck,ICheckCharacter afterCheck) {
		super(seq);
		this.beforeCheck = beforeCheck;
		this.afterCheck = afterCheck;
	}
	
	public boolean compare(ILineCharacterScanner scanner0, boolean eofAllowed,
			boolean backScanner) {
		DomainScanner scanner = (DomainScanner) scanner0;
		int c;
		if (beforeCheck != null && scanner.getOffset()>1) {
			scanner.unread();
			scanner.unread();
			c = scanner.read();
			scanner.read();
			if (! beforeCheck.isTrueCharacter(c)) { 
				return false;
			}
		}
		if (afterCheck != null) {
			c = scanner.read();
			scanner.unread();
			if (c != ILineCharacterScanner.EOF && ! afterCheck.isTrueCharacter(c)) {
				return false;
			}
		}
		return super.compare(scanner,eofAllowed, backScanner);
	}

}
