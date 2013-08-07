package info.nfuture.eclipse.lib.comparators;


import info.nfuture.eclipse.lib.ILineCharacterScanner;



public class HereDocumentEndComparator implements
		ICharacterComparatorWithRefAfters {
	// 「refAfter + 改行」により、シーケンスが来たと判定する。
	
	protected int count;
	
	public boolean compare(String refAfter, ILineCharacterScanner scanner,
			boolean eofAllowed, boolean backScanner) {
		if (refAfter==null) { // 2009/6/18
			return false;
		}
		scanner.unread(); // 1文字目がすでに読み込まれているので、１度バックする
		count = 0;
		int i0 = 0;
		
		int cs = refAfter.charAt(0);
		if (cs == '-') {
			// １文字目がマイナスの場合、それは任意の空白が前にあってもよいことをあらわす。
			i0 = 1;
			
			cs = scanner.read();
			count++;
			while (Character.isWhitespace(cs) || cs == '\t') {
				cs = scanner.read();
				count++;
			}
			scanner.unread();
			count--;
		}
		
		for (int i=i0;i<refAfter.length();i++) {
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
				scanner.read(); // 最初にバックした分もどす
				return false;
			}
		}
		
		int c = scanner.read();
		int d = scanner.checkLineDelimiter(c,eofAllowed);
		if (d==0) {
			for (int j=0;j<count;j++) {
				scanner.unread();
			}
			scanner.read(); // 最初にバックした分もどす
			return false;
		}	
		count += d;
		
		if (backScanner) {
			for (int j=0;j<count;j++) {
				scanner.unread();
			}
			scanner.read(); // 最初にバックした分もどす
		}
		return true;
	}

//	protected boolean checkLineDelimiter(int c,ILineCharacterScanner scanner,boolean eofAllowed) {
//		boolean flag = false;
//		for (Iterator<String> it = scanner.getSortedLineDelimiters().iterator();it.hasNext();) {
//			String s = it.next();
//			if (c == s.charAt(0) && subRead(scanner,s,eofAllowed)) {
//				flag = true;
//			} 
//		}
//		if (!flag) {
//			return false;
//		}
//		return true;
//	}
//	
//	protected boolean subRead(ILineCharacterScanner scanner,String str,boolean eofAllowed) {
//		for (int i=1;i<str.length();i++) {
//			int c = scanner.read();
//			count ++;
//			if (c == ICharacterScanner.EOF) {
//				if (eofAllowed) {
//					return true; // 成功
//				} else {
//					return false; // 失敗
//				}
//			} else	if (c != str.charAt(i)) {
//				return false;
//			}
//		}
//		return true; // 成功
//	}

	
	
	
	
	
	
	
	
	
	
	public String getRefAfter() { // ダミー
		return null;
	}

	public boolean compare(ILineCharacterScanner scanner, boolean eofAllowed, // ダミー
			boolean backScanner) {
		return false;
	}

	public char getFirstChar() { // ダミー
		return 0;
	}

}
