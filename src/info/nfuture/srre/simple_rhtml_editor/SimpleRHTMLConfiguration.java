package info.nfuture.srre.simple_rhtml_editor;

import info.nfuture.srre.simple_rhtml_editor.scanners.DefaultScanner;
import info.nfuture.srre.simple_rhtml_editor.scanners.HtmlCommentScanner;
import info.nfuture.srre.simple_rhtml_editor.scanners.HtmlTagScanner;
import info.nfuture.srre.simple_rhtml_editor.scanners.HtmlValueScanner;
import info.nfuture.srre.simple_rhtml_editor.scanners.JavascriptScanner;
import info.nfuture.srre.simple_rhtml_editor.scanners.RubyScanner;
import info.nfuture.srre.simple_rhtml_editor.scanners.StylesheetScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.HereDocumentScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.HereDocumentStaticScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.RubyStringScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.RubyStringStaticScanner;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class SimpleRHTMLConfiguration extends TextSourceViewerConfiguration {
	private RuleBasedScanner defaultScanner;
	private HtmlCommentScanner htmlCommentScanner;
	private HtmlTagScanner htmlTagScanner;
	private HtmlValueScanner htmlValueScanner;
	private JavascriptScanner javascriptScanner;
	private StylesheetScanner stylesheetScanner;
	private RubyScanner rubyScanner;
	private RubyStringScanner rubyStringScanner;
	private RubyStringStaticScanner rubyStringStaticScanner;
	private HereDocumentScanner hereDocumentScanner;
	private HereDocumentStaticScanner hereDocumentStaticScanner;
	
	
	protected RuleBasedScanner getDefaultScanner() {
		if (defaultScanner == null){
			defaultScanner = new DefaultScanner();
		}
		return defaultScanner;
	}
	
	protected HtmlCommentScanner getHtmlCommentScanner() {
		if (htmlCommentScanner == null) {
			htmlCommentScanner = new HtmlCommentScanner();
		}
		return htmlCommentScanner;
	}
	
	protected HtmlTagScanner getHtmlTagScanner() {
		if (htmlTagScanner == null) {
			htmlTagScanner = new HtmlTagScanner();
		}
		return htmlTagScanner;
	}

	protected HtmlValueScanner getHtmlValueScanner() {
		if (htmlValueScanner == null) {
			htmlValueScanner = new HtmlValueScanner();
		}
		return htmlValueScanner;
	}
	
	protected JavascriptScanner getJavascriptScanner() {
		if (javascriptScanner == null) {
			javascriptScanner = new JavascriptScanner();
		}
		return javascriptScanner;
	}
	
	protected StylesheetScanner getStylesheetScanner() {
		if (stylesheetScanner == null) {
			stylesheetScanner = new StylesheetScanner();
		}
		return stylesheetScanner;
	}
	
	protected RubyScanner getRubyScanner() {
		if (rubyScanner == null) {
			rubyScanner = new RubyScanner();
		}
		return rubyScanner;
	}
	
	protected RubyStringScanner getRubyStringScanner() {
		if (rubyStringScanner == null) {
			rubyStringScanner = new RubyStringScanner();
		}
		return rubyStringScanner;
	}
	
	protected RubyStringStaticScanner getRubyStringStaticScanner() {
		if (rubyStringStaticScanner == null) {
			rubyStringStaticScanner = new RubyStringStaticScanner();
		}
		return rubyStringStaticScanner;
	}
	
	protected HereDocumentScanner getHereDocumentScanner() {
		if (hereDocumentScanner == null) {
			hereDocumentScanner = new HereDocumentScanner();
		}
		return hereDocumentScanner;
	}

	protected HereDocumentStaticScanner getHereDocumentStaticScanner() {
		if (hereDocumentStaticScanner == null) {
			hereDocumentStaticScanner = new HereDocumentStaticScanner();
		}
		return hereDocumentStaticScanner;
	}

	
	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr;
		
		dr = new DefaultDamagerRepairer(getDefaultScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(getHtmlCommentScanner());
		reconciler.setDamager(dr, SimpleRHTMLPartitionScanner.HTML_COMMENT);
		reconciler.setRepairer(dr, SimpleRHTMLPartitionScanner.HTML_COMMENT);

		dr = new DefaultDamagerRepairer(getHtmlTagScanner());
		reconciler.setDamager(dr, SimpleRHTMLPartitionScanner.HTML_TAG);
		reconciler.setRepairer(dr, SimpleRHTMLPartitionScanner.HTML_TAG);

		dr = new DefaultDamagerRepairer(getHtmlValueScanner());
		reconciler.setDamager(dr, SimpleRHTMLPartitionScanner.HTML_VALUE);
		reconciler.setRepairer(dr, SimpleRHTMLPartitionScanner.HTML_VALUE);

		dr = new DefaultDamagerRepairer(getJavascriptScanner());
		reconciler.setDamager(dr, SimpleRHTMLPartitionScanner.JAVASCRIPT);
		reconciler.setRepairer(dr, SimpleRHTMLPartitionScanner.JAVASCRIPT);

		dr = new DefaultDamagerRepairer(getStylesheetScanner());
		reconciler.setDamager(dr, SimpleRHTMLPartitionScanner.STYLESHEET);
		reconciler.setRepairer(dr, SimpleRHTMLPartitionScanner.STYLESHEET);

		dr = new DefaultDamagerRepairer(getRubyScanner());
		reconciler.setDamager(dr, SimpleRHTMLPartitionScanner.RUBY);
		reconciler.setRepairer(dr, SimpleRHTMLPartitionScanner.RUBY);

		dr = new DefaultDamagerRepairer(getRubyStringScanner());
		reconciler.setDamager(dr, SimpleRHTMLPartitionScanner.RUBY_STRING);
		reconciler.setRepairer(dr, SimpleRHTMLPartitionScanner.RUBY_STRING);

		dr = new DefaultDamagerRepairer(getRubyStringStaticScanner());
		reconciler.setDamager(dr, SimpleRHTMLPartitionScanner.RUBY_STRING_STATIC);
		reconciler.setRepairer(dr, SimpleRHTMLPartitionScanner.RUBY_STRING_STATIC);

		dr = new DefaultDamagerRepairer(getHereDocumentScanner());
		reconciler.setDamager(dr, SimpleRHTMLPartitionScanner.HEREDOCUMENT);
		reconciler.setRepairer(dr, SimpleRHTMLPartitionScanner.HEREDOCUMENT);

		dr = new DefaultDamagerRepairer(getHereDocumentStaticScanner());
		reconciler.setDamager(dr, SimpleRHTMLPartitionScanner.HEREDOCUMENT_STATIC);
		reconciler.setRepairer(dr, SimpleRHTMLPartitionScanner.HEREDOCUMENT_STATIC);

		return reconciler;
	}



	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			SimpleRHTMLPartitionScanner.JAVASCRIPT,
			SimpleRHTMLPartitionScanner.STYLESHEET,
			SimpleRHTMLPartitionScanner.RUBY, 
			SimpleRHTMLPartitionScanner.RUBY_STRING,
			SimpleRHTMLPartitionScanner.RUBY_STRING_STATIC,
			SimpleRHTMLPartitionScanner.HTML_COMMENT,
			SimpleRHTMLPartitionScanner.HTML_TAG,
			SimpleRHTMLPartitionScanner.HTML_VALUE,
			SimpleRHTMLPartitionScanner.HEREDOCUMENT ,
			SimpleRHTMLPartitionScanner.HEREDOCUMENT_STATIC 
		};
	}

	
}
