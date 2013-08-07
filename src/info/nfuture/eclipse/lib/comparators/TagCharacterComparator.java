package info.nfuture.eclipse.lib.comparators;

import info.nfuture.eclipse.lib.ILineCharacterScanner;

import java.util.List;



public class TagCharacterComparator implements ICharacterComparator {
	// "<"�Ŏn�܂�A">"�ŏI���B
	// "<name ���R����>"�Ƃ����`���ŁAname�����݂̂��w��B
	// �啶�����������킸�ɔ��f����
	// ���C���h�J�[�h���̃G�X�P�[�v��\�ōs���B
	// "��'�ł͂��܂ꂽ�����̕����͖�������B
	
	protected String name;
	
	protected int count;

	protected char firstChar = '<';
	protected char lastChar = '>';
	
	public TagCharacterComparator(String name) {
		this.name = name.toLowerCase(); // �������ɕϊ����Ċi�[
	}
	
	public boolean compare(ILineCharacterScanner scanner, boolean eofAllowed,
			boolean backScanner) {
		// eofAllowed�͏�ɖ������܂��B���false�����ƂȂ�܂��B
		count = 0;
		
		// �܂��A���O�����̃`�F�b�N
		if (! checkStr(name,scanner)) {
			scanback(scanner);
			return false;
		}
		
		// �󔒂��`�F�b�N
		int c = scanner.read();
		count++;
		if ( c == lastChar) {
			// ���̒i�K�Ő���
			if (backScanner) {
				scanback(scanner);
			}
			return true;
		}
		if (!(Character.isWhitespace(c) ||  c == '\t')) {
			// ���s
			scanback(scanner);
			return false;
		}

		
		
		// �c��̕�������`�F�b�N �_�u���N�H�[�e�[�V�����܂��̓N�H�[�e�[�V�����ň͂܂ꂽ�̈�͕ی�B����ɂ��̒���\�̓G�X�P�[�v�Ƃ��ď����B
		// 
		if (! checkInnerSequence(lastChar,scanner)) {
			scanback(scanner);
			return false;
		}
		
		// �����I
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
					scanner.read(); // �P�ǂݔ�΂�
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
