/**
 *
 * Created on May 24, 2007, 9:04 AM
 *
 */

package rti.build.ant;

import java.io.File;
import java.util.HashSet;
import org.apache.tools.ant.Project;

/**
 *
 * @author iws
 */
public class Utils {
    
    public static String getRelativePath(Project project,File f) {
        return getRelativePath(project.getBaseDir().getAbsoluteFile(),f.getAbsoluteFile());
    }
    
    public static String getRelativePath(File baseDir,File f) {
        if (isParent(baseDir,f)) {
            return f.getAbsolutePath().substring(baseDir.getAbsolutePath().length() + 1);
        }
        File commonParent = findCommonParent(baseDir,f);
        if (commonParent == null) {
            return f.getAbsolutePath();
        }
        
        
        StringBuffer sb = new StringBuffer("..").append(File.separatorChar);
        File parent = baseDir.getParentFile();
        while (parent != null && !parent.equals(commonParent)) {
            sb.append("..").append(File.separatorChar);
            parent = parent.getParentFile();
        }
        if (parent == null) {
            System.out.println("NO PATH");
            return null;
        }
        sb.append(f.getAbsolutePath().substring(parent.getAbsolutePath().length() + 1));
        return sb.toString();
    }
    
    private static File findCommonParent(File f, File f2) {
        HashSet parents = new HashSet();
        File p = f.getParentFile();
        while (p != null) {
            parents.add(p);
            p = p.getParentFile();
        }
        p = f2.getParentFile();
        while (p != null) {
            if (parents.contains(p)) {
                return p;
            }
            p = p.getParentFile();
        }
        return null;
    }
    
    
    static boolean isParent(File parent,File child) {
        File cp = child.getParentFile();
        while (cp != null) {
            if (parent.equals(cp))
                return true;
            cp = cp.getParentFile();
        }
        return false;
    }
    
}
