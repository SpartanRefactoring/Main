package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Between nano
 * @author Ori Marcovitch
 * @since 2016 */

@Ignore
@SuppressWarnings("static-method")
public class BetweenTest {
  @Test public void a() {
    trimminKof("if( c >'a' && c < 'z') use();")//
        .using(new Between(), InfixExpression.class)//
        .gives("if(is.value(c).between('a').and('z'))use();")//
        .stays();
  }

  // different operators
  @Test public void b() {
    trimminKof("if('a' <= c && c < 'z') use();")//
        .using(new Between(), InfixExpression.class)//
        .gives("if(is.value(c).between('a').inclusive().and('z'))use();")//
        .stays();
  }

  // order
  @Test public void c() {
    trimminKof("radix >= Character.MIN_RADIX && radix <= Character.MAX_RADIX")//
        .using(new Between(), InfixExpression.class)//
        .gives("if(is.value(radix).between(Character.MIN_RADIX).inclusive().and(Character.MAX_RADIX))use();")//
        .stays();
  }
}
