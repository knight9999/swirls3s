package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

public interface IBaseComparator {
	public boolean compare(ILineCharacterScanner scanner,boolean eofAllowed,boolean backScanner);
}
