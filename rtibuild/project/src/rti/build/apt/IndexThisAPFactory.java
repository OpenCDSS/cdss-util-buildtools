/*
 * IndexThisAnnotationProcessor.java
 * 
 * Created on Sep 26, 2007, 5:03:39 PM
 * 
 */

package rti.build.apt;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 *
 * @author iws
 */
public class IndexThisAPFactory implements AnnotationProcessorFactory {

    public IndexThisAPFactory() {
        System.out.println("index factory created");
    }

    public Collection<String> supportedOptions() {
        return Collections.emptyList();
    }

    public Collection<String> supportedAnnotationTypes() {
        return Collections.singleton(IndexThisProcessor.INDEX_THIS);
    }

    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> declarations,
            AnnotationProcessorEnvironment env) {
        AnnotationProcessor processor = AnnotationProcessors.NO_OP;
        if (! declarations.isEmpty() ) {
            processor = new IndexThisProcessor(declarations,env);
        }
        return processor;
    }

}
