/*
 * IndexThisProcessor.java
 *
 * Created on Sep 26, 2007, 5:06:55 PM
 *
 */

package rti.build.apt;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iws
 */
class IndexThisProcessor implements AnnotationProcessor {

    public static final String INDEX_THIS = "rti.beans.MetaData.IndexThis";
    public static final String INDEX_NAME = "indexName";
    private Set<AnnotationTypeDeclaration> declarations;
    private AnnotationProcessorEnvironment env;
    private final AnnotationTypeDeclaration indexThis;
    private final Map<String, PrintWriter> writers;

    public IndexThisProcessor(Set<AnnotationTypeDeclaration> declarations,
            AnnotationProcessorEnvironment env) {
        this.declarations = declarations;
        this.env = env;
        indexThis = (AnnotationTypeDeclaration) env.getTypeDeclaration(INDEX_THIS);
        writers = new HashMap<String, PrintWriter>();
    }

    public void process() {
        for (Declaration decl : env.getDeclarationsAnnotatedWith(indexThis)) {
            if (decl instanceof ClassDeclaration) {
                try {
                    process((ClassDeclaration) decl);
                } catch (IOException ex) {
                    env.getMessager().printError("Unexpected exception opening output index " + ex.getMessage());
                }
            }
        }
    }

    private PrintWriter getWriter(String location) throws IOException {
        PrintWriter writer = writers.get(location);
        if (writer == null) {
            writer = env.getFiler().createTextFile(Filer.Location.SOURCE_TREE,
                    "", new File("META-INF/services/" + location), null);
            writers.put(location, writer);
        }
        return writer;
    }

    private void process(ClassDeclaration decl) throws IOException {
        Collection<AnnotationMirror> mirrors = decl.getAnnotationMirrors();
        for (AnnotationMirror anno : mirrors) {
            if (anno.getAnnotationType().getDeclaration().equals(indexThis)) {
                for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> el : anno.getElementValues().entrySet()) {
                    if (el.getKey().getSimpleName().equals(INDEX_NAME)) {
                        getWriter(el.getValue().getValue().toString()).println(decl.getQualifiedName());
                        break;
                    }
                }
                break;
            }
        }
    }
}