package tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @apiNote  Use with the following name
 * convention : same name as the image file
 * but in UPPER CASE, and replace '-' by '_'.
 *
 * @implNote Specials characters are not allowed (except
 * '-' and '_')!
 *
 * @author Arthur Deck
 * @version 1
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface Load {
}
