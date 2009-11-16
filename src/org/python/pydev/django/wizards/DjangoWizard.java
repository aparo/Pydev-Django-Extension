/**
 * 
 */
package org.python.pydev.django.wizards;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.python.core.PyObject;
import org.python.pydev.core.ICallback;
import org.python.pydev.plugin.PyStructureConfigHelpers;
import org.python.pydev.plugin.PydevPlugin;
import org.python.pydev.ui.perspective.PythonPerspectiveFactory;
import org.python.pydev.ui.wizards.gettingstarted.AbstractNewProjectWizard;
import org.python.pydev.ui.wizards.project.PythonProjectWizard;


import org.python.pydev.jython.*;

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

	protected DjangoConfigWizardPage djangoProjectPage;
	
	public DjangoWizard() {
		super();
		djangoProjectPage = new DjangoConfigWizardPage("Django Installation Settings:");
	}
	/**
     * Add django wizard pages to the instance
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        super.addPages();
        addPage(djangoProjectPage);
    }
    
    /**
     * Overrides the PyDev createAndConfigProject method.
     * 
     * After calling the parent method executes a system call to django-admin.py script to start a new project
     * in the project location path.
     */
    protected void createAndConfigProject(final IProject newProjectHandle, final IProjectDescription description,
            final String projectType, final String projectInterpreter, IProgressMonitor monitor)
            throws CoreException{
    	super.createAndConfigProject(newProjectHandle, description, projectType, projectInterpreter, monitor);
    	
    	System.out.println(System.getenv("PYTHONPATH"));
    	
    	String djangoProjectPath = newProjectHandle.getLocation().toString();
    	
    	if(projectPage.shouldCreatSourceFolder()) {
    		djangoProjectPath += "src";
    	} else {
    		djangoProjectPath += newProjectHandle.getName();
    	}
    }
}
