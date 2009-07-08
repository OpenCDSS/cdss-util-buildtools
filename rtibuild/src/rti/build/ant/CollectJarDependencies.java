/**
 *
 * Created on January 29, 2007, 11:34 AM
 *
 */

package rti.build.ant;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.Path;

/**
 *
 * @author iws
 */
public class CollectJarDependencies extends CollectProductDependencies {

    private String refID;
    boolean useBuildDirs = false;
    private String excludes;
    private String includes;

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public void setUseBuildDirs(boolean useBuildDirs) {
        this.useBuildDirs = useBuildDirs;
    }

    public void setRefid(String refID) {
        this.refID = refID;
    }

    public void execute() throws BuildException {
        Set jars = computeJars();
        if (refID == null) {
            getProject().setProperty(getProperty(), buildPath(jars));
        } else {
            getProject().addReference(refID, buildRef(jars));
        }
    }

    private Path buildRef(Collection jarFiles) {
        FileList files = new FileList();
        Path path = new Path(getProject());
        path.addFilelist(files);
        File root = getProject().getBaseDir().getAbsoluteFile().getParentFile();
        files.setDir(root);
        Iterator it = jarFiles.iterator();
        while (it.hasNext()) {
            File f = (File) it.next();
            String fName = f.getAbsolutePath();
            String rPath = Utils.getRelativePath(root, f.getAbsoluteFile());
            if (!fName.equals(rPath)) {
                FileList.FileName name = new FileList.FileName();
                name.setName(Utils.getRelativePath(root, f.getAbsoluteFile()));
                files.addConfiguredFile(name);
            } else {
                path.add(new Path(getProject(),fName));
            }
        }
        return path;
    }

    private File getProductJar(File productDir, String depName) {
        return new File(productDir, "/dist/" + getProject().getProperty(depName + ".product.name") + "_" + getProject().getProperty(depName + ".product.version") + ".jar");
    }

    public Set computeJars() {
        Collection products = getDeps();
        LinkedHashSet jars = new LinkedHashSet();
        HashSet jarNames = new HashSet();
        String thisProduct = getProject().getProperty("product.name");
        File thisProductDir = getProject().getBaseDir();
        File thisProductJar = new File(thisProductDir, "/dist/" + thisProduct + "_" + getProject().getProperty("product.version") + ".jar");
        jarNames.add(thisProductJar.getName());
        jars.add(thisProductJar);
        loadProperties(thisProductDir);
        addJars(jars, jarNames, thisProductDir);
        Set includes = this.includes == null ? null : new HashSet(Arrays.asList(this.includes.split("[ ,]")));
        for (Iterator it = products.iterator(); it.hasNext();) {
            File dep = (File) it.next();
            if (includes != null && !includes.contains(dep.getName())) {
                continue;
            }
            loadProperties(dep);
            addJars(jars, jarNames, dep);
            String depName = dep.getName();
            if (useBuildDirs) {
                String buildDir = getProject().getProperty("build.dir");
                if (buildDir == null) {
                    buildDir = "bin";
                }
                jars.add(new File(dep, buildDir));
            } else {
                File f = getProductJar(dep, depName);
                // only add jar if final name hasn't been seen before
                if (jarNames.add(f.getName())) {
                    jars.add(f);
                }
            }
        }
        List excludeList = this.excludes == null ? new LinkedList() : new LinkedList(Arrays.asList(this.excludes.split("[, ]")));
        Iterator i = excludeList.iterator();
        while (i.hasNext()) {
            String excludeToken = (String) i.next();
            if (excludeToken.trim().length() == 0) {
                i.remove();
            }
        }
        String[] excludes = (String[]) excludeList.toArray(new String[0]);
        i = jars.iterator();
        while (i.hasNext()) {
            File f = (File) i.next();
            String jarName = f.getName();
            for (int j = 0; j < excludes.length; j++) {
                if (jarName.indexOf(excludes[j]) >= 0) {
                    i.remove();
                    break;
                }
            }
        }
        return jars;
    }

    private void addJars(LinkedHashSet jars, Set jarNames, File dep) {
        add(jars, jarNames, dep, "lib.deps");
        add(jars, jarNames, dep, "run.classpath");
    }

    private String getProperty() {
        return prop == null ? "collected.jar.deps" : prop;
    }

    private void add(LinkedHashSet jars, Set jarNames, File dep, String prop) {
        String fullProp = dep.getName() + "." + prop;
        String paths = getProject().getProperty(fullProp);
        if (paths != null) {
            String[] chunks = paths.split("(;|:)(?!//)");
            for (int i = 0; i < chunks.length; i++) {
                String trimmed = chunks[i].trim();
                if (trimmed.length() > 0) {
                    if ((trimmed.endsWith(".jar") || useBuildDirs) && jarNames.add(trimmed)) {
                        if (isAbsolutePath(trimmed)) {
                            jars.add(new File(trimmed));
                        } else {
                            jars.add(new File(dep, trimmed));
                        }
                    }
                }
            }
        }
    }

    private boolean isAbsolutePath(String trimmed) {
        return trimmed.startsWith("/") || trimmed.matches("[a-z]://.*");
    }
}