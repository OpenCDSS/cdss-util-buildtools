/**
 *
 * Created on February 1, 2007, 11:04 AM
 *
 */

package rti.agent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author iws
 */
public class ReloadAgent implements Runnable {
    ServerSocket server;
    Instrumentation inst;
    ReloadAgent(ServerSocket server,Instrumentation inst) {
        this.server = server;
        this.inst = inst;
    }
    
    public static void premain(String agentArgs, Instrumentation inst) {
        int port = 12345;
        try {
            if (agentArgs != null)
                port = Integer.parseInt(agentArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ServerSocket s = new ServerSocket(port);
            Thread daemon = new Thread(new ReloadAgent(s,inst));
            daemon.setDaemon(true);
            daemon.start();
            System.out.println("reload agent ready");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void run() {
        while (true) {
            try {
                acceptClients();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void acceptClients() throws Exception {
        Socket client = server.accept();
        Scanner lines = new Scanner(client.getInputStream());
        Scanner s = new Scanner(lines.nextLine());
        Pattern pathElement = Pattern.compile(File.pathSeparator);
        ArrayList<URL> urls = new ArrayList<URL>();
        s.useDelimiter(pathElement);
        while (s.hasNext()) {
            String el = s.next();
            System.out.println("el " + el );
            urls.add(new File(el).toURI().toURL());
        }
        URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
        Pattern className = Pattern.compile("[,]");
        if (lines.hasNextLine()) {
            s = new Scanner(lines.nextLine());
            s.useDelimiter(className);
        }
        ArrayList<ClassDefinition> redefines = new ArrayList<ClassDefinition>();
        while (s.hasNext()) {
            String name = s.next();
            String resourceName = name;
            resourceName = resourceName.replace('.','/') + ".class";
            URL resource = loader.findResource(resourceName);
            if (resource == null) {
                System.out.println("could not locate " + resourceName);
            } else {
                URLConnection conn = resource.openConnection();
                byte[] bytes = new byte[conn.getContentLength()];
                InputStream in = conn.getInputStream();
                in.read(bytes);
                in.close();
                ClassDefinition cd = new ClassDefinition(Class.forName(name),bytes);
                redefines.add(cd);
            }
        }
        System.out.println("redfining " + redefines);
        inst.redefineClasses(redefines.toArray(new ClassDefinition[0]));
        System.out.println("done");
    }
    
}
