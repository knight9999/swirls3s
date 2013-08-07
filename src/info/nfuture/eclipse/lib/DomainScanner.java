package info.nfuture.eclipse.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;


public class DomainScanner extends RuleBasedPartitionScanner implements ILineCharacterScanner {
	protected List<DomainTag> stack;
	protected String nextType;
	protected String key;
	
	@Deprecated
	public void setPredicateRules(IPredicateRule[] rules) {
		throw new UnsupportedOperationException();
	}
	
	public void setDomainRules(List<DomainRule> rules) {
		super.setPredicateRules( rules.toArray( new DomainRule[0]) );
	}
	
	public void setPartialRange(IDocument document, int offset, int length,
			String contentType , int partitionOffset) {
		super.setPartialRange(document,offset,length,contentType,partitionOffset);

		Assert.isTrue( document instanceof DomainDocument );
		
		stack = new ArrayList<DomainTag>();
		nextType = null;
		key = null;
		int keyStackLevel = 0;
//		releaseKeys = new ArrayList<String>();
//		fHereDocumentReleaseKey = null;
		
		try {
			// prevDomainPositionからstackを取得する！
			// prevDomainPositionからnextTypeも取得する！
			
			// これはもう少し早くする方法があるのかも、、、、。
			DomainPosition []dps = ((DomainDocument) document ).getDomainPositions();
			for (int i=0;i<dps.length;i++) {
				DomainPosition p = dps[i];
				if (p.offset + p.length - 1 < offset ) {
					Assert.isTrue( ! p.isDeleted() );
					
					nextType = p.getNextType();
					if (nextType != null) {
						stack.add( p.getTag() );
						if (p.getTag().getKey() != null) {
							key = p.getTag().getKey();
							keyStackLevel = stack.size() - 1;
						}
					} else {
						if (stack.size()>0) {
							DomainTag t = stack.remove( stack.size() - 1);
							nextType = t.getType();
							if (key != null && keyStackLevel == stack.size() ) {
								key = null;
								keyStackLevel = 0;
							}
						}
					}
				} else if (p.offset < offset) { 
					nextType = null;
					key = p.getTag().getKey();
				} else {
					break;
				}
			}
			
		} catch (BadPositionCategoryException e) {
			e.printStackTrace();
		}
		
	}

	
	@Override
	public void setRange(IDocument document, int offset, int length) {
		setPartialRange(document,offset,length,null,-1);
	}
	
	

	@Override
	public IToken nextToken() {
//		fHereDocumentReleaseKey = null;
		
		if (fContentType == null || fRules == null) {
			return originalNextToken();
		}
		boolean resume= (fPartitionOffset > -1 && fPartitionOffset <= fOffset);
		fTokenOffset= resume ? fPartitionOffset : fOffset;

		DomainRule rule;
		IToken token;

		for (int i= 0; i < fRules.length; i++) {
			rule= (DomainRule) fRules[i];
			token= rule.getSuccessToken();
			if (fContentType.equals(token.getData())) {
				token= rule.evaluate(this, resume);
				if (!token.isUndefined()) {
					fContentType= null;
					return token;
				}
			}
		}

		// haven't found any rule for this type of partition
		fContentType= null;
		if (resume)
			fOffset= fPartitionOffset;
	
		return originalNextToken();
	}

	protected IToken originalNextToken() {
		// RuleBasedScannerのnextToken
		fTokenOffset= fOffset;
		fColumn= UNDEFINED;

		if (fRules != null) {
			for (int i= 0; i < fRules.length; i++) {
				IToken token= (fRules[i].evaluate(this));
				if (!token.isUndefined()) {
					return token;
				}
			}
		}

		if (read() == EOF) {
			return Token.EOF;
		}
		return fDefaultReturnToken;
		
	}

	public List<DomainTag> getStack() {
		return stack;
	}

	public void setStack(List<DomainTag> stack) {
		this.stack = stack;
	}

	public void pushTag(DomainTag type) {
		stack.add(type);
	}
	
	public DomainTag popTag() {
		return stack.remove(stack.size()-1);
	}

	public DomainTag getLastTag() {
		return stack.get(stack.size()-1);
	}

	public String getNextType() {
		return nextType;
	}

