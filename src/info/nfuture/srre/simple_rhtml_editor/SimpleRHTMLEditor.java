package info.nfuture.srre.simple_rhtml_editor;

import org.eclipse.ui.editors.text.TextEditor;

public class SimpleRHTMLEditor extends TextEditor {
	public SimpleRHTMLEditor() {
		super();
		setSourceViewerConfiguration(new SimpleRHTMLConfiguration());
		setDocumentProvider(new SimpleRHTMLDocumentProvider());
	}
}
