package info.nfuture.srre.simple_ruby_editor;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;

public class SimpleRubyEditor extends TextEditor {
	protected SimpleRubyTabSetting tabSetting;
	
	public SimpleRubyEditor() {
		super();
		tabSetting = new SimpleRubyTabSetting(true,2);
		setSourceViewerConfiguration(new SimpleRubyConfiguration(tabSetting) );
		setDocumentProvider(new SimpleRubyDocumentProvider());
	}

	@Override
	protected boolean isTabsToSpacesConversionEnabled() {
		return tabSetting.isTabsToSpacesConversionEnabled();
	}
	
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {

		fAnnotationAccess= getAnnotationAccess();
		fOverviewRuler= createOverviewRuler(getSharedColors());

		ISourceViewer viewer= new SimpleRubySourceViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles,tabSetting);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}

}
