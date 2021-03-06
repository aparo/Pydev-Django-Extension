/**
 * 
 */
package org.python.pydev.django.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author alberto
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class DjangoConfigWizardPage extends WizardPage {
	private Text djangoLocationPathField;
	
	private Label djangoLocationLabel;
	
	public Button radio11;
	
	public Button radio10;
	
	public Button radio09;
	
	private Button browseButton;
	
	private String djangoSrcPath;
	
	private Listener locationModifyListener = new Listener() {
        public void handleEvent(Event e) {
            setPageComplete(validateDjangoPage());
        }
    };

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;
	
    private final List<String> externalSourceFolders = new ArrayList<String>();
    
    private final Map<String, String> variableSubstitution = new HashMap<String, String>();
	
	protected DjangoConfigWizardPage(String pageName) {
		super(pageName);
        this.setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setFont(parent.getFont());
        
        createDjangoLocationGroup(composite);
        
        createDjangoVersionGroup(composite);

        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
	}
	
	public void createDjangoLocationGroup(Composite parent) {
        Font font = parent.getFont();
        // project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);

        // new project label
        Label projectContentsLabel = new Label(projectGroup, SWT.NONE);
        projectContentsLabel.setFont(font);
        
        projectContentsLabel.setText("Select Django Path:");

        GridData labelData = new GridData();
        labelData.horizontalSpan = 3;
        projectContentsLabel.setLayoutData(labelData);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        
        // location label
        djangoLocationLabel = new Label(projectGroup, SWT.NONE);
        djangoLocationLabel.setFont(font);
        djangoLocationLabel.setText("Director&y");
        djangoLocationLabel.setEnabled(true);

        // project location entry field
        djangoLocationPathField = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        djangoLocationPathField.setLayoutData(data);
        djangoLocationPathField.setFont(font);
        djangoLocationPathField.setEnabled(true);

        djangoLocationPathField.addListener(SWT.Modify, locationModifyListener);
        // browse button
        browseButton = new Button(projectGroup, SWT.PUSH);
        browseButton.setFont(font);
        browseButton.setText("B&rowse");
        browseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
            	handleDjangoLocationBrowseButtonPressed();
            	setPageComplete(validateDjangoPage());
            }
        });

        browseButton.setEnabled(true);        
	}

	public void createDjangoVersionGroup(Composite parent) {
        Font font = parent.getFont();
        // project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);
        
      //Project type
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        Group group = new Group(projectGroup, SWT.NONE);
        group.setText("Choose the django project version");
        group.setLayout(layout);
        //gd= new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        
        radio11 = new Button(group, SWT.RADIO | SWT.LEFT);
        radio11.setText("1.1.1");
        
        radio10 = new Button(group, SWT.RADIO | SWT.LEFT);
        radio10.setText("1.0");
        
        
        radio09 = new Button(group, SWT.RADIO | SWT.LEFT);
        radio09.setText("0.9.6");
        
	}
	
    /**
     * @param composite

    private void createProjectDetails(Composite parent) {
        Font font = parent.getFont();
        Composite projectDetails = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        projectDetails.setLayout(layout);
        projectDetails.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectDetails.setFont(font);

        Label projectTypeLabel = new Label(projectDetails, SWT.NONE);
        projectTypeLabel.setFont(font);
        projectTypeLabel.setText("Project type");
        //let him choose the type of the project
        details = new DjangoProjectDetails.DjangoVersionAndConf(new ICallback(){

            //Whenever the configuration changes there, we must evaluate whether the page is complete
            public Object call(Object args) throws Exception {
                setPageComplete(NewProjectNameAndLocationWizardPage.this.validatePage());
                return null;
            }}
        );
        
        Control createdOn = details.doCreateContents(projectDetails);
        details.setDefaultSelection();
        GridData data=new GridData(GridData.FILL_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        createdOn.setLayoutData(data);
    }
     */

    public List<String> getExternalSourceFolders(){
        return externalSourceFolders;
    }

    public Map<String, String> getVariableSubstitution(){
        return variableSubstitution;
    }
    
    /**
     *  Open an appropriate directory browser
     */
    private void handleDjangoLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(djangoLocationPathField.getShell());
        dialog.setMessage("Select Django Installation Path");

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            djangoLocationPathField.setText(selectedDirectory);
        }
    }
    
    private boolean validateDjangoPage() {
    	if (djangoLocationPathField == null) {
    		setMessage("Select django installation path.");
            return false;
    	} else {
    		String loc = djangoLocationPathField.getText() + "/core/__init__.py";
    		File chkDir = new File(loc);
    		if (!chkDir.exists()) {
    			setErrorMessage("Select the right django path");
    			return false;
    		} else {
    			djangoSrcPath = djangoLocationPathField.getText();
    		}
    	}
    	
    	setErrorMessage(null);
    	setMessage(null);
    	return true;
    }
    
    /**
     * Returns the django project template dir.
     */
    public String getProjectTemplateDir() {
    	return djangoSrcPath + "/conf/project_template/";
    }
}
