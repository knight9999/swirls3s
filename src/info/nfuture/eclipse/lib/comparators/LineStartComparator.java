package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

public class LineStartComparator implements ILineComparator {

	public boolean compare(ILineCharacterScanner scanner, boolean eofAllowed,
			boolean backScanner) {
		if (!scanner.isBeforeDelimiter(1)) {
			return false;
		}
		scanner.unread();
		int c = scanner.read();
		if (c == ' ') {
			return false;
		}
		int n = scanner.checkLineDelimiter(c,false);
		if (n>0) {
			for (int i=1;i<n;i++) {
				scanner.unread();
			}
			return false;
		}
		
		return true;
	}

}
