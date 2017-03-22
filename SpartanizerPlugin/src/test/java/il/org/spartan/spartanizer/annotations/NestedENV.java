package il.org.spartan.spartanizer.annotations;

import org.jetbrains.annotations.NotNull;

/** @TODO real documentation here.
 * @author Alex Kopzon
 * @since 2016 */
public @interface NestedENV {
  @NotNull Id[] value();
}