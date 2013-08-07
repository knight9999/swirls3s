package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

public class BraceStartComparator implements ICharacterComparatorWithRefAfters {
	// SimpleCharacterComparatorの、閉じタグバリエーション記憶用
	
	protected String originalSequence;
	protected char firstChar;
	protected boolean disincludeSequence;
	
	protected int count;
	protected String releaseKey;
	
	protected Character [][] data = {
			{ '[',']'} ,
			{ '<','>'} ,
			{ '(',')'} ,
			{ '{','}'} ,
			{ '|','|'} ,
			{ '*','*'} ,
			{ '!','!'}
		};
	public BraceStartComparator(String seq) {
		originalSequence = seq;
		if (originalSequence.length()>0) {
			firstChar = originalSequence.charAt(0);
		} else {
			throw new RuntimeException("設定文字列の長さをゼロには出来ません！");
		}
		disincludeSequence = false;
	}

	public BraceStartComparator(String seq,boolean dis) {
		this(seq);
		disincludeSequence = dis;
	}

	public boolean compare(ILineCharacterScanner scanner, boolean eofAllowed,
			boolean backScanner) {
		int count = 0;
		int c;
		for (int i=1;i<originalSequence.length();i++) {
			c = scanner.read();
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
		c = scanner.read();
		count++;
		boolean flagOk = false;
		for (int i2=0;i2<data.length;i2++) {
			if (data[i2][0].charValue() == c) {
				flagOk = true;
				releaseKey = data[i2][1].toString();
			}
		}
		if (! flagOk) {
			for (int j=0;j<count;j++) {
				scanner.unread();
			}
			return false;
		}
		
		if (backScanner || disincludeSequence ) {
			for (int j=0;j<count;j++) {
				scanner.unread();
			}
		}
		if (disincludeSequence) {
			scanner.unread();
		}
		return true;
	}

	public boolean compare(String refAfter, ILineCharacterScanner scanner,  // ダミー
			boolean eofAllowed, boolean backScanner) {
		return false;
	}

	public String getRefAfter() {
		return releaseKey;
	}

	public char getFirstChar() {
		return firstChar;
	}


}
