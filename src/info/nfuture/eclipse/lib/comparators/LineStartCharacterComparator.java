package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

public class LineStartCharacterComparator implements ICharacterComparator {
	// �s�̐�[����n�܂�Comparator
	// ����ȊO�́ASimpleCharacterComparator�Ɠ����B
	
	protected String originalSequence;
	protected char firstChar;
	
	protected int count;
	
	public LineStartCharacterComparator(String seq) {
		originalSequence = seq;
		if (originalSequence.length()>0) {
			firstChar = originalSequence.charAt(0);
		} else {
			firstChar = 0;
		}
	}

	public boolean compare(ILineCharacterScanner scanner, boolean eofAllowed,
			boolean backScanner) {
		// �ŏ��̂P�����́A���łɓǂ܂�Ă��Ĕ��f���I�����Ă���Ƃ���
		// (���̕��������́A���Ƃ�unread�����Ȃ�)
		// backScanner�́A��������unread���邩�ǂ����H
		// ���s���́A���Ȃ炸unread����B

		if (!scanner.isBeforeDelimiter(1)) {
			return false;
		}
		
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
		if (backScanner) {
			for (int j=0;j<count;j++) {
				scanner.unread();
			}
		}
		return true;
	}

	public char getFirstChar() {
		return firstChar;
	}

}
