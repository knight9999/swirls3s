package info.nfuture.eclipse.lib;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.rules.IToken;

public class DomainPosition extends TypedPosition {
	protected DomainPosition prev;
	protected DomainTag tag;
	protected String nextType;
	
//	protected String fHereDocumentReleaseKey;
	
	public DomainPosition(int offset, int length, DomainTag tag,String nextType) {
		super(offset,length,null);
		this.tag = tag;
		this.nextType = nextType;
	}
	
	@Deprecated
	public DomainPosition(int offset, int length, String type) {
		super(offset, length, type);
		// ‚±‚ê‚Í‹@”\’âŽ~
		throw new UnsupportedOperationException();
	}

	@Deprecated
	public DomainPosition(ITypedRegion region) {
		super(region);
		// ‚±‚ê‚Í‹@”\’âŽ~
		throw new UnsupportedOperationException();
	}
	
	
	public DomainTag getTag() {
		return tag;
	}

	public void setTag(DomainTag tag) {
		this.tag = tag;
	}

	public String getNextType() {
		return nextType;
	}

	public void setNextType(String nextType) {
		this.nextType = nextType;
	}

	public String getType() {
		return (String) tag.getToken().getData();
	}

	public DomainPosition getPrev() {
		return prev;
	}
	public void setPrev(DomainPosition prev) {
		this.prev = prev;
	}
	
	public int hashCode() {
	 	return super.hashCode() | tag.hashCode() | nextType.hashCode();
	 }

	
}
