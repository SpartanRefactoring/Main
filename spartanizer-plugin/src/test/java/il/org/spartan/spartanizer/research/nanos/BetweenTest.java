package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.junit.Ignore;
import org.junit.Test;

/** Between nano
 * @author Ori Marcovitch
 * @since 2016 */
@Ignore("Ori Marcovitch please fix")
@SuppressWarnings("static-method")
public class BetweenTest {
  @Test public void a() {
    trimmingOf("if( c >'a' && c < 'z') use();").using(new Between(), InfixExpression.class).gives("if(is.value(c).between('a').and('z'))use();")
        .stays();
  }
  @Test public void b() {
    trimmingOf("if('a' <= c && c < 'z') use();").using(new Between(), InfixExpression.class)
        .gives("if(is.value(c).between('a').inclusive().and('z'))use();").stays();
  }
  @Test public void c() {
    trimmingOf("radix >= Character.MIN_RADIX && radix <= Character.MAX_RADIX").using(new Between(), InfixExpression.class)
        .gives("if(is.value(radix).between(Character.MIN_RADIX).inclusive().and(Character.MAX_RADIX))use();").stays();
  }
}
