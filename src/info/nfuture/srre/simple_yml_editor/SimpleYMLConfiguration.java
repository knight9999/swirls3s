package info.nfuture.srre.simple_yml_editor;

import info.nfuture.srre.simple_yml_editor.scanners.CommentScanner;
import info.nfuture.srre.simple_yml_editor.scanners.DefaultScanner;
import info.nfuture.srre.simple_yml_editor.scanners.SectionScanner;
import info.nfuture.srre.simple_yml_editor.scanners.TitleScanner;
import info.nfuture.srre.simple_yml_editor.scanners.ValueScanner;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class SimpleYMLConfiguration extends TextSourceViewerConfiguration {
	private RuleBasedScanner defaultScanner;
	private SectionScanner sectionScanner;
	private TitleScanner titleScanner;
	private ValueScanner valueScanner;
	private CommentScanner commentScanner;
	
	protected RuleBasedScanner getDefaultScanner() {
		if (defaultScanner == null) {
			defaultScanner = new DefaultScanner();
		}
		return defaultScanner;
	}

	protected SectionScanner getSectionScanner() {
		if (sectionScanner == null) {
			sectionScanner = new SectionScanner();
		}
		return sectionScanner;
	}
	
	protected TitleScanner getTitleScanner() {
		if (titleScanner == null) {
			titleScanner = new TitleScanner();
		}
		return titleScanner;
	}
	
	protected ValueScanner getValueScanner() {
		if (valueScanner == null){
			valueScanner = new ValueScanner();
		}
		return valueScanner;
	}
	
	protected CommentScanner getCommentScanner() {
		if (commentScanner == null) {
			commentScanner = new CommentScanner();
		}
		return commentScanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = 
			new PresentationReconciler();
		DefaultDamagerRepairer dr;
		
		dr = new DefaultDamagerRepairer(getDefaultScanner());
		reconciler.setDamager(dr,IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr,IDocument.DEFAULT_CONTENT_TYPE);
		
		dr = new DefaultDamagerRepairer(getSectionScanner());
		reconciler.setDamager(dr,SimpleYMLPartitionScanner.SECTION_KEY);
		reconciler.setRepairer(dr,SimpleYMLPartitionScanner.SECTION_KEY);

		dr = new DefaultDamagerRepairer(getTitleScanner());
		reconciler.setDamager(dr,SimpleYMLPartitionScanner.TITLE_KEY);
		reconciler.setRepairer(dr,SimpleYMLPartitionScanner.TITLE_KEY);
		
		dr = new DefaultDamagerRepairer(getValueScanner());
		reconciler.setDamager(dr,SimpleYMLPartitionScanner.VALUE_KEY);
		reconciler.setRepairer(dr,SimpleYMLPartitionScanner.VALUE_KEY);

		dr = new DefaultDamagerRepairer(getCommentScanner());
		reconciler.setDamager(dr,SimpleYMLPartitionScanner.COMMENT);
		reconciler.setRepairer(dr,SimpleYMLPartitionScanner.COMMENT);

		return reconciler;
	}
	
	
}
