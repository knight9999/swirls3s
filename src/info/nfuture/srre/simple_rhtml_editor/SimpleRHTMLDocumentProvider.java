package info.nfuture.srre.simple_rhtml_editor;

import info.nfuture.eclipse.lib.DomainDocument;
import info.nfuture.eclipse.lib.DomainPartitioner;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import org.eclipse.ui.IFileEditorInput;
import org.eclipse.core.runtime.IPath;

public class SimpleRHTMLDocumentProvider extends FileDocumentProvider {

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner =
				new DomainPartitioner( new SimpleRHTMLPartitionScanner() ,
					new String[] {
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
				} );
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}

	@Override
	protected IDocument createEmptyDocument() {
		return new DomainDocument();
	}

	@Override
	public boolean isDeleted(Object element) {

		if (element instanceof IFileEditorInput) {
			IFileEditorInput input= (IFileEditorInput) element;

			IPath path= input.getFile().getLocation();
			if (path == null)
				return false;

			return !path.toFile().exists();
		}

		return super.isDeleted(element);
	}

	
}
