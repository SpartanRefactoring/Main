package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** TODO:  Dan Abramovich
 please add a description 
 @author Dan Abramovich
 * @since 27-11-2016 
 */

@SuppressWarnings("static-method")
public class Issue0191 {
  @Test public void test0() {
    trimmingOf("int f(){  return ¢ ? Boolean.TRUE : Boolean.FALSE;}")//
        .gives("int f(){return ¢;}")//
        .stays();
  }

  @Test public void test1() {
    trimmingOf("int f(){  return ¢ ? Boolean.FALSE : Boolean.TRUE;}")//
        .gives("int f(){return !¢;}")//
        .stays();
  }
}

