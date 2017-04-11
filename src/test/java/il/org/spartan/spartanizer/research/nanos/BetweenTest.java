package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/** Between nano
 * @author Ori Marcovitch
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
@SuppressWarnings("static-method")
public class BetweenTest {
  @Test public void a() {
    topDownTrimming("if( c >'a' && c < 'z') use();")//
        .using(InfixExpression.class, new Between())//
        .gives("if(is.value(c).between('a').and('z'))use();")//
        .stays();
  }

  // different operators
  @Test public void b() {
    topDownTrimming("if('a' <= c && c < 'z') use();")//
        .using(InfixExpression.class, new Between())//
        .gives("if(is.value(c).between('a').inclusive().and('z'))use();")//
        .stays();
  }

  // order
  @Test public void c() {
    topDownTrimming("radix >= Character.MIN_RADIX && radix <= Character.MAX_RADIX")//
        .using(InfixExpression.class, new Between())//
        .gives("if(is.value(radix).between(Character.MIN_RADIX).inclusive().and(Character.MAX_RADIX))use();")//
        .stays();
  }
}
