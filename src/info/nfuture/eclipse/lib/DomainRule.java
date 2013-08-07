package info.nfuture.eclipse.lib;

import info.nfuture.eclipse.lib.comparators.IBaseComparator;
import info.nfuture.eclipse.lib.comparators.ICharacterComparator;
import info.nfuture.eclipse.lib.comparators.ICharacterComparatorWithRefAfters;
import info.nfuture.eclipse.lib.comparators.ILineComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;



public class DomainRule implements IPredicateRule {

	// �s�̏I�[�L���̏W�����擾����Ƃ��ɗ��p�B
	private static class DecreasingCharArrayLengthComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			return ((char[]) o2).length - ((char[]) o1).length;
		}
	}
	private Comparator fLineDelimiterComparator= new DecreasingCharArrayLengthComparator();
	
	// ���̃��[�����K�p���ꂽ�ꍇ�ɕԂ����g�[�N���̐��`�B
	// ���ۂɕԂ����̂́A����ɂ���ɕt����񂪉���������̂ƂȂ�B
	protected IToken fToken;
	
	protected IBaseComparator fStartSequence;
	protected IBaseComparator fEndSequence;
	
	protected char fEscapeCharacter;
	protected boolean fEscapeContinuesLine;
	protected boolean fBreaksOnEOL; // �p�^�[�����s���ŏI��邩
	protected boolean fBreaksOnEOF = true; // �p�^�[�����t�@�C�����ŏI��邩�H

	protected boolean fHereDocument = false; // �q�A�h�L�������g�^�C�v���H(�s�w��������)
	
