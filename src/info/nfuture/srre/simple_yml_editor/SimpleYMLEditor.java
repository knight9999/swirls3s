package info.nfuture.srre.simple_yml_editor;
import org.eclipse.ui.editors.text.TextEditor;


public class SimpleYMLEditor extends TextEditor {

	public SimpleYMLEditor() {
		super();
		setSourceViewerConfiguration(new SimpleYMLConfiguration());
		setDocumentProvider(new SimpleYMLDocumentProvider());
	}

}
