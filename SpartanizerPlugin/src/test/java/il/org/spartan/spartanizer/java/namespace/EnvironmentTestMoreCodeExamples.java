package il.org.spartan.spartanizer.java.namespace;

import il.org.spartan.spartanizer.annotations.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
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
