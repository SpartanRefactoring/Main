package il.org.spartan.spartanizer.annotations;

import org.jetbrains.annotations.NotNull;

/** @author Alex Kopzon
 * @since 2016 */
public @interface Id {
  @NotNull String clazz();

  @NotNull String name();
}