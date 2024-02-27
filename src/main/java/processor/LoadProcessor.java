package processor;

import tools.Load;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.reflect.Field;
import java.util.Set;

@SupportedAnnotationTypes("tools.Load")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class LoadProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> elems, RoundEnvironment renv) {
        Set<? extends Element> elements = renv.getElementsAnnotatedWith(Load.class);
        for (Element e : elements) {
            if (e instanceof VariableElement && ((VariableElement)e).getKind() == ElementKind.FIELD) {
                Field field = (Field) e; // How to transform a field in element ?
            } else {
                throw new IllegalArgumentException("@Load must be use on field.");
            }
        }
        return true;
    }
}
