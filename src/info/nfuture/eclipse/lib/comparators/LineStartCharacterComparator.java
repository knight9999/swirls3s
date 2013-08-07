package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

public class LineStartCharacterComparator implements ICharacterComparator {
	// 行の先端から始まるComparator
	// それ以外は、SimpleCharacterComparatorと同じ。
	
	protected String originalSequence;
	protected char firstChar;
	
	protected int count;
	
	public LineStartCharacterComparator(String seq) {
		originalSequence = seq;
		if (originalSequence.length()>0) {
			firstChar = originalSequence.charAt(0);
		} else {
			firstChar = 0;
		}
	}

	public boolean compare(ILineCharacterScanner scanner, boolean eofAllowed,
			boolean backScanner) {
		// 最初の１文字は、すでに読まれていて判断が終了しているとする
		// (この部分だけは、あとでunreadもしない)
		// backScannerは、成功時にunreadするかどうか？
		// 失敗時は、かならずunreadする。

		if (!scanner.isBeforeDelimiter(1)) {
			return false;
		}
		
		int count = 0;
		for (int i=1;i<originalSequence.length();i++) {
			int c = scanner.read();
			count++;
			if (c == ILineCharacterScanner.EOF) {
				if (eofAllowed) {
					if (backScanner) {
						for (int j=0;j<count;j++) {
							scanner.unread();
						}
					}
					return true;
				} else {
					for (int j=0;j<count;j++) {
						scanner.unread();
					}
					return false;
				}
			} else if (c != originalSequence.charAt(i)) {
				for (int j=0;j<count;j++) {
					scanner.unread();
				}
				return false;
			}
		}
		if (backScanner) {
			for (int j=0;j<count;j++) {
				scanner.unread();
			}
		}
		return true;
	}

	public char getFirstChar() {
		return firstChar;
	}

}
