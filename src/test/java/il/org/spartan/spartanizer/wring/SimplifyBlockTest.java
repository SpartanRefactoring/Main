package il.org.spartan.spartanizer.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.spartanizations.TESTUtils.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.apply;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.spartanizations.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Unit tests for {@link NameYourClassHere}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
@Ignore("Still problems with #205")
public class SimplifyBlockTest {
  @Test public void complexEmpty0() {
    trimming("{;}").to("/* empty */    ");
  }

  @Test public void complexEmpty0A() {
    trimming("{}").to("/* empty */");
  }

  @Test public void complexEmpty0B() {
    trimming("{;}").to("/* empty */");
  }

  @Test public void complexEmpty0C() {
    trimming("{{;}}").to("/* empty */");
  }

  @Test public void complexEmpty0D() {
    trimming("{;;;{;;;}{;}}").to("/* empty */    ");
  }

  @Test public void complexEmpty1() {
    trimming("{;;{;{{}}}{}{};}").to("/* empty */ ");
  }

  @Test public void complexSingleton() {
    assertSimplifiesTo("{;{{;;return b; }}}", "return b;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void deeplyNestedReturn() {
    assertSimplifiesTo("{{{;return c;};;};}", "return c;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void empty() {
    assertSimplifiesTo("{;;}", "", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void emptySimpler() {
    assertSimplifiesTo("{;}", "", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void emptySimplest() {
    assertSimplifiesTo("{}", "", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void emptySimplestA() {
    final Wrap w = Wrap.Statement;
    final String wrap = w.on("{}");
    final String unpeeled = apply(new BlockSimplify(), wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + "{}");
    final String peeled = w.off(unpeeled);
    if (peeled.equals("{}"))
      azzert.that("No similification of " + "{}", peeled, is(not("{}")));
    if (tide.clean(peeled).equals(tide.clean("{}")))
      azzert.that("Simpification of " + "{}" + " is just reformatting", tide.clean("{}"), is(not(tide.clean(peeled))));
    assertSimilar("", peeled);
  }

  @Test public void emptySimplestB() {
    final Wrap w = Wrap.Statement;
    final String wrap = w.on("{}");
    apply(new BlockSimplify(), wrap);
  }

  @Test public void emptySimplestC() {
    final Wrap w = Wrap.Statement;
    final String wrap = w.on("{}");
    apply(new BlockSimplify(), wrap);
  }

  @Test public void emptySimplestD() {
    apply(new BlockSimplify(), Wrap.Statement.on("{}"));
  }

  @Test public void emptySimplestE() {
    final String from = Wrap.Statement.on("{}");
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    final Document d = new Document(from);
    assert d != null;
    final Wring<Block> inner = new BlockSimplify();
    assert inner != null;
    final AsSpartanization s = new AsSpartanization(inner);
    assert s != null;
    emptySimplestE_Aux(u, d, s);
  }

  @Test public void expressionVsExpression() {
    trimming("6 - 7 < a * 3").to("-1 < 3 * a");
  }

  @Test public void literalVsLiteral() {
    trimming("if (a) return b; else c();").to("if(a)return b;c();");
  }

  @Test public void threeStatements() {
    assertSimplifiesTo("{i++;{{;;return b; }}j++;}", "i++;return b;j++;", new BlockSimplify(), Wrap.Statement);
  }

  private void emptySimplestE_Aux(final CompilationUnit u, final Document d, final AsSpartanization s) {
    try {
      s.rewriterOf(u, new NullProgressMonitor(), (IMarker) null).rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
  }
}
