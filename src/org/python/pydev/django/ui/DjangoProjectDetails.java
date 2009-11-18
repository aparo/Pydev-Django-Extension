package org.python.pydev.django.ui;

import java.util.ArrayList;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.*;
import org.python.pydev.core.IInterpreterInfo;
import org.python.pydev.core.IInterpreterManager;
import org.python.pydev.core.IPythonNature;
import org.python.pydev.plugin.PydevPlugin;
import org.python.pydev.utils.ICallback;

public class DjangoProjectDetails extends PropertyPage {

    /**
     * This  class provides a way to show to the user the options available to configure a project with the
     * correct interpreter and grammar.
     */
    public static class DjangoVersionAndConf{
        private static final String DJANGO_NOT_CONFIGURED_MSG = "<a>Please configure Django Installation to use.</a>";
        public Label versionLabel;
        public Combo interpretersChoice;
        public Combo djangoVersionChoice;
        private Link interpreterNoteText;
        private SelectionListener selectionListener;
        private ICallback onSelectionChanged;
        private Label installationLabel;
        
        public DjangoVersionAndConf() {
            
        }
        
        /**
         * Optionally, a callback may be passed to be called whenever the selection of the project type changes.
         */
        public DjangoVersionAndConf(ICallback callback) {
            this.onSelectionChanged = callback;
        }


        public Control doCreateContents(Composite p) {
            Composite topComp= new Composite(p, SWT.NONE);
            GridLayout innerLayout= new GridLayout();
            innerLayout.numColumns= 1;
            innerLayout.marginHeight= 0;
            innerLayout.marginWidth= 0;
            topComp.setLayout(innerLayout);
            GridData gd= new GridData(GridData.FILL_BOTH);
            topComp.setLayoutData(gd);

            
            
            //Project type
            Group group = new Group(topComp, SWT.NONE);
            group.setText("Choose the project type");
            GridLayout layout = new GridLayout();
            layout.horizontalSpacing = 8;
            layout.numColumns = 3;
            group.setLayout(layout);
            gd= new GridData(GridData.FILL_HORIZONTAL);
            group.setLayoutData(gd);
            
            //Grammar version
            versionLabel = new Label(topComp, 0);
            versionLabel.setText("Grammar Version");
            gd= new GridData(GridData.FILL_HORIZONTAL);
            versionLabel.setLayoutData(gd);
            
            
            
            djangoVersionChoice = new Combo(topComp, SWT.READ_ONLY);
            djangoVersionChoice.add("0.9.6");
            djangoVersionChoice.add("1.0");
            djangoVersionChoice.add("1.1.1");
            
            gd= new GridData(GridData.FILL_HORIZONTAL);
            djangoVersionChoice.setLayoutData(gd);

            
            //Interpreter
            installationLabel = new Label(topComp, 0);
            installationLabel.setText("Interpreter");
            gd= new GridData(GridData.FILL_HORIZONTAL);
            installationLabel.setLayoutData(gd);
            

            //interpreter configured in the project
            final String [] idToConfig = new String[]{"org.python.pydev.ui.pythonpathconf.interpreterPreferencesPagePython"};
            interpretersChoice = new Combo(topComp, SWT.READ_ONLY);
            selectionListener = new SelectionListener(){

                public void widgetDefaultSelected(SelectionEvent e) {
                    
                }

                /**
                 * @param e can be null to force an update.
                 */
                public void widgetSelected(SelectionEvent e) {
                    if(e != null){
                        Button source = (Button) e.getSource();
                        if(!source.getSelection()){
                            return; //we'll get 2 notifications: selection of one and deselection of the other, so, let's just treat the selection
                        }
                    }
                    
                }
            };
            
            interpreterNoteText = new Link(topComp, SWT.LEFT | SWT.WRAP);
            gd= new GridData(GridData.FILL_HORIZONTAL);
            interpreterNoteText.setLayoutData(gd);

            interpreterNoteText.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent e) {
                    PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, idToConfig[0], null, null);
                    dialog.open(); 
                    //just to re-update it again
                    selectionListener.widgetSelected(null);
                }

                public void widgetDefaultSelected(SelectionEvent e) {
                }
            });


            return topComp;
        }

    }

	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}
}