/** TODO:  Yossi Gil <yossi.gil@gmail.com> please add a description 
 * @author  Yossi Gil <yossi.gil@gmail.com>
 * @since Dec 26, 2016
 */
package il.org.spartan.spartanizer.java.namespace;

import il.org.spartan.spartanizer.annotations.*;

final class EnvironmentTestMoreCodeExamples {
  class A {
    @FlatEnvUse({ @Id(name = "str", clazz = "String") }) void foo() {
      /**/}
  }

  {
    new A().hashCode();
  }
}

