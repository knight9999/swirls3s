package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

import java.util.List;



public interface ICharacterComparatorWithRefAfters extends ICharacterComparator {
	public String getRefAfter(); // 後方参照用。
	public boolean compare(String refAfter,ILineCharacterScanner scanner,boolean eofAllowed,boolean backScanner); // 後方参照を用いた場合の処理
}
