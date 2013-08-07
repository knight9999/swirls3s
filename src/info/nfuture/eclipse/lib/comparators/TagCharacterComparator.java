package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

import java.util.List;



public class TagCharacterComparator implements ICharacterComparator {
	// "<"で始まり、">"で終わる。
	// "<name 自由部分>"という形式で、name部分のみを指定。
	// 大文字小文字を問わずに判断する
	// ワイルドカード部のエスケープは\で行う。
	// "や'ではさまれた部分の閉じ部は無視する。
	
	protected String name;
	
	protected int count;

	protected char firstChar = '<';
	protected char lastChar = '>';
	
	public TagCharacterComparator(String name) {
		this.name = name.toLowerCase(); // 小文字に変換して格納
	}
	
	public boolean compare(ILineCharacterScanner scanner, boolean eofAllowed,
			boolean backScanner) {
		// eofAllowedは常に無視します。常にfalse扱いとなります。
		count = 0;
		
		// まず、名前部分のチェック
		if (! checkStr(name,scanner)) {
			scanback(scanner);
			return false;
		}
		
		// 空白をチェック
		int c = scanner.read();
		count++;
		if ( c == lastChar) {
			// この段階で成功
			if (backScanner) {
				scanback(scanner);
			}
			return true;
		}
		if (!(Character.isWhitespace(c) ||  c == '\t')) {
			// 失敗
			scanback(scanner);
			return false;
		}

		
		
		// 残りの文字列をチェック ダブルクォーテーションまたはクォーテーションで囲まれた領域は保護。さらにその中で\はエスケープとして処理。
		// 
		if (! checkInnerSequence(lastChar,scanner)) {
			scanback(scanner);
			return false;
		}
		
		// 成功！
		if (backScanner) {
			scanback(scanner);
		}
		return true;
	}
	
	protected void scanback(ILineCharacterScanner scanner) {
		for (int j=0;j<count;j++) {
			scanner.unread();
		}
	}
	protected boolean checkStr(String str,ILineCharacterScanner scanner) {
		for (int i=0;i<str.length();i++) {
			int c = scanner.read();
			count++;
			if ( c == ILineCharacterScanner.EOF) {
				return false;
			} else if (Character.toLowerCase(c) != str.charAt(i)) {
				return false;
			}
		}
		return true;
	}
	

	protected boolean checkInnerSequence(char lastChar,ILineCharacterScanner scanner) {
		int c = scanner.read();
		count++;
		int flagQuote = 0;
		while (c != ILineCharacterScanner.EOF) {
			if (flagQuote==0) {
				if (c == lastChar) {
					break;
				}
				if (c == '"' || c == '\'') { 
					flagQuote = c;
				} 
			} else {
				if (c == '\\') {
					scanner.read(); // １つ読み飛ばす
					count++;
				} else if (c == '"' || c == '\'') {
					flagQuote = 0;
				}
			}
			
			c = scanner.read();
			count++;
		}
		if (c == ILineCharacterScanner.EOF) {
			return false;
		}
		
		return true;
	}
	
	
	public char getFirstChar() {
		return firstChar;
	}


}
