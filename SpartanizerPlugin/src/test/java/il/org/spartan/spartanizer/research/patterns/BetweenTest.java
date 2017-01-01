package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class BetweenTest {
  @Test public void a() {
    trimmingOf("if('a' < c && c < 'z') use();")//
        .withTipper(InfixExpression.class, new Between())//
        .gives("if(is.value(c).between('a').and('z'))use();")//
        .stays();
  }

  // different operators
  @Ignore @Test public void b() {
    trimmingOf("if('a' <= c && c < 'z') use();")//
        .withTipper(InfixExpression.class, new Between())//
        .gives("if(is.value(c).between('a').inclusive().and('z'))use();")//
        .stays();
  }

  // order
  @Ignore @Test public void c() {
    trimmingOf("radix >= Character.MIN_RADIX && radix <= Character.MAX_RADIX")//
        .withTipper(InfixExpression.class, new Between())//
        .gives("if(is.value(radix).between(Character.MIN_RADIX).inclusive().and(Character.MAX_RADIX))use();")//
        .stays();
  }
}
