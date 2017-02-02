package il.org.spartan.spartanizer.annotation;

import org.jetbrains.annotations.NotNull;

/** @TODO: real documentation here.
 * @author Alex Kopakzon
 * @since 2016 */
public @interface NestedENV {
  @NotNull String[] value();
}