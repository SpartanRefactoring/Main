package il.org.spartan.spartanizer.annotation;

import org.jetbrains.annotations.*;

public @interface InOrderFlatENV {
  @NotNull String[] value();
}