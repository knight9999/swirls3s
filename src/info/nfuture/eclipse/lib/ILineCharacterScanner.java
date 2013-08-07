package info.nfuture.eclipse.lib;

import java.util.List;

import org.eclipse.jface.text.rules.ICharacterScanner;

public interface ILineCharacterScanner extends ICharacterScanner {
	public List<String> getSortedLineDelimiters();
	
	public boolean isBeforeDelimiter(int back);
	
	public int checkLineDelimiter(int c,boolean eofAllowed);
	// 1�����ڂ́A���ł�c�ɒl�������Ă���ƍl����B
	// ���ʂ��[���̏ꍇ�A���s�ł͂Ȃ��B
	// ���ʂ����l�ł���΁A���s�ł���A���̐������ړ����Ă���B
}
