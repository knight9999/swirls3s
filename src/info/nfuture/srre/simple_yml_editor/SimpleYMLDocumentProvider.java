package info.nfuture.srre.simple_yml_editor;

import info.nfuture.eclipse.lib.DomainDocument;
import info.nfuture.eclipse.lib.DomainPartitioner;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class SimpleYMLDocumentProvider extends FileDocumentProvider {

	
	
	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner =
				new DomainPartitioner( new SimpleYMLPartitionScanner() ,
					new String[] {
					SimpleYMLPartitionScanner.TITLE_KEY,
//					SimpleYMLPartitionScanner.NORMAL_KEY,
					SimpleYMLPartitionScanner.SECTION_KEY,
					SimpleYMLPartitionScanner.COMMENT,
					SimpleYMLPartitionScanner.VALUE_KEY
				}
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

	
}
