/**
 *
 * Created on May 24, 2007, 8:18 AM
 *
 */

package rti.build.ant;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import org.apache.tools.ant.Task;

/**
 *
 * @author iws
 */
public class GenerateNSISJarFilesText extends Task {
    private String property;
    private String excludes;
    
    public void setProperty(String propertyName) {
        this.property = propertyName;
    }
    
    public String getProperty() {
        return property == null ? "nsis.jars.text" : property;
    }
    
    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }
    
    public void execute() {
        CollectJarDependencies cjd = new CollectJarDependencies();
        cjd.setExcludes(excludes);
        cjd.setProject(getProject());
        Set jars = cjd.computeJars();
        Iterator i = jars.iterator();
        StringBuffer sb = new StringBuffer("#Generated jar file entry\n");
        String spaces = "    ";
        while (i.hasNext()) {
            File jar = (File) i.next();
            sb.append(spaces).append("File ..\\").append(Utils.getRelativePath(getProject(),jar)).append('\n');
        }
        getProject().setProperty(getProperty(),sb.toString());
    }
    
}
