// javac -cp "lib/*:src/main/java" -d build src/main/java/processor/AutoSetterProcessor.java
// javac -cp "lib/*:src/main/java:build" -processor processor.AutoSetterProcessor -d build src/main/java/view/Debug.java

package model.processor;

import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic.Kind;

import model.tools.AutoSetter;

@SupportedAnnotationTypes("tools.AutoSetter")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class AutoSetterProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> elems, RoundEnvironment renv) {
        Set<? extends Element> elements = renv.getElementsAnnotatedWith(AutoSetter.class);
        for (Element e : elements) {
            TypeMirror paramType = null;
            try {
                e.getAnnotation(AutoSetter.class).typeParam();
            } catch (MirroredTypeException excpt) {
                paramType = excpt.getTypeMirror();
            }
            if(!hasPublicConstructor(e, paramType)) {
                processingEnv.getMessager().printMessage(Kind.ERROR,
                    "The class " + e.getSimpleName() + " must implement a public constructor with a GameView in param.");
                return false;
            }
        }
        return true;
    }

    private boolean hasPublicConstructor(Element e, TypeMirror paramType) {
        List<ExecutableElement> constructors = ElementFilter.constructorsIn(e.getEnclosedElements());
        return constructors.stream()
                .filter(c -> c.getModifiers().contains(Modifier.PUBLIC))
                .filter(c -> c.getParameters().size() == 1)
                .anyMatch(c -> c.getParameters().get(0).asType().equals(paramType));
    }
}