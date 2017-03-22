package il.org.spartan.spartanizer.annotations;

import org.jetbrains.annotations.*;

public @interface FlatEnvUse {
  @NotNull Id[] value();
}