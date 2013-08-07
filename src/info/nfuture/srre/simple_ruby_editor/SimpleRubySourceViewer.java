package info.nfuture.srre.simple_ruby_editor;

import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class SimpleRubySourceViewer extends SourceViewer {
	protected SimpleRubyTabSetting tabSetting;
	protected int nextChar = -1;
	
	public SimpleRubySourceViewer(Composite parent, IVerticalRuler ruler,
			int styles,SimpleRubyTabSetting ts) {
		super(parent, ruler, styles);
		tabSetting = ts;
		setVerifyKeyListener();
		
	}

	public SimpleRubySourceViewer(Composite parent,
			IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
			boolean showAnnotationsOverview, int styles,SimpleRubyTabSetting ts) {
		super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles);
		tabSetting = ts;
		setVerifyKeyListener();
	}

	public void setVerifyKeyListener() {
		getTextWidget().addVerifyKeyListener( new VerifyKeyListener() {

			public void verifyKey(VerifyEvent event) {
				VerifyEvent e = event;
				int keyCode = e.keyCode;
				if (keyCode == SWT.ARROW_DOWN || keyCode == SWT.ARROW_UP ||
						keyCode == SWT.ARROW_LEFT || keyCode == SWT.ARROW_RIGHT ) {
//					System.out.println(e.toString());
					setNextChar( -1 );
				}
			}

		});
	}
	
	protected void shift(boolean useDefaultPrefixes, boolean right, boolean ignoreWhitespace) {
		if (fUndoManager != null)
			fUndoManager.beginCompoundChange();
		
		IDocument d= getDocument();
		Map partitioners= null;
		DocumentRewriteSession rewriteSession= null;
		try {
			Point selection= getSelectedRange();
			IRegion block= getTextBlockFromSelection(selection);
			ITypedRegion[] regions= TextUtilities.computePartitioning(d, getDocumentPartitioning(), block.getOffset(), block.getLength(), false);

			int lineCount= 0;
			int[] lines= new int[regions.length * 2]; // [start line, end line, start line, end line, ...]
			for (int i= 0, j= 0; i < regions.length; i++, j+= 2) {
				// start line of region
				lines[j]= getFirstCompleteLineOfRegion(regions[i]);
				// end line of region
				int length= regions[i].getLength();
				int offset= regions[i].getOffset() + length;
				if (length > 0)
					offset--;
				lines[j + 1]= (lines[j] == -1 ? -1 : d.getLineOfOffset(offset));
				lineCount += lines[j + 1] - lines[j] + 1;
			}

			if (d instanceof IDocumentExtension4) {
				IDocumentExtension4 extension= (IDocumentExtension4) d;
				rewriteSession= extension.startRewriteSession(DocumentRewriteSessionType.SEQUENTIAL);
			} else {
				setRedraw(false);
				startSequentialRewriteMode(true);
			}
			if (lineCount >= 20)
				partitioners= TextUtilities.removeDocumentPartitioners(d);

			// Perform the shift operation.
			Map map= (useDefaultPrefixes ? fDefaultPrefixChars : fIndentChars);
				for (int i= 0, j= 0; i < regions.length; i++, j += 2) {
				String[] prefixes= (String[]) selectContentTypePlugin(regions[i].getType(), map);
				if (prefixes != null && prefixes.length > 0 && lines[j] >= 0 && lines[j + 1] >= 0) {
					if (right) {
						if (getTabSetting().isTabsToSpacesConversionEnabled()) {
							shiftRight(lines[j], lines[j + 1], prefixes[getTabSetting().getTabWidth()]); 
						} else {
							shiftRight(lines[j], lines[j + 1], prefixes[0]);
						}
					} else
						shiftLeft(lines[j], lines[j + 1], prefixes, ignoreWhitespace);
				}
			}

		} catch (BadLocationException x) {
			if (TRACE_ERRORS)
				System.out.println( "TextViewer.error.bad_location.shift_1" ); //$NON-NLS-1$

		} finally {

			if (partitioners != null)
				TextUtilities.addDocumentPartitioners(d, partitioners);
			
			if (d instanceof IDocumentExtension4) {
				IDocumentExtension4 extension= (IDocumentExtension4) d;
				extension.stopRewriteSession(rewriteSession);
			} else {
				stopSequentialRewriteMode();
				setRedraw(true);
			}

			if (fUndoManager != null)
				fUndoManager.endCompoundChange();
		}
	}

	// 以下、shiftメソッドを実装するのに必要なメソッド
	// (TextViewerから、そっくりコピー)

	private Object selectContentTypePlugin(String type, Map plugins) {

		if (plugins == null)
			return null;

		return plugins.get(type);
	}

	
	private int getFirstCompleteLineOfRegion(IRegion region) {

		try {

			IDocument d= getDocument();

			int startLine= d.getLineOfOffset(region.getOffset());

			int offset= d.getLineOffset(startLine);
			if (offset >= region.getOffset())
				return startLine;

			offset= d.getLineOffset(startLine + 1);
			return (offset > region.getOffset() + region.getLength() ? -1 : startLine + 1);

		} catch (BadLocationException x) {
			if (TRACE_ERRORS)
				System.out.println( "TextViewer.error.bad_location.getFirstCompleteLineOfRegion"); //$NON-NLS-1$
		}

		return -1;
	}

	private IRegion getTextBlockFromSelection(Point selection) {

		try {
			IDocument document= getDocument();
			IRegion line= document.getLineInformationOfOffset(selection.x);
			int length= selection.y == 0 ? line.getLength() : selection.y + (selection.x - line.getOffset());
			return new Region(line.getOffset(), length);

		} catch (BadLocationException x) {
		}

		return null;
	}

	private void shiftRight(int startLine, int endLine, String prefix) {

		try {

			IDocument d= getDocument();
			while (startLine <= endLine) {
				d.replace(d.getLineOffset(startLine++), 0, prefix);
			}

		} catch (BadLocationException x) {
			if (TRACE_ERRORS)
				System.out.println("TextViewer.shiftRight: BadLocationException"); //$NON-NLS-1$
		}
	}

	private void shiftLeft(int startLine, int endLine, String[] prefixes, boolean ignoreWhitespace) {

		IDocument d= getDocument();

		try {

			IRegion[] occurrences= new IRegion[endLine - startLine + 1];

			// find all the first occurrences of prefix in the given lines
			for (int i= 0; i < occurrences.length; i++) {

				IRegion line= d.getLineInformation(startLine + i);
				String text= d.get(line.getOffset(), line.getLength());

				int index= -1;
				int[] found= TextUtilities.indexOf(prefixes, text, 0);
				if (found[0] != -1) {
					if (ignoreWhitespace) {
						String s= d.get(line.getOffset(), found[0]);
						s= s.trim();
						if (s.length() == 0)
							index= line.getOffset() + found[0];
					} else if (found[0] == 0)
						index= line.getOffset();
				}

				if (index > -1) {
					// remember where prefix is in line, so that it can be removed
					int length= prefixes[found[1]].length();
					if (length == 0 && !ignoreWhitespace && line.getLength() > 0) {
						// found a non-empty line which cannot be shifted
						return;
					}
					occurrences[i]= new Region(index, length);
				} else {
					// found a line which cannot be shifted
					return;
				}
			}

			// OK - change the document
			int decrement= 0;
			for (int i= 0; i < occurrences.length; i++) {
				IRegion r= occurrences[i];
				d.replace(r.getOffset() - decrement, r.getLength(), ""); //$NON-NLS-1$
				decrement += r.getLength();
			}

		} catch (BadLocationException x) {
			if (TRACE_ERRORS)
				System.out.println("TextViewer.shiftLeft: BadLocationException"); //$NON-NLS-1$
		}
	}

	public int getNextChar() {
		return nextChar;
	}

	public void setNextChar(int nextChar) {
		this.nextChar = nextChar;
	}

	public SimpleRubyTabSetting getTabSetting() {
		return tabSetting;
	}

	public void setTabSetting(SimpleRubyTabSetting tabSetting) {
		this.tabSetting = tabSetting;
	}



}
