package il.org.spartan.spartanizer.annotation;

import org.jetbrains.annotations.NotNull;

public @interface OutOfOrderFlatENV {
  @NotNull String[] value();
}
