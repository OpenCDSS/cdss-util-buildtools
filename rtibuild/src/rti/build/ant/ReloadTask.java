/**
 *
 * Created on February 1, 2007, 11:05 AM
 *
 */

package rti.build.ant;

import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.selectors.DependSelector;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.GlobPatternMapper;

/**
 *
 * @author iws
 */
public class ReloadTask extends Task {
    
    private int port = 12345;
    private String projectDeps;
//    private Path classpath;
//    private String classes;
    
    public void execute() throws BuildException {
        try {
            reload();
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
    
    private void gather(String depName,StringBuffer path,StringBuffer classes) {
        System.out.println("adding deps for " + depName);
        String buildDirName = getProject().getProperty("basedir") + "/../" + depName + "/build";
        path.append(buildDirName);
        
        FileSet files = new FileSet();
        File buildDir = new File(buildDirName);
        File srcDir = new File(getProject().getProperty("basedir") + "/../" + depName + "/src");
        files.setDir(srcDir);
        DependSelector dep = new DependSelector();
        Mapper.MapperType type = new Mapper.MapperType();
        type.setValue("glob");
        Mapper mapper = dep.createMapper();
        mapper.setType(type);
        mapper.setFrom("*.java");
        mapper.setTo("*.class");
        dep.setTargetdir(buildDir);
        files.addDepend(dep);
        System.out.println("scanning");
        DirectoryScanner dscan = files.getDirectoryScanner(getProject());
        dscan.scan();
        String[] inc = dscan.getIncludedFiles();
        if (classes.length() > 0 && inc.length > 0)
            classes.append(',');
        for (int j = 0; j < inc.length; j++) {
            String clazz = inc[j].replace(File.separatorChar,'.').replace(".java","");
            classes.append(clazz);
            if (j + 1 < inc.length) {
                classes.append(',');
            }
        }
        System.out.println("classes " + classes);
    }
    
    private void reload() throws Exception {
        String[] deps = projectDeps.split(File.pathSeparator);
        StringBuffer path = new StringBuffer();
        StringBuffer classes = new StringBuffer();
        gather(getProject().getBaseDir().getName(),path,classes);
        if (deps.length > 0)
            path.append(File.pathSeparator);
        for (int i = 0; i < deps.length; i++) {
            gather(deps[i],path,classes);
            if (i + 1 < deps.length) {
                path.append(File.pathSeparator);
            }
        }
        System.out.println("classes to redefine " + classes);
        getProject().setProperty("fork.javac","false");
        getProject().executeTarget("compile-java");
        Socket s = new Socket(InetAddress.getLocalHost(),12345);
        PrintWriter pw = new PrintWriter(s.getOutputStream(),false);
        pw.println(path.toString());
        pw.println(classes.toString());
        pw.flush();
        pw.close();
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
//    public Path getClasspath() {
//        return classpath;
//    }
//
//    public void setClasspath(Path classpath) {
//        this.classpath = classpath;
//    }
//
//    public String getClasses() {
//        return classes;
//    }
//
//    public void setClasses(String classes) {
//        this.classes = classes;
//    }
    
    public String getProjectDeps() {
        return projectDeps;
    }
    
    public void setProjectDeps(String projectDeps) {
        this.projectDeps = projectDeps;
    }
    
    
    
}
