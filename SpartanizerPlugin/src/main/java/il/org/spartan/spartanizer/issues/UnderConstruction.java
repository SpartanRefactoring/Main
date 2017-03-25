package il.org.spartan.spartanizer.issues;

import org.jetbrains.annotations.*;

/** Notational convenience
 * @author Yossi Gil {@code yogi@cs.technion.ac.il}
 * @since 2017-03-22 */
public @interface UnderConstruction {
  @NotNull String value() default "";
}
