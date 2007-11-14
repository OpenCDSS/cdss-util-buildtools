/**
 *
 * Created on January 29, 2007, 10:28 AM
 *
 */

package rti.build.ant;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Property;

/**
 *
 * @author iws
 */
public class CollectProductDependencies extends Task {
    
    protected  String prop;
    private String sep;
    
    public void setSeparator(String sep) {
        this.sep = sep;
    }
    
    public String getSeparator() {
        return sep == null ? File.pathSeparator : sep;
    }
    
    public void setProperty(String prop) {
        this.prop = prop;
    }
    
    private String getProperty() {
        return prop == null ? "collected.product.deps" : prop;
    }
    
    protected Collection getDeps() {
        LinkedHashSet products = new LinkedHashSet();
        String depsString = getProject().getProperty("product.deps");
        try {
            collectDeps(getProject().getBaseDir(),products,null,depsString);
        } catch (IOException ioe) {
            throw new BuildException(ioe);
        }
        return products;
    }
    
    protected String buildPath(Collection stuff) {
        StringBuffer sb = new StringBuffer();
        String sep = getSeparator();
        for (Iterator it = stuff.iterator(); it.hasNext();) {
            String path = getRelativePath( (File) it.next() );
            if (path != null) {
                sb.append( path );
                if (it.hasNext()) {
                    sb.append(sep);
                }
            }
        }
        return sb.toString();
    }
    
    protected String getRelativePath(File f) {
        return Utils.getRelativePath(getProject(),f);
    }
    
    
    public void execute() throws BuildException {
        Collection products = getDeps();
        getProject().setProperty(getProperty(),buildPath(products));
    }
    
    protected void loadProperties(File productDir) throws BuildException {
        Property load = new Property();
        File toLoad = new File(productDir,"/conf/product.properties");
        load.setFile(toLoad);
        load.setProject(getProject());
        load.setPrefix(productDir.getName());
        load.execute();
    }
    
    private void collectDeps(File projectDir,LinkedHashSet all,HashSet visited,String depsString) throws IOException {
        if (depsString == null) {
            return;
        }
        if (visited == null) {
            visited = new HashSet();
        }
        String[] productPaths = depsString.split(",");
        for (int i = 0; i < productPaths.length; i++) {
            File product = new File(projectDir,productPaths[i]).getCanonicalFile();
            if (visited.add(product)) {
                loadProperties(product);
                String deps = getProject().getProperty(product.getName() + ".product.deps");
                collectDeps(product,all,visited,deps);
            }
            all.add(product);
        }
    }
    
}


