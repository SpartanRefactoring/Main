package il.org.spartan.spartanizer.annotations;

import org.jetbrains.annotations.NotNull;

public @interface FlatEnvUse {
  @NotNull Id[] value();
}