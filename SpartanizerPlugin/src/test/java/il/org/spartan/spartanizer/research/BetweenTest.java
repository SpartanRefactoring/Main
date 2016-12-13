package il.org.spartan.spartanizer.research;

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

  @Ignore @Test public void b() {
    trimmingOf("if('a' <= c && c < 'z') use();")//
        .withTipper(InfixExpression.class, new Between())//
        .gives("if(is.value(c).between('a').inclusive().and('z'))use();")//
        .stays();
  }
  // @Test public void respect() {
  // trimmingOf("return ¢ != null ? ¢ : \"\";")//
  // .withTipper(ConditionalExpression.class, new Unless())//
  // .withTipper(ConditionalExpression.class, new DefaultsTo())//
  // .gives("return default¢(¢).to(\"\");")//
  // .stays();
  // }
  //
  // @Test public void respect2() {
  // trimmingOf("return ¢ != null ? ¢ : \"\";")//
  // .withTipper(ConditionalExpression.class, new Unless())//
  // .stays();
  // }
}
