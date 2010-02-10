/**
 * 
 */
package org.python.pydev.django.wizards;

import java.io.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.python.pydev.ui.wizards.project.PythonProjectWizard;

import org.python.pydev.django.utils.*;

/**
 * Wizard that helps in the creation of a Pydev project configured for Django. 
 * 
 * @author alberto
 *
 */
public class DjangoWizard extends PythonProjectWizard {

	protected DjangoConfigWizardPage djangoProjectPage;
	
	public DjangoWizard() {
		super();
		djangoProjectPage = new DjangoConfigWizardPage("Django Page");
		djangoProjectPage.setTitle("Django");
		djangoProjectPage.setDescription("Set DjangoConfiguration");
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
    	
    	String djangoProjectPath = newProjectHandle.getLocation().toString();
    	
    	if(projectPage.shouldCreatSourceFolder()) {
    		djangoProjectPath += "/src";
    	} else {
    		djangoProjectPath += "/" + newProjectHandle.getName();
    		if (!(new File(djangoProjectPath)).mkdir())
    			// TODO: implement fail method
    			System.out.println("Fail");
    	}
    	///home/flaper87/.virtualenvs/r2d2/lib/python2.6/site-packages/django
    	String templates_dir = djangoProjectPage.getProjectTemplateDir();
    	
    	File dir = new File(templates_dir);

    	String[] files = dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".py");
            }
        });

    	try {
	    	for (int i=0; i<files.length; i++) {
	    		System.out.println(files[i]);
	    		Utils.copy( templates_dir + files[i], djangoProjectPath + "/" + files[i]);
	    	}
    	} catch (Exception e) {
			// TODO: implement fail method    		
    		newProjectHandle.getLocation().toString();
    		e.printStackTrace();
		}
    }
}
