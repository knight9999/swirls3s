package info.nfuture.eclipse.lib;

import java.util.List;

import org.eclipse.jface.text.rules.ICharacterScanner;

public interface ILineCharacterScanner extends ICharacterScanner {
	public List<String> getSortedLineDelimiters();
	
	public boolean isBeforeDelimiter(int back);
	
	public int checkLineDelimiter(int c,boolean eofAllowed);
	// 1文字目は、すでにcに値が入っていると考える。
	// 結果がゼロの場合、改行ではない。
	// 結果が正値であれば、改行であり、その数だけ移動している。
}
