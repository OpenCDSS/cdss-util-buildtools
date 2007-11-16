/*
 * GenerateBuildDoc.java
 * 
 * Created on Nov 14, 2007, 8:35:56 AM
 * 
 */
package rti.build.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author iws
 */
public class GenerateBuildDoc extends Task {

    private File outputDirectory;

    private Document buildMasterDoc() throws Exception {
        Document master = DocumentBuilderFactory.newInstance().
                newDocumentBuilder().newDocument();
        List docs = findAllDocs();
        Element root = master.createElement("projects");
        master.appendChild(root);
        for (int i = 0; i < docs.size(); i++) {
            Document doc = (Document) docs.get(i);
            root.appendChild(master.importNode(doc.getDocumentElement(), true));
        }
        return master;
    }

    private List findAllDocs() throws Exception {
        Set docSourceFiles = new HashSet();
        LinkedList filesToSearch = new LinkedList();
        List docs = new ArrayList();
        filesToSearch.add(new File(getProject().getBaseDir(), "conf/build.xml"));
        while (filesToSearch.size() > 0) {
            File f = (File) filesToSearch.removeFirst();
            if (docSourceFiles.add(f)) {
                Document doc = parseDoc(f);
                docs.add(doc);
                NodeList imports = doc.getElementsByTagName("import");
                for (int i = 0; i < imports.getLength(); i++) {
                    String imported = imports.item(i).getAttributes().
                            getNamedItem("file").getNodeValue();
                    filesToSearch.add(new File(f.getAbsoluteFile().getParentFile(),
                            imported));
                }
            }
        }
        return docs;
    }

    private void generate(Document doc) throws Exception {
        StreamSource styleSheet = new StreamSource(getClass().
                getResourceAsStream("antstyle.xsl"));
        TransformerFactory factory = TransformerFactory.newInstance();
        // workaround for indentation madness between 1.4 and 1.5

        try {
            factory.setAttribute("indent-number", new Integer(2));
        } catch (IllegalArgumentException iae) {
        // ignore
        }
        Transformer trans = factory.newTransformer(new StreamSource(getClass().
                getResourceAsStream("antstyle.xsl")));
        trans.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        trans.transform(new DOMSource(doc), new StreamResult(new File(getOutputDirectory(),
                "antdoc.html")));
        factory.newTransformer().transform(new DOMSource(doc),
                new StreamResult(System.out));
    }

    private Document parseDoc(File f) throws Exception {
        if (!f.exists()) {
            throw new FileNotFoundException(f.getAbsolutePath());
        }
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
    }

    public File getOutputDirectory() {
        return outputDirectory == null ? new File(getProject().getBaseDir(),
                "doc") : outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

//    @Override
    public void execute() throws BuildException {
        File outputDir = getOutputDirectory();
        outputDir.mkdirs();
        try {
            Document doc = buildMasterDoc();
            generate(doc);
        } catch (Exception ex) {
            throw new BuildException(ex);
        }

    }
}
