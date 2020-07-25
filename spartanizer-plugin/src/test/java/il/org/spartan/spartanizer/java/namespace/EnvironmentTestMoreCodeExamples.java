package il.org.spartan.spartanizer.java.namespace;

import il.org.spartan.spartanizer.annotations.FlatEnvUse;
import il.org.spartan.spartanizer.annotations.Id;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Dec 26, 2016 */
final class EnvironmentTestMoreCodeExamples {
  {
    new A().hashCode();
  }

  static class A {
    @FlatEnvUse(@Id(name = "str", clazz = "String")) void foo() {
      /**/}
  }
}
