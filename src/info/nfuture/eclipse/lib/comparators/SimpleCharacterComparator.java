package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

import java.util.Iterator;
import java.util.List;



public class SimpleCharacterComparator implements ICharacterComparator {
	// ワイルドカードなどは使えない、一番簡単なタイプのComparator
	// 該当した文字列を含まない場合、コンストラクタの第二引数をtrueにする
	
	protected String originalSequence;
	protected char firstChar;
	protected boolean disincludeSequence;
	
	protected int count;
	
	public SimpleCharacterComparator(String seq) {
		originalSequence = seq;
		if (originalSequence.length()>0) {
			firstChar = originalSequence.charAt(0);
		} else {
			firstChar = 0;
		}
		disincludeSequence = false;
	}
	
	public SimpleCharacterComparator(String seq,boolean dis) {
		this(seq);
		disincludeSequence = dis;
	}

	public boolean compare(ILineCharacterScanner scanner, boolean eofAllowed,
			boolean backScanner) {
		// 最初の１文字は、すでに読まれていて判断が終了しているとする
		// (この部分だけは、あとでunreadもしない)
		// backScannerは、成功時にunreadするかどうか？
		// 失敗時は、かならずunreadする。
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

	public char getFirstChar() {
		return firstChar;
	}

//	public int length() {
//		return originalSequence.length();
//	}

}
