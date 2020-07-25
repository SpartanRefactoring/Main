package il.org.spartan.spartanizer.java;

import il.org.spartan.spartanizer.annotations.FlatEnvUse;
import il.org.spartan.spartanizer.annotations.Id;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Sep 7, 2016 */
@SuppressWarnings("all")
public final class EnvironmentCodeExamples {
  static class EX02 {
    int x = 1;
    @FlatEnvUse(@Id(name = "x", clazz = "int")) int y;
  }
}
