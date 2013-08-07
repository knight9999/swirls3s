package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.DomainScanner;
import info.nfuture.eclipse.lib.ILineCharacterScanner;

public class SimpleCharacterComparatorWithException extends
		SimpleCharacterComparator {

	protected char preExceptChar;
	
	public SimpleCharacterComparatorWithException(String seq) {
		super(seq);
	}

	public SimpleCharacterComparatorWithException(String seq,char p) {
		super(seq);
		preExceptChar = p;
	}

	public SimpleCharacterComparatorWithException(String seq, boolean dis) {
		super(seq, dis);
	}

	@Override
	public boolean compare(ILineCharacterScanner scanner0, boolean eofAllowed,
			boolean backScanner) {
		DomainScanner scanner = (DomainScanner) scanner0;
		int c;
		if (scanner.getOffset()>1) {
			scanner.unread();
			scanner.unread();
			c = scanner.read();
			scanner.read();
			if (c == preExceptChar) {
				return false;
			}
		}
		return super.compare(scanner, eofAllowed, backScanner);
	}

	public char getPreExceptChar() {
		return preExceptChar;
	}

	public void setPreExceptChar(char preExceptChar) {
		this.preExceptChar = preExceptChar;
	}

}
