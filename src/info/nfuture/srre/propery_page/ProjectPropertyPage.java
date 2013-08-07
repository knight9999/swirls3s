package info.nfuture.srre.propery_page;

import info.nfuture.srre.Activator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class ProjectPropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static String RAILS_PROJECT_PROPERTY_KEY = "RAILS_PROJECT_PROPERTY";
	private Group gr = null;
	private Button btn1 = null;
	private Button btn2 = null;
	private Button btn3 = null;
	
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
//		GridLayout layout = new GridLayout();
//		layout.numColumns = 2;
//		composite.setLayout(layout);
//		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new RowLayout());
		
		Label label = new Label(composite,SWT.NONE);
		label.setText("RAILS Project:");
		
		gr = new Group(composite, SWT.NONE);
		gr.setLayout(new RowLayout());
		btn1 = new Button(gr,SWT.RADIO);
		btn1.setText("AUTO");
		btn2 = new Button(gr,SWT.RADIO);
		btn2.setText("RAILS");
		btn3 = new Button(gr,SWT.RADIO);
		btn3.setText("NO RAILS");
		
		String modeStr = null;
		try {
			IProject project = (IProject)getElement();
			modeStr = project.getPersistentProperty(
				new QualifiedName(Activator.PLUGIN_ID,RAILS_PROJECT_PROPERTY_KEY)
			);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (modeStr==null || modeStr.equals("1") ) {
			btn1.setSelection(true);
		} else if (modeStr.equals("2")) {
			btn2.setSelection(true);
		} else {
			btn3.setSelection(true);
		}
		
		return composite;
	}

	@Override
	protected void performDefaults() {
		btn1.setSelection(true);
		btn2.setSelection(false);
		btn3.setSelection(false);
	}

	@Override
	public boolean performOk() {
		String modeStr = null;
		if (btn1.getSelection()) {
			modeStr = "1";
		} else if (btn2.getSelection()) {
			modeStr = "2";
		} else {
			modeStr = "3";
		}
		IProject project = null;
		try {
			project = (IProject)getElement();
			project.setPersistentProperty(
				new QualifiedName(Activator.PLUGIN_ID,RAILS_PROJECT_PROPERTY_KEY),modeStr
			);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		ProjectLabelDecorator decorator = getProjectLabelDecorator();
		if (decorator != null) {
			decorator.fireLabelEvent(
					new LabelProviderChangedEvent(decorator,project)
			);
		}
		
		
		return true;
	}
	
	public static ProjectLabelDecorator getProjectLabelDecorator() {
		IDecoratorManager decoratorManager =
			Activator.getDefault().getWorkbench().getDecoratorManager();
		ProjectLabelDecorator decorator = (ProjectLabelDecorator) decoratorManager.getBaseLabelProvider(
				"swirls3.decorator1"
		);
		return decorator;
	}
	

}


/*
ラベル装飾（デコレーター）を使います。

参考ページ：http://www13.plala.or.jp/observe/PDE/PDEDecorator.html

拡張ポイント：org.eclipse.ui.decorators

objectClassとしては、
org.eclipse.core.resources.IProject
とする。

iconは、プログラム中で指定するので、宣言的には指定しなくてＯＫ

*/


/*
 * 
参考 Understanding Decorators in Eclipse
http://www.eclipse.org/articles/Article-Decorators/decorators.html 
参考１
http://www.ibm.com/developerworks/jp/opensource/library/os-echsql2/index.html
参考２
http://www.eclipsezone.com/eclipse/forums/t89119.html
*/