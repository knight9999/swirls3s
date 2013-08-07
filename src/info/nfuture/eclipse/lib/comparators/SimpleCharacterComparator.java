package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

import java.util.Iterator;
import java.util.List;



public class SimpleCharacterComparator implements ICharacterComparator {
	// ���C���h�J�[�h�Ȃǂ͎g���Ȃ��A��ԊȒP�ȃ^�C�v��Comparator
	// �Y��������������܂܂Ȃ��ꍇ�A�R���X�g���N�^�̑�������true�ɂ���
	
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
		// �ŏ��̂P�����́A���łɓǂ܂�Ă��Ĕ��f���I�����Ă���Ƃ���
		// (���̕��������́A���Ƃ�unread�����Ȃ�)
		// backScanner�́A��������unread���邩�ǂ����H
		// ���s���́A���Ȃ炸unread����B
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
