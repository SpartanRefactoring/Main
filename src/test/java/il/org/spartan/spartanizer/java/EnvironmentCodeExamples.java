/** TODO:  Yossi Gil <yossi.gil@gmail.com> please add a description 
 * @author  Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 7, 2016
 */
package il.org.spartan.spartanizer.java;

import il.org.spartan.spartanizer.annotations.*;

@SuppressWarnings("all")
public final class EnvironmentCodeExamples {
  static class EX02 {
    int x = 1;
    @FlatEnvUse({ @Id(name = "x", clazz = "int") }) int y;
  }
}

