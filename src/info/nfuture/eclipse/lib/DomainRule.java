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

	// 行の終端記号の集合を取得するときに利用。
	private static class DecreasingCharArrayLengthComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			return ((char[]) o2).length - ((char[]) o1).length;
		}
	}
	private Comparator fLineDelimiterComparator= new DecreasingCharArrayLengthComparator();
	
	// このルールが適用された場合に返されるトークンの雛形。
	// 実際に返されるのは、これにさらに付加情報が加わったものとなる。
	protected IToken fToken;
	
	protected IBaseComparator fStartSequence;
	protected IBaseComparator fEndSequence;
	
	protected char fEscapeCharacter;
	protected boolean fEscapeContinuesLine;
	protected boolean fBreaksOnEOL; // パターンが行末で終わるか
	protected boolean fBreaksOnEOF = true; // パターンがファイル末で終わるか？

	protected boolean fHereDocument = false; // ヒアドキュメントタイプか？(行指向文字列)
	
//	private char[][] fLineDelimiters;
//	private char[][] fSortedLineDelimiters;
	
	protected List<DomainRule> subRules;
	protected List<DomainRule> superRules;  // これが設定されていると、親状態からの遷移でしかこのルールは採用されない。

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
			// scannerのnextTokenに何かある場合は、優先してこの領域を取得する
			boolean flagNormalEvaluate = true;
			if (scanner.getStack().size()>0) {
				int c= scanner.read();
				scanner.unread();
				if (c != ICharacterScanner.EOF) {
//				if (true) {
					String nextType = scanner.getNextType();
					if (nextType != null && ( nextType.equals((String) fToken.getData() ) ) ) {
						startSequenceDetected(scanner, c, false,null,true); // スタート部分を読み飛ばす
						scanner.read();
						
						if (changeSequenceDetected(scanner)) {
							return fToken;
						}
						scanner.unread();   // これがここでいいか、かなり確認すること！
					} else if ( nextType == null && scanner.getLastTag().getType().equals((String) fToken.getData() ) ) {
						scanner.setKey( scanner.getLastTag().getKey() );
						if (changeSequenceDetected(scanner,true)) {
//							scanner.popToken();
							return fToken;
						}
						scanner.unread();   // これがここでいいか、かなり確認すること！
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
				scanner.unread();   // これがここでいいか、かなり確認すること！
			}
		}
		return Token.UNDEFINED;
	}
	
	protected boolean changeSequenceDetected(DomainScanner scanner) {
		return changeSequenceDetected(scanner,false);
	}
	
	
	protected boolean changeSequenceDetected(DomainScanner scanner,boolean withPop) {
		// withPop == trueの場合は、成功時にscanner.popTokenを実行する
		
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
					// 1行で終わるようなルールの場合で、このフラグがたっていると、エスケープで行が終わっている場合は次の行へとその処理が続く
					c= scanner.read(); 
					readCount ++; // TODO もとのPatternRuleにはないが、ここでreadCountを増やした。
					if (scanner.checkLineDelimiter(c, true)>0) {
						break;
					}
				} else {
					scanner.read();  
					readCount ++; // TODO もとのPatternRuleにはないが、ここでreadCountを増やした。
				}
			} else {
				boolean flagCheckOther = true;  // これ、意味あるかどうか良く分からない。
				/*
				 * 通常終了
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
//						flagCheckOther = false;  一文字先読み(c)をやっていないので、ここではEOLかどうかの判断は出来ない
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
				 * ヒアドキュメントで終了
				 */
				if (fHereDocument && scanner.isBeforeDelimiter(1) ) {   // この判定条件を、改行 + （任意スペース？） + 解除キーに変更する
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
				 * 次のsubRuleが来るために終了
				 */
				for (Iterator<DomainRule> it = subRules.iterator();it.hasNext();) {
					DomainRule subRule = it.next();
					boolean a = subRule.startSequenceDetected(scanner,c,true,this,false); 
					if (a) {
						// ***************************************************************************
						// ここで、スキャナーに、次のルールとコンテントタイプ、ステータスを覚えさせる。
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
				 * 改行により終了
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
				return false;  // ルールが設定されているのに、親がnullの場合は必ずfalse
			}
			if (superRules.indexOf(parent) == -1) {
				return false;
			}
		}
		if ( fStartSequence instanceof ICharacterComparator && 
				c == ( (ICharacterComparator) fStartSequence).getFirstChar() ) {
			if (sequenceDetected(scanner,fStartSequence,false,backScanner)) {
				if (fStartSequence instanceof ICharacterComparatorWithRefAfters && !backScanner) { // 元に戻す場合は、Keyは変化させない
					String releaseKey = ((ICharacterComparatorWithRefAfters) fStartSequence).getRefAfter();
					scanner.setKey( releaseKey );
				}
				return true;
			}
		} else if ( fStartSequence instanceof ILineComparator ) {
			if (sequenceDetected(scanner,fStartSequence,false,backScanner)) {
				if (fStartSequence instanceof ICharacterComparatorWithRefAfters && !backScanner) {  // 元に戻す場合は、Keyは変化させない
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
