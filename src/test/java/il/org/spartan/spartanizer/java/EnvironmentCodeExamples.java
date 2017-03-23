package il.org.spartan.spartanizer.java;

import il.org.spartan.spartanizer.annotations.*;

/** TODO: Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 7, 2016 */
@SuppressWarnings("all")
public final class EnvironmentCodeExamples {
  static class EX02 {
    int x = 1;
    @FlatEnvUse(@Id(name = "x", clazz = "int")) int y;
  }
}