	public void setNextType(String nextType) {
		this.nextType = nextType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getOffset() {
		return fOffset;
	}
	
	// 行の終端記号の集合を取得するときに利用。
	private static class DecreasingCharArrayLengthComparator implements Comparator<char []> {
		public int compare(char[] o1, char[] o2) {
			return o2.length - o1.length;
		}
	}
	private Comparator<char []> fLineDelimiterComparator= new DecreasingCharArrayLengthComparator();
	private char[][] fLineDelimiters;
	private char[][] fSortedLineDelimiters;
	private List<String> sortedLineDelimiters;
	
	public List<String> getSortedLineDelimiters() {
		// 行端末チェック用の準備
		char[][] originalDelimiters= getLegalLineDelimiters();
		int count= originalDelimiters.length;
		if (fLineDelimiters == null) {
			fSortedLineDelimiters= new char[count][];
			sortedLineDelimiters = new ArrayList<String>();
		} else {
			while (count > 0 && fLineDelimiters[count-1] == originalDelimiters[count-1])
				count--;
		}
		if (count != 0) {
			fLineDelimiters= originalDelimiters;
			System.arraycopy(fLineDelimiters, 0, fSortedLineDelimiters, 0, fLineDelimiters.length);
			Arrays.sort(fSortedLineDelimiters, fLineDelimiterComparator);
			sortedLineDelimiters = new ArrayList<String>();
			for (int i=0;i<fSortedLineDelimiters.length;i++) {
				sortedLineDelimiters.add( new String( fSortedLineDelimiters[i]));
			}
		}
		// 行端末チェックここまで
		return sortedLineDelimiters;
	}
	
	public boolean isBeforeDelimiter(int back) {
		// backの数だけ、現状の位置から前にもどって評価する
		// TODO backだけ戻れなかった場合は、falseにする。
		// TODO backだけ戻った時点がoffset=0の場合は、trueにする。
		
		for (int x=0;x<back;x++) {
			if (this.fOffset==0) {
				// backだけ戻れなかった場合
				for (int j=0;j<x;j++) {
					read();
				}
//				System.out.println("CAN NOT BACK");
				return false;
			}
			unread();
		}
		
		if (this.fOffset==0) {
			// documentの最初なので、trueとする
			for (int j=0;j<back;j++) {
				read();
			}
//			System.out.println("HEAD Ok");
			return true;
		}
		

		List<String> list = getSortedLineDelimiters();
		for (Iterator<String> it = list.iterator();it.hasNext();) {
			String str = it.next();
			int l = str.length();
			int i;
			if (l>fOffset) {
				continue;
			}
			for (i=0;i<l;i++) {
				unread();
			}
			for (i=0;i<l;i++) {
				int c = read();
				
				if (c != str.charAt(i)) {
					break;
				}
			}
			if (i==l) { // 成功
				for (int x=0;x<back;x++) {
					read();
				}
//				System.out.println("OTHER Ok");
				return true;
			} 
			for (i=i+1;i<l;i++) {
				read();
			}
		}
		for (int x=0;x<back;x++) {
			read();
		}
		return false;
	}

	protected int countCheckLineDelimiter;
	
	public int checkLineDelimiter(int c,boolean eofAllowed) {
		// 失敗時は戻るが、成功時は戻らない。
		countCheckLineDelimiter = 1;
		if (c == ICharacterScanner.EOF && eofAllowed) {
			return countCheckLineDelimiter;
		}
		
		boolean flag=false;
		for (Iterator<String> it = getSortedLineDelimiters().iterator();it.hasNext();) {
			String s = it.next();
			if (c == s.charAt(0)) {
				if (subRead(s,eofAllowed)) {
					flag = true;
				} else {
					for (int i=1;i<countCheckLineDelimiter;i++) {
						unread();
					}
				}
			}
		}
		if (!flag) {
			return 0;
		}
		return countCheckLineDelimiter;
	}
	
	protected boolean subRead(String str,boolean eofAllowed) {
		for (int i=1;i<str.length();i++) {
			int c = read();
			countCheckLineDelimiter ++;
			if (c == ICharacterScanner.EOF) {
				if (eofAllowed) {
					return true; // 成功
				} else {
					return false; // 失敗
				}
			} else	if (c != str.charAt(i)) {
				return false;
			}
		}
		return true; // 成功
	}
	
}
