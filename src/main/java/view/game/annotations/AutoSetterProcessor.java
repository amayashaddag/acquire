// javac -cp "lib/*:src/main/java" -d build src/main/java/view/game/annotations/AutoSetterProcessor.java
// javac -cp "lib/*:src/main/java:build" -processor view.game.annotations.AutoSetterProcessor -d build src/main/java/view/Debug.java 

package view.game.annotations;

import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic.Kind;

import view.game.GameView;

import javax.lang.model.util.Types;

@SupportedAnnotationTypes("view.game.annotations.AutoSetter")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AutoSetterProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> elems, RoundEnvironment renv) {
        Set<? extends Element> elements = renv.getElementsAnnotatedWith(AutoSetter.class);
        for (Element e : elements) {
            Class<?> paramType;
            try {
                paramType = e.getAnnotation(AutoSetter.class).paramType();
            } catch (MirroredTypeException excpt) {
                paramType = null;
                System.out.println("ir");
            } // FIXME ir ??
            if(!hasPublicConstructor(e, paramType)) {
                processingEnv.getMessager().printMessage(Kind.ERROR,
                    "The class " + e.getSimpleName() + " must implement a public constructor with a GameView in param.");
                return false;
            }
        }
        return true;
    }

    private boolean hasPublicConstructor(Element e, Class<?> paramType) {
        List<ExecutableElement> constructors = ElementFilter.constructorsIn(e.getEnclosedElements());
        return constructors.stream()
                .filter(c -> c.getModifiers().contains(Modifier.PUBLIC))
                .filter(c -> c.getParameters().size() == 1)
                .anyMatch(c -> c.getParameters().get(0).asType().toString().equals(paramType.getName()));
    }
}