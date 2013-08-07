package info.nfuture.srre.simple_ruby_editor;

import info.nfuture.eclipse.lib.DomainDocument;
import info.nfuture.eclipse.lib.DomainPartitioner;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import org.eclipse.ui.IFileEditorInput;
import org.eclipse.core.runtime.IPath;

public class SimpleRubyDocumentProvider extends FileDocumentProvider {

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner =
				new DomainPartitioner( new SimpleRubyPartitionScanner() ,
					SimpleRubyPartitionScanner.contentTypes().toArray(new String[0])
//					new String[] {  
//					SimpleRubyPartitionScanner.COMMENT ,
//					SimpleRubyPartitionScanner.LINE_COMMENT ,
//					SimpleRubyPartitionScanner.RUBY_STRING ,
//					SimpleRubyPartitionScanner.RUBY_STRING_STATIC ,
//					SimpleRubyPartitionScanner.RUBY_REG_EXP,
//					SimpleRubyPartitionScanner.RUBY_REG_EXP2,
//					SimpleRubyPartitionScanner.INNER_RUBY ,
//					SimpleRubyPartitionScanner.HEREDOCUMENT ,
//					SimpleRubyPartitionScanner.HEREDOCUMENT_STATIC 
//				} 
				);
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
