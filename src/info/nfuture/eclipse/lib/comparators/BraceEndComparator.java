package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

public class BraceEndComparator implements ICharacterComparatorWithRefAfters {

	protected int count;
	
	public boolean compare(String refAfter, ILineCharacterScanner scanner,
			boolean eofAllowed, boolean backScanner) {
		scanner.unread(); // 1文字目がすでに読み込まれているので、１度バックする
		count = 0;
		for (int i=0;i<refAfter.length();i++) {
			int c = scanner.read();
			count++;
			if (c == ILineCharacterScanner.EOF) {
				if (eofAllowed) {
					if (backScanner) {
						for (int j=0;j<count;j++) {
							scanner.unread();
						}
						scanner.read(); // 最初にバックした分もどす
					}
					return true;
				} else {
					for (int j=0;j<count;j++) {
						scanner.unread();
					}
					scanner.read(); // 最初にバックした分もどす
					return false;
				}
				
			} else if (c != refAfter.charAt(i)) {
				for (int j=0;j<count;j++) {
					scanner.unread();
				}
//				System.out.println( new Character( (char) c) );
				scanner.read(); // 最初にバックした分もどす
				return false;
			}
		}
		
		// int c = scanner.read();
		
		if (backScanner) {
			for (int j=0;j<count;j++) {
				scanner.unread();
			}
			scanner.read(); // 最初にバックした分もどす
		}
		return true;
	}

	public String getRefAfter() {
		// ダミー
		return null;
	}

	public char getFirstChar() {
		// ダミー
		return 0;
	}

	public boolean compare(ILineCharacterScanner scanner, boolean eofAllowed,
			boolean backScanner) {
		// ダミー
		return false;
	}

}
