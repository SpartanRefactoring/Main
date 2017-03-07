package il.org.spartan.spartanizer.java.namespace;

import il.org.spartan.spartanizer.annotations.*;

/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
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
