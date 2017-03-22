package il.org.spartan.spartanizer.annotation;

import org.jetbrains.annotations.*;

public @interface OutOfOrderFlatENV {
  @NotNull String[] value();
}
