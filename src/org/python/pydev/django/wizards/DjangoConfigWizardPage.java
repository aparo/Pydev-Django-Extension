/**
 * 
 */
package org.python.pydev.django.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * @author alberto
 *
 */
public class DjangoConfigWizardPage extends WizardPage {

    private final List<String> externalSourceFolders = new ArrayList<String>();
    
    private final Map<String, String> variableSubstitution = new HashMap<String, String>();
	
	protected DjangoConfigWizardPage(String pageName) {
		super(pageName);
        this.setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		
	}

    public List<String> getExternalSourceFolders(){
        return externalSourceFolders;
    }

    public Map<String, String> getVariableSubstitution(){
        return variableSubstitution;
    }
}