//	private char[][] fLineDelimiters;
//	private char[][] fSortedLineDelimiters;
	
	protected List<DomainRule> subRules;
	protected List<DomainRule> superRules;  // ���ꂪ�ݒ肳��Ă���ƁA�e��Ԃ���̑J�ڂł������̃��[���͍̗p����Ȃ��B

	public DomainRule(IBaseComparator startSequence, IBaseComparator endSequence, IToken token, char escapeCharacter, boolean breaksOnEOL) {
		Assert.isNotNull(token);

		fStartSequence = startSequence;
		fEndSequence = endSequence;
		fToken= token;
		fEscapeCharacter= escapeCharacter;
		fBreaksOnEOL = breaksOnEOL;
		subRules = new ArrayList<DomainRule>();
		superRules = new ArrayList<DomainRule>();
	}
	
	
	protected IToken doEvaluate(DomainScanner scanner, boolean resume) {
		if (resume) {
			if (changeSequenceDetected(scanner)) {
				return fToken; 
			}
			scanner.unread();
		} else {
			scanner.setKey( null );
			// scanner��nextToken�ɉ�������ꍇ�́A�D�悵�Ă��̗̈���擾����
			boolean flagNormalEvaluate = true;
			if (scanner.getStack().size()>0) {
				int c= scanner.read();
				scanner.unread();
				if (c != ICharacterScanner.EOF) {
//				if (true) {
					String nextType = scanner.getNextType();
					if (nextType != null && ( nextType.equals((String) fToken.getData() ) ) ) {
						startSequenceDetected(scanner, c, false,null,true); // �X�^�[�g������ǂݔ�΂�
						scanner.read();
						
						if (changeSequenceDetected(scanner)) {
							return fToken;
						}
						scanner.unread();   // ���ꂪ�����ł������A���Ȃ�m�F���邱�ƁI
					} else if ( nextType == null && scanner.getLastTag().getType().equals((String) fToken.getData() ) ) {
						scanner.setKey( scanner.getLastTag().getKey() );
						if (changeSequenceDetected(scanner,true)) {
//							scanner.popToken();
							return fToken;
						}
						scanner.unread();   // ���ꂪ�����ł������A���Ȃ�m�F���邱�ƁI
					}
					flagNormalEvaluate = false;
				}
			}
			
			if (flagNormalEvaluate) {
				int c= scanner.read();
				if (startSequenceDetected(scanner, c, false,null,false) ) {
					if (changeSequenceDetected(scanner)) {
						return fToken;  
					}
				}
				scanner.unread();   // ���ꂪ�����ł������A���Ȃ�m�F���邱�ƁI
			}
		}
		return Token.UNDEFINED;
	}
	
	protected boolean changeSequenceDetected(DomainScanner scanner) {
		return changeSequenceDetected(scanner,false);
	}
	
	
	protected boolean changeSequenceDetected(DomainScanner scanner,boolean withPop) {
		// withPop == true�̏ꍇ�́A��������scanner.popToken�����s����
		
		int readCount= 1;
		int c;
//		String nowKey = null;
//		if (scanner.getStack().size()>0) {
//			nowKey = scanner.getLastTag().getKey();
//		}
		while ((c= scanner.read()) != ICharacterScanner.EOF) {
			if (c == fEscapeCharacter) {
				// Skip escaped character(s)
				if (fEscapeContinuesLine) { 
					// 1�s�ŏI���悤�ȃ��[���̏ꍇ�ŁA���̃t���O�������Ă���ƁA�G�X�P�[�v�ōs���I����Ă���ꍇ�͎��̍s�ւƂ��̏���������
					c= scanner.read(); 
					readCount ++; // TODO ���Ƃ�PatternRule�ɂ͂Ȃ����A������readCount�𑝂₵���B
					if (scanner.checkLineDelimiter(c, true)>0) {
						break;
					}
				} else {
					scanner.read();  
					readCount ++; // TODO ���Ƃ�PatternRule�ɂ͂Ȃ����A������readCount�𑝂₵���B
				}
			} else {
				boolean flagCheckOther = true;  // ����A�Ӗ����邩�ǂ����ǂ�������Ȃ��B
				/*
				 * �ʏ�I��
				 */
				if (!fHereDocument && fEndSequence != null ) { 
					if (fEndSequence instanceof ICharacterComparator && 
						c == ( (ICharacterComparator) fEndSequence).getFirstChar() ) {
						flagCheckOther = false;
						if (sequenceDetected(scanner, fEndSequence, true, false)) {
							scanner.setNextType(null);
							if (withPop==true) {
								scanner.popTag();
							}
							return true;
						}
					}
					if (fEndSequence instanceof ICharacterComparatorWithRefAfters) {
//						flagCheckOther = false;  �ꕶ����ǂ�(c)������Ă��Ȃ��̂ŁA�����ł�EOL���ǂ����̔��f�͏o���Ȃ�
						if ( ( (ICharacterComparatorWithRefAfters) fEndSequence).compare(scanner.getKey(),scanner, true, false) ) {
							scanner.setNextType(null);
							if (withPop==true) {
								scanner.popTag();
							}
							return true;
						}
					}
				}

				/*
				 * �q�A�h�L�������g�ŏI��
				 */
				if (fHereDocument && scanner.isBeforeDelimiter(1) ) {   // ���̔���������A���s + �i�C�ӃX�y�[�X�H�j + �����L�[�ɕύX����
					flagCheckOther = false;

					if ( ( (ICharacterComparatorWithRefAfters) fEndSequence).compare(scanner.getKey(),scanner, true, false) ) {
						scanner.setNextType(null);
						if (withPop==true) {
							scanner.popTag();
						}
						return true;
					}
				}
				/*
				 * ����subRule�����邽�߂ɏI��
				 */
				for (Iterator<DomainRule> it = subRules.iterator();it.hasNext();) {
					DomainRule subRule = it.next();
					boolean a = subRule.startSequenceDetected(scanner,c,true,this,false); 
					if (a) {
						// ***************************************************************************
						// �����ŁA�X�L���i�[�ɁA���̃��[���ƃR���e���g�^�C�v�A�X�e�[�^�X���o��������B
						// ***************************************************************************
						if (withPop==true) {
							scanner.popTag();
						}
						int offset = scanner.getTokenOffset();
						String nextType = (String) subRule.getFToken().getData();
						IToken pushToken = this.getFToken();
						
						scanner.setNextType( nextType );
						scanner.pushTag( new DomainTag( pushToken , scanner.getKey() ) );
						scanner.unread();
						return true;
					}
				}
				/*
				 * ���s�ɂ��I��
				 */
				if (flagCheckOther && fBreaksOnEOL) {
					if (scanner.checkLineDelimiter(c, true)>0) {
						scanner.setNextType(null);
//						scanner.setKey(null);
						if (withPop==true) {
							scanner.popTag();
						}
						return true;
					}
				}

			}
			readCount++;
		}
		
		if (fBreaksOnEOF) {
			scanner.setNextType(null);
			if (withPop==true) {
				scanner.popTag();
			}
			return true;
		}

		for (; readCount > 0; readCount--)
			scanner.unread();

			
		return false;
	}
	
	public boolean startSequenceDetected(DomainScanner scanner,int c,boolean backScanner,DomainRule parent,boolean flagForceLoad) {
		
		if (!flagForceLoad && superRules.size()>0) {
			if (parent==null) { 
				return false;  // ���[�����ݒ肳��Ă���̂ɁA�e��null�̏ꍇ�͕K��false
			}
			if (superRules.indexOf(parent) == -1) {
				return false;
			}
		}
		if ( fStartSequence instanceof ICharacterComparator && 
				c == ( (ICharacterComparator) fStartSequence).getFirstChar() ) {
			if (sequenceDetected(scanner,fStartSequence,false,backScanner)) {
				if (fStartSequence instanceof ICharacterComparatorWithRefAfters && !backScanner) { // ���ɖ߂��ꍇ�́AKey�͕ω������Ȃ�
					String releaseKey = ((ICharacterComparatorWithRefAfters) fStartSequence).getRefAfter();
					scanner.setKey( releaseKey );
				}
				return true;
			}
		} else if ( fStartSequence instanceof ILineComparator ) {
			if (sequenceDetected(scanner,fStartSequence,false,backScanner)) {
				if (fStartSequence instanceof ICharacterComparatorWithRefAfters && !backScanner) {  // ���ɖ߂��ꍇ�́AKey�͕ω������Ȃ�
					String releaseKey = ((ICharacterComparatorWithRefAfters) fStartSequence).getRefAfter();
					scanner.setKey( releaseKey );
				}
				return true;
			}
		}
		return false;
	}
	
	protected boolean sequenceDetected(ILineCharacterScanner scanner, IBaseComparator sequence, boolean eofAllowed, boolean backScanner) {
		return sequence.compare(scanner,eofAllowed,backScanner);
	}

	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		Assert.isTrue( scanner instanceof DomainScanner);
		return doEvaluate((DomainScanner) scanner, resume);
	}

	public IToken getSuccessToken() {
		return fToken;
	}

	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}



	public IToken getFToken() {
		return fToken;
	}



	public void setFToken(IToken token) {
		fToken = token;
	}



	public IBaseComparator getFEndSequence() {
		return fEndSequence;
	}



	public void setFEndSequence(ICharacterComparator endSequence) {
		fEndSequence = endSequence;
	}



	public char getFEscapeCharacter() {
		return fEscapeCharacter;
	}



	public void setFEscapeCharacter(char escapeCharacter) {
		fEscapeCharacter = escapeCharacter;
	}



	public boolean isFEscapeContinuesLine() {
		return fEscapeContinuesLine;
	}



	public void setFEscapeContinuesLine(boolean escapeContinuesLine) {
		fEscapeContinuesLine = escapeContinuesLine;
	}



	public boolean isFBreaksOnEOL() {
		return fBreaksOnEOL;
	}



	public void setFBreaksOnEOL(boolean breaksOnEOL) {
		fBreaksOnEOL = breaksOnEOL;
	}



	public boolean isFBreaksOnEOF() {
		return fBreaksOnEOF;
	}



	public void setFBreaksOnEOF(boolean breaksOnEOF) {
		fBreaksOnEOF = breaksOnEOF;
	}



	public List<DomainRule> getSubRules() {
		return subRules;
	}



	public void setSubRules(List<DomainRule> subRules) {
		this.subRules = subRules;
	}

	public void addSubRule(DomainRule subRule) {
		this.subRules.add(subRule);
	}

	public void addSuperRule(DomainRule superRule) {
		this.superRules.add(superRule);
	}

	public IBaseComparator getFStartSequence() {
		return fStartSequence;
	}



	public void setFStartSequence(ICharacterComparator startSequence) {
		fStartSequence = startSequence;
	}



	public List<DomainRule> getSuperRules() {
		return superRules;
	}


	public void setSuperRules(List<DomainRule> superRules) {
		this.superRules = superRules;
	}


	public boolean isFHereDocument() {
		return fHereDocument;
	}


	public void setFHereDocument(boolean hereDocument) {
		fHereDocument = hereDocument;
	}

	
	
}
