package de.schuette.cobra2D.entity.editing;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @author Chris
 * This annotation marks Entities as editable. Editable entities can be loaded into the entity editor to change the following attributes:
 * - Texture of the entity
 * - EntityPoints of the entity
 * Only mark your entity as editable, if you do not manage one of the above aspects in the entity automatically.
 * Example: If your entity calculates its points itself, to modify them dynamically, you should not mark your entity as editable.
 * If your entity do not care about entity points, and the map designer can change the texture, mark your entity as editable to. So the 
 * map designer is able to add his own entity points corresponding to the underlying texture.   
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Editable {

}
