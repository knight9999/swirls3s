package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;


public class HereDocumentStartComparator implements
		ICharacterComparatorWithRefAfters {
	// "<<"�ł͂��܂����("<<"�ȊO�ɂ��邱�Ƃ��\)
	// <<EOS��<<-EOS�Ƃ�������������A<<"EOS"�A<<-"EOS"�Ƃ��������������\�B
	// EOS�̕����́A�A���t�@�x�b�g�A�����Ȃ�Ȃ�ł��n�j�D
	
	protected String prev="<<";
	protected char quote;           // quote�Ƃ��ĉ����g����
	protected boolean alwaysQuote;  // quote���K�{���H
	protected int count;
	protected String releaseKey;
	
	public HereDocumentStartComparator(String prev,char quote,boolean alwaysQuote) {
		this.prev = prev;
		this.quote = quote;
		this.alwaysQuote = alwaysQuote;
		
	}

	public boolean compare(String refAfter, ILineCharacterScanner scanner, // �_�~�[
			boolean eofAllowed, boolean backScanner) {
		return false;
	}
	
	public boolean compare(ILineCharacterScanner scanner, boolean eofAllowed,
			boolean backScanner) {
		// eofAllowed�͏�ɖ������܂��B���false�����ƂȂ�܂��B
		count = 0;
		
		// �܂��Aprev(<<)�����̃`�F�b�N
		if (! checkStr(1,prev,scanner)) {
			scanback(scanner);
			return false;
		}
		
		// �}�C�i�X�����邩�ǂ�������
		boolean flagMinus = false;
		int c = scanner.read();
		count++;
		if (c=='-') {
			flagMinus = true;
			c = scanner.read();
			count++;
		}
		
		// �_�u���N�H�[�g�����邩�ǂ�������
		boolean flagQuote = false;
		if (c==quote) {
			flagQuote = true;
			c = scanner.read();
			count++;
		} else if (alwaysQuote) {
			scanback(scanner);
			return false;
		}
		
		StringBuffer buf = new StringBuffer();
		while (c != ILineCharacterScanner.EOF) {
			if (Character.isLetterOrDigit(c) || c=='_') {
				buf.append( (char) c );
			} else {
				break;
			}
			c = scanner.read();
			count++;
		}
		
		if (c == ILineCharacterScanner.EOF) {
			scanback(scanner);
			return false;
		}

		releaseKey = buf.toString();
		if (releaseKey.length()==0) {
			scanback(scanner);
			return false;
		}
		
		if (flagQuote) {
			if (c != quote) {
				scanback(scanner);
				return false;
			}
			c = scanner.read();
			count++;
		}
		
		if (flagMinus) {
			releaseKey = "-" + releaseKey;
		}
		
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
	protected boolean checkStr(int start,String str,ILineCharacterScanner scanner) {
		for (int i=start;i<str.length();i++) {
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
	

	public String getRefAfter() {
		return releaseKey;
	}


	public char getFirstChar() {
		return prev.charAt(0);
	}

}
