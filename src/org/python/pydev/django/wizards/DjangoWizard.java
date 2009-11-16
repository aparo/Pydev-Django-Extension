/**
 * 
 */
package org.python.pydev.django.wizards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.python.pydev.core.ICallback;
import org.python.pydev.plugin.PyStructureConfigHelpers;
import org.python.pydev.ui.wizards.project.IWizardNewProjectNameAndLocationPage;
import org.python.pydev.ui.wizards.project.NewProjectNameAndLocationWizardPage;
import org.python.pydev.ui.wizards.project.PythonProjectWizard;

/**
 * Wizard that helps in the creation of a Pydev project configured for Django. 
 * 
 * @author alberto
 *
 */
public class DjangoWizard extends PythonProjectWizard {
    private DjangoConfigWizardPage djangoConfigWizardPage;

    /**
     * Add wizard pages to the instance
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages(){
        addPage(projectPage);

        djangoConfigWizardPage = new DjangoConfigWizardPage("Django Page");
        djangoConfigWizardPage.setTitle("Django");
        djangoConfigWizardPage.setDescription("Set DjangoConfiguration");
        addPage(djangoConfigWizardPage);        
    }
    
    
    /**
     * Creates the project page.
     */
    protected IWizardNewProjectNameAndLocationPage createProjectPage(){
        return new NewProjectNameAndLocationWizardPage("Setting project properties"){
            
            @Override
            public void createControl(Composite parent){
                super.createControl(parent);
                checkSrcFolder.setVisible(false);
            }
            
            @Override
            public boolean shouldCreatSourceFolder(){
                return true; //Always start a Django with a source folder (we need it for the templates)
            }
        };
    }

    /**
     * Overridden to add the external source folders from Django. 
     */
    protected void createAndConfigProject(final IProject newProjectHandle, final IProjectDescription description,
            final String projectType, final String projectInterpreter, IProgressMonitor monitor) throws CoreException{
        ICallback<List<IFolder>, IProject> getSourceFolderHandlesCallback = new ICallback<List<IFolder>, IProject>(){

            public List<IFolder> call(IProject projectHandle){
                if(projectPage.shouldCreatSourceFolder()){
                    IFolder folder = projectHandle.getFolder("src");
                    List<IFolder> ret = new ArrayList<IFolder>();
                    ret.add(folder);
                    return ret;
                }
                return null;
            }
        };

        ICallback<List<String>, IProject> getExternalSourceFolderHandlesCallback = new ICallback<List<String>, IProject>(){
        
            public List<String> call(IProject projectHandle){
                return djangoConfigWizardPage.getExternalSourceFolders();
            }
        };
        
        ICallback<Map<String, String>, IProject> getVariableSubstitutionCallback = new ICallback<Map<String, String>, IProject>(){
            
            public Map<String, String> call(IProject projectHandle){
                return djangoConfigWizardPage.getVariableSubstitution(); 
            }
        };
        
        PyStructureConfigHelpers.createPydevProject(description, newProjectHandle, monitor, projectType,
                projectInterpreter, getSourceFolderHandlesCallback, getExternalSourceFolderHandlesCallback,
                getVariableSubstitutionCallback);
        
        //Ok, after the default is created, let's see if we have a template...
        IFolder sourceFolder = newProjectHandle.getFolder("src");
    }

}
