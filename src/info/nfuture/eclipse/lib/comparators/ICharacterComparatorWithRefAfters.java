package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

import java.util.List;



public interface ICharacterComparatorWithRefAfters extends ICharacterComparator {
	public String getRefAfter(); // ����Q�Ɨp�B
	public boolean compare(String refAfter,ILineCharacterScanner scanner,boolean eofAllowed,boolean backScanner); // ����Q�Ƃ�p�����ꍇ�̏���
}
