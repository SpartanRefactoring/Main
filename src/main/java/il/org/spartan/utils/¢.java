package il.org.spartan.utils;

import java.lang.annotation.*;

import il.org.spartan.spartanizer.engine.nominal.*;
import org.jetbrains.annotations.NotNull;

/** Short name of {@link JohnDoe} parameter
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-13 */
@Documented
@Target({ ElementType.PARAMETER, ElementType.TYPE_PARAMETER })
public @interface ¢ {
  /** Add here brief documentation if you like; return type may change though */
  @NotNull String value() default "J(ohn)|(ane) Doe";
}
