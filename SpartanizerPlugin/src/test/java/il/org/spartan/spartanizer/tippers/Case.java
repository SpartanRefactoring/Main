package il.org.spartan.spartanizer.tippers;

import il.org.spartan.spartanizer.ast.navigate.*;

/**
 * Represents a test case, in conjunction with {@link ReflectiveTester}
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-17
 */
public interface Case {

  void after();

  void before();
  
}
