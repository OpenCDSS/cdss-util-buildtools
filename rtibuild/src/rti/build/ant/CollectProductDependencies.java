/**
 *
 * Created on January 29, 2007, 10:28 AM
 *
 */

package rti.build.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Property;

/**
 *
 * @author iws
 */
public class CollectProductDependencies extends Task {
    
    protected  String prop;
    private String sep;
    private Map projectDeps = new HashMap();
    private Set visited = new HashSet();
    
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
        String depsString = getProject().getProperty("product.deps");
        try {
            File baseDir = getProject().getBaseDir();
            collectDeps(baseDir,depsString);
        } catch (IOException ioe) {
            throw new BuildException(ioe);
        }
        List sorted = topologicalSort(projectDeps);
        // remove this from the dependencies
        sorted.remove(getProject().getBaseDir());

        return sorted;
    }

    static String format(Map deps) {
        StringBuffer b = new StringBuffer();
        Iterator i = deps.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry e = (Map.Entry) i.next();
            b.append( ((File)e.getKey()).getName() ).append(':').append(format((List) e.getValue()));
            if (i.hasNext()) {
                b.append("\n");
            }
        }
        return b.toString();
    }

    static String format(List files) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < files.size(); i++) {
            b.append( ((File) files.get(i)).getName() );
            if (i + 1 < files.size()) {b.append(",");}
        }
        return b.toString();
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
        Project x = new Project();
        load.setFile(toLoad);
        load.setProject(x);
        load.setPrefix(productDir.getName());
        load.execute();
        for (Object o : x.getProperties().keySet()) {
            getProject().setProperty(o.toString(), (String) x.getProperties().get(o));
        }
    }
    
    private void collectDeps(File projectDir,String depsString) throws IOException {
        List depList = new ArrayList();
        projectDeps.put(projectDir,depList);
        if (depsString == null) {
            return;
        }
        String[] productPaths = depsString.split(",");
        for (int i = 0; i < productPaths.length; i++) {
            String path = productPaths[i].trim();
            if (path.length() == 0) {
                continue;
            }
            File product = new File(projectDir,path).getCanonicalFile();
            if (product.exists()) {
                depList.add(product);
                if (visited.add(product)) {
                    loadProperties(product);
                    String deps = getProject().getProperty(product.getName() + ".product.deps");
                    collectDeps(product,deps);
                }
            } else {
                System.err.println(product + " doesn't exist");
            }
        }
    }
    
    private Map deepClone(Map map) {
        Map cloned = new HashMap();
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            cloned.put(e.getKey(), new ArrayList( (List) e.getValue() ));
        }
        return cloned;
    }
    
    private List/*<File>*/ topologicalSort(Map/*<File,List<File>>*/ depMap) {
        Map projectDeps = deepClone(depMap);
        List sorted = new ArrayList();
        LinkedList q = new LinkedList();
        HashSet processed = new HashSet();
        for (Iterator it = projectDeps.keySet().iterator(); it.hasNext();) {
            Object project = it.next();
            processed.add(project);
            // check for projects with single dependency (themselves) and seed topology tree
            if ( ((List)projectDeps.get(project)).size() == 0) {
                q.add(project);
            }
        }
        while (q.size() > 0) {
            Object n = q.removeLast();
            processed.remove(n);
            sorted.add(n);
            List fromN = findDependencies(n,projectDeps);
            for (int i = 0; i < fromN.size(); i++) {
                Object m = fromN.get(i);
//                processed.remove(m);
                List deps = (List) projectDeps.get(m);
                deps.retainAll(processed);
                if (deps.size() == 0) {
                    q.add(m);
                }                
            }
             
        }
        return sorted;
    }
    
    private List findDependencies(Object n, Map projectDeps) {
        List deps = new ArrayList();
        for (Iterator it = projectDeps.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            if ( ((List) e.getValue()).contains(n) ) {
                deps.add(e.getKey());
            }
        }
        return deps;
    }
}


