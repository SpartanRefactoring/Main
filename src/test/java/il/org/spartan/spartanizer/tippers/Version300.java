package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Misc unit tests with no better other place for version 3.00
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-09 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore("Unignore one by one")
@SuppressWarnings({ "static-method", "javadoc" })
public final class Version300 {
  @Test public void strangeFixedPoint() {
    azzert.that(theSpartanizer.fixedPoint("A a(A b) throws B { A c; c = b; return c; }"), iz("A a(A b) throws B { return b; }"));
  }

  @Test public void negationPushdownTernary() {
    trimmingOf("a = !(b ? c: d)")//
        .using(PrefixExpression.class, new PrefixNotPushdown())//
        .gives("a = b ? !c : !d") //
    ;
  }
}
