package info.nfuture.srre.propery_page;

import info.nfuture.srre.Activator;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.widgets.Display;

public class ProjectLabelDecorator 
	extends LabelProvider
	implements ILightweightLabelDecorator  
{

	
	public void showListforDebug(IResource resList[]) {
		for (int i=0;i<resList.length;i++) {
			IResource res = resList[i];
			if (res != null) {
				if (res instanceof IProject) {
					System.out.println("Project : " + res.getName());
				} else if (res instanceof IFolder) {
					System.out.println("Folder : " + res.getName());
				} else if (res instanceof IFile) {
					System.out.println("File : " + res.getName());
				} else {
					System.out.println("other :" + res.getName());
				}
			}
		}
	}
	
	
	
	public void decorate(Object element, IDecoration decoration) {
	    if (element instanceof IProject) { // IProject型のはず
	    	IProject project = (IProject) element;

	    	boolean flagAuto = false;
	    	// project配下のファイルをチェック(ネストはチェックしない)
	    	try {
				IResource [] resList = project.members();
				IFolder fol = project.getFolder("config");
				if (fol != null) {
					IFile file = fol.getFile("environment.rb");
					if ( file.exists() ) {
//						System.out.println("FILE FOUND!");
						flagAuto = true;
					} else {
//						System.out.println("FILE NOT FOUND!");
					}
				}
//				showListforDebug(resList);
			} catch (ResourceException e2) {
				// ソースが閉じている場合
				return;
	    	} catch (CoreException e1) {
				e1.printStackTrace();
			}
	    	
	    	String modeStr = null;
	    	try {
	    		modeStr = project.getPersistentProperty(
					new QualifiedName(Activator.PLUGIN_ID,ProjectPropertyPage.RAILS_PROJECT_PROPERTY_KEY)
				);
			} catch (ResourceException e2) {
				// ソースが閉じている場合
				return;
	    	} catch (CoreException e) {
				e.printStackTrace();
			}
	    	if (modeStr==null || modeStr.equals("1")) {
	    		if (flagAuto == true) {
	    			// 自動判定の場合
	    			railsDecoration(decoration);
	    		}
	    	} else if (modeStr.equals("2")) {
    			// 手動設定の場合
	    		railsDecoration(decoration);
	    	} else {
	    		
	    	}
	    	
	    }

	}
	
	public void railsDecoration(IDecoration decoration) {
        ImageDescriptor desc =
	          ImageDescriptor.createFromFile(
	            ProjectLabelDecorator.class,
	            "rails.gif");
	        decoration.addOverlay(desc);
//	        decoration.addPrefix("Rails ");
//	        decoration.addSuffix(" (RAILS) ");
	}
	
	public void fireLabelEvent(final LabelProviderChangedEvent event) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				fireLabelProviderChanged(event);
			}
		});
		
	}



}
