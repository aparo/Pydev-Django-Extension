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
 * @author alberto
 *
 */
public class DjangoWizard extends PythonProjectWizard {

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
