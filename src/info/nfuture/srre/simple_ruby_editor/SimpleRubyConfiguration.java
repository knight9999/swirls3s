package info.nfuture.srre.simple_ruby_editor;

import info.nfuture.srre.simple_ruby_editor.scanners.CommentScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.HereDocumentScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.HereDocumentStaticScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.InnerRubyScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.LineCommentScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.RubyRegExpScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.RubyScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.RubyStringScanner;
import info.nfuture.srre.simple_ruby_editor.scanners.RubyStringStaticScanner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class SimpleRubyConfiguration extends TextSourceViewerConfiguration {
	protected SimpleRubyTabSetting tabSetting; 
	
	private RuleBasedScanner defaultScanner;
	private CommentScanner commentScanner;
	private LineCommentScanner lineCommentScanner;
	private RubyStringScanner rubyStringScanner;
	private RubyStringStaticScanner rubyStringStaticScanner;
	private RubyRegExpScanner rubyRegExpScanner;
	private InnerRubyScanner innerRubyScanner;
	private HereDocumentScanner hereDocumentScanner;
	private HereDocumentStaticScanner hereDocumentStaticScanner;
	
	
	public SimpleRubyConfiguration(SimpleRubyTabSetting ts) {
		tabSetting = ts;
	}
	
	protected RuleBasedScanner getDefaultScanner() {
		if (defaultScanner == null) {
			defaultScanner = new RubyScanner();
		}
		return defaultScanner;
	}

	protected CommentScanner getCommentScanner() {
		if (commentScanner == null) {
			commentScanner = new CommentScanner();
		}
		return commentScanner;
	}
	
	protected LineCommentScanner getLineCommentScanner() {
		if (lineCommentScanner == null) {
			lineCommentScanner = new LineCommentScanner();
		}
		return lineCommentScanner;
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

	protected RubyRegExpScanner getRubyRegExpScanner() {
		if (rubyRegExpScanner == null) {
			rubyRegExpScanner = new RubyRegExpScanner();
		}
		return rubyRegExpScanner;
	}

	protected InnerRubyScanner getInnerRubyScanner() {
		if (innerRubyScanner == null) {
			innerRubyScanner = new InnerRubyScanner();
		}
		return innerRubyScanner;
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

		dr = new DefaultDamagerRepairer(getCommentScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.COMMENT);
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.COMMENT);

		dr = new DefaultDamagerRepairer(getLineCommentScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.LINE_COMMENT);
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.LINE_COMMENT);

		dr = new DefaultDamagerRepairer(getRubyStringScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.RUBY_STRING);
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.RUBY_STRING);

		dr = new DefaultDamagerRepairer(getRubyStringStaticScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.RUBY_STRING_STATIC);
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.RUBY_STRING_STATIC);

		dr = new DefaultDamagerRepairer(getRubyRegExpScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.RUBY_REG_EXP);
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.RUBY_REG_EXP);

//		dr = new DefaultDamagerRepairer(getRubyRegExpScanner());
//		reconciler.setDamager(dr, SimpleRubyPartitionScanner.RUBY_REG_EXP2);
//		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.RUBY_REG_EXP2);

		dr = new DefaultDamagerRepairer(getInnerRubyScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.INNER_RUBY);
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.INNER_RUBY);

		dr = new DefaultDamagerRepairer(getHereDocumentScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.HEREDOCUMENT);
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.HEREDOCUMENT);

		dr = new DefaultDamagerRepairer(getHereDocumentStaticScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.HEREDOCUMENT_STATIC);
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.HEREDOCUMENT_STATIC);

		dr = new DefaultDamagerRepairer(getRubyStringScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.RUBY_STRING_VAL0 );
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.RUBY_STRING_VAL0 );
	
		dr = new DefaultDamagerRepairer(getRubyStringScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.RUBY_STRING_VAL );
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.RUBY_STRING_VAL );

		dr = new DefaultDamagerRepairer(getRubyStringStaticScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.RUBY_STRING_STATIC_VAL );
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.RUBY_STRING_STATIC_VAL );

		dr = new DefaultDamagerRepairer(getRubyRegExpScanner());
		reconciler.setDamager(dr, SimpleRubyPartitionScanner.RUBY_REG_EXP_VAL );
		reconciler.setRepairer(dr, SimpleRubyPartitionScanner.RUBY_REG_EXP_VAL );
		
		return reconciler;
	}



	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		List<String> list = new ArrayList<String>();
		list.add( IDocument.DEFAULT_CONTENT_TYPE);
		list.addAll( SimpleRubyPartitionScanner.contentTypes() );
		return list.toArray(new String[0]);
/*		
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE ,
			SimpleRubyPartitionScanner.COMMENT ,
			SimpleRubyPartitionScanner.LINE_COMMENT ,
			SimpleRubyPartitionScanner.RUBY_STRING ,
			SimpleRubyPartitionScanner.RUBY_STRING_STATIC ,
			SimpleRubyPartitionScanner.RUBY_REG_EXP,
			SimpleRubyPartitionScanner.INNER_RUBY ,
			SimpleRubyPartitionScanner.HEREDOCUMENT ,
			SimpleRubyPartitionScanner.HEREDOCUMENT_STATIC 
		};
*/
	}

	@Override
	public int getTabWidth(ISourceViewer sourceViewer) {
		return tabSetting.getTabWidth();
	}

	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
//		IAutoEditStrategy[] parentStrategies = super.getAutoEditStrategies(sourceViewer, contentType);
//		IAutoEditStrategy[] myStrategies = new IAutoEditStrategy[parentStrategies.length+1];
//		System.arraycopy(parentStrategies,0,myStrategies,0,parentStrategies.length);

		IAutoEditStrategy[] myStrategies = new IAutoEditStrategy[1];
		myStrategies[myStrategies.length - 1] = new SimpleRubyAutoEditStrategy( (SimpleRubySourceViewer)sourceViewer);
		return myStrategies;
	}
	
	
	
}
