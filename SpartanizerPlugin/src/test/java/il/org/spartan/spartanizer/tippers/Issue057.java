package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.ExpressionComparator.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static il.org.spartan.spartanizer.tippers.TESTUtils.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.spartanizations.*;
import il.org.spartan.spartanizer.tipping.*;


/** @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" }) public class Issue057 {
  @Test public void a() {
    trimmingOf("void m(List<Expression>... expressions) { }").gives("void m(List<Expression>... xss) {}");
  }

  @Test public void b() {
    trimmingOf("void m(Expression... expression) { }").gives("void m(Expression... xs) {}");
  }
}
