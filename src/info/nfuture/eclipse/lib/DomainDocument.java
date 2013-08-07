package info.nfuture.eclipse.lib;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.AbstractDocument;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.Position;

public class DomainDocument extends Document {
	private String domainPositionCategory;

	
	
	public DomainDocument() {
		super();
	}

	public String getDomainPositionCategory() {
		return domainPositionCategory;
	}

	public void setDomainPositionCategory(String domainPositionCategory) {
		this.domainPositionCategory = domainPositionCategory;
	}

	public DomainPosition [] getDomainPositions() throws BadPositionCategoryException {
		List<DomainPosition> ldp = new ArrayList<DomainPosition>();
		Position [] origPositions = getPositions(domainPositionCategory);
		for (int i=0;i<origPositions.length;i++) {
			Position p = origPositions[i];
			if (p instanceof DomainPosition && ! p.isDeleted() ) {
				ldp.add((DomainPosition) p );
			}
		}
		
		return (DomainPosition []) ldp.toArray(new DomainPosition[0]);
	}

	public void addDomainPosition(DomainPosition position) throws BadLocationException, BadPositionCategoryException {
		addPosition2(domainPositionCategory , position);
		DomainPosition []dps = getDomainPositions();
		for (int i=0;i<dps.length;i++) {
			if (dps[i] == position) {
				if (i>0) {
					dps[i].setPrev( dps[i-1] );
				}
				if (i<dps.length-1) {
					dps[i+1].setPrev( dps[i] );
				}
			}
		}
	}
	
	public void removeDomainPosition(DomainPosition position) throws BadPositionCategoryException {
		DomainPosition []dps = getDomainPositions();
		for (int i=0;i<dps.length;i++) {
			if (dps[i] == position) {
				if (i>0 && i<dps.length-1) {
					dps[i+1].setPrev( dps[i-1] );
				}
			}
		}
		removePosition(domainPositionCategory,position);
	}
	
	
	// •À‚Ñ‘Ö‚¦‚ÌÝ’è•ÏX offset‚ª“¯‚¶ê‡‚ÉAlength==0‚Ì‚à‚Ì‚ª‘O‚É—ˆ‚é //
	
	
	public void addPosition2(String category, Position position) throws BadLocationException, BadPositionCategoryException  {

		Map fPositions = getDocumentManagedPositions();
		Map fEndPositions = null;
		try {
			Field field = null;
			field = getClass().getSuperclass().getSuperclass().getDeclaredField("fEndPositions");
			field.setAccessible(true);
			fEndPositions = (Map) field.get(this);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		if ((0 > position.offset) || (0 > position.length) || (position.offset + position.length > getLength()))
			throw new BadLocationException();

		if (category == null)
			throw new BadPositionCategoryException();

		List list= (List) fPositions.get(category);
		if (list == null)
			throw new BadPositionCategoryException();
		
		int x = computeIndexInPositionList2(list, position.offset);
		
		if (x < list.size()) {
			Position p = (Position)list.get(x);
			if (p.offset == position.offset && p.length == 0) {
			x = x + 1;
			}
		}
		list.add(x, position);
		
		List endPositions= (List) fEndPositions.get(category);
		if (endPositions == null)
			throw new BadPositionCategoryException();
		endPositions.add(computeIndexInPositionList2(endPositions, position.offset + position.length - 1, false), position);
	}
	
	
	protected int computeIndexInPositionList2(List positions, int offset) {
		return computeIndexInPositionList2(positions, offset, true);
	}

	
	protected int computeIndexInPositionList2(List positions, int offset, boolean orderedByOffset) {
		if (positions.size() == 0)
			return 0;
		if (orderedByOffset == false) {
			return super.computeIndexInPositionList(positions, offset,orderedByOffset);
		}
		
		int left= 0;
		int right= positions.size() -1;
		int mid= 0;
		Position p= null;

		while (left < right) {

			mid= (left + right) / 2;

			p= (Position) positions.get(mid);
			int pOffset= getOffset(orderedByOffset, p);
			if (offset < pOffset) {
				if (left == mid)
					right= left;
				else
					right= mid -1;
			} else if (offset > pOffset) {
				if (right == mid)
					left= right;
				else
					left= mid  +1;
			} else if (offset == pOffset) {
				left= right= mid;
			}

		}

		int pos= left;
		p= (Position) positions.get(pos);
		int pPosition= getOffset(orderedByOffset, p);
		if (offset > pPosition) {
			// append to the end
			pos++;
		} else {
			// entry will become the first of all entries with the same offset
			do {
				--pos;
				if (pos < 0)
					break;
				p= (Position) positions.get(pos);
				pPosition= getOffset(orderedByOffset, p);
			} while (offset == pPosition);
			++pos;
		}

		Assert.isTrue(0 <= pos && pos <= positions.size());

		return pos;
	}

	private int getOffset(boolean orderedByOffset, Position position) {
		if (orderedByOffset || position.getLength() == 0)
			return position.getOffset();
		return position.getOffset() + position.getLength() - 1;
	}

}
