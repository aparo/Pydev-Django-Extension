/**
 * 
 */
package org.python.pydev.django;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.python.pydev.core.bundle.BundleInfo;
import org.python.pydev.core.bundle.IBundleInfo;
import org.python.pydev.core.bundle.ImageCache;

/**
 * The main plugin class to be used in the desktop.
 */
public class DjangoPlugin extends AbstractUIPlugin {
    //The shared instance.
    private static DjangoPlugin plugin;
    //Resource bundle.
    private ResourceBundle resourceBundle;
    
    /**
     * The constructor.
     */
    public DjangoPlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle("org.python.pydev.django.DjangoPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
     * This method is called when the plug-in is stopped
     */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     */
    public static DjangoPlugin getDefault() {
        return plugin;
    }

    /**
     * Returns the string from the plugin's resource bundle,
     * or 'key' if not found.
     */
    public static String getResourceString(String key) {
        ResourceBundle bundle = DjangoPlugin.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
     * Returns the plugin's resource bundle,
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    
    // ----------------- SINGLETON THINGS -----------------------------
    public static IBundleInfo info;
    public static IBundleInfo getBundleInfo(){
        if(DjangoPlugin.info == null){
        	DjangoPlugin.info = new BundleInfo(DjangoPlugin.getDefault().getBundle());
        }
        return DjangoPlugin.info;
    }
    public static void setBundleInfo(IBundleInfo b){
    	DjangoPlugin.info = b;
    }
    // ----------------- END BUNDLE INFO THINGS --------------------------
    /**
     * @return the cache that should be used to access images within the pydev plugin.
     */
    public static ImageCache getImageCache(){
        return DjangoPlugin.getBundleInfo().getImageCache();
    }
}
