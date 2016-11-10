package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Unit tests for {@link ClassInstanceCreation}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue223 {
  private static final Class<ClassInstanceCreation> SUBJECT_CLASS = ClassInstanceCreation.class;
  private static final String INPUT = "return new Integer(f());";
  Tipper<ClassInstanceCreation> tipper;
  Statement context;
  ClassInstanceCreation focus;

  @Test public void A$010_createTipper() {
    tipper = makeTipper();
    assert tipper != null;
  }
  @Test public void A$020_CreateContext() {
    context = into.s(INPUT);
    assert context != null;
  }
  @Test public void A$030_FindFocus() {
    A$020_CreateContext();
    focus = findMe(context);
    assert focus != null;
  }
  @Test public void A$040_init() {
    A$010_createTipper();
    A$020_CreateContext();
    A$030_FindFocus();
    Toolbox.refresh();
  }
  @Test public void B$010init() {
    A$040_init();
  }
  @Test public void B$020findFirst() {
    A$040_init();
    azzert.that(findMe(context), instanceOf(SUBJECT_CLASS));
  }
  @Test public void B$030canSuggest() {
    A$040_init();
    assert tipper.canTip(focus);
  }
  @Test public void B$030demands() {
    A$040_init();
    assert tipper.canTip(focus);
  }
  @Test public void B$040tipNotNull() {
    A$040_init();
    assert tipper.tip(focus) != null;
  }
  @Test public void B$050toolboxCanFindTipper() {
    A$040_init();
    assert Toolbox.defaultInstance().firstTipper(focus) != null;
  }
  @Test public void B$060toolboxCanFindFindCorrectTipper() {
    A$040_init();
    azzert.that(Toolbox.defaultInstance().firstTipper(focus), instanceOf(tipper.getClass()));
  }
  @Test public void B$070callSuggest() {
    A$040_init();
    tipper.tip(focus);
  }
  @Test public void B$080descriptionNotNull() {
    A$040_init();
    assert tipper.tip(focus).description != null;
  }
  @Test public void B$090suggestNotNull() {
    A$040_init();
    assert tipper.tip(focus) != null;
  }
  @Test public void B$100descriptionContains() {
    A$040_init();
    azzert.that(tipper.tip(focus).description, containsString(focus.getType() + ""));
  }
  @Test public void B$110rangeNotEmpty() {
    A$040_init();
    assert !tipper.tip(focus).isEmpty();
  }
  @Test public void B$120findTipperNotEmpty() {
    A$040_init();
    assert Toolbox.defaultInstance().firstTipper(focus) != null;
  }
  @Test public void B$130findTipperOfCorretType() {
    A$040_init();
    azzert.that(Toolbox.defaultInstance().firstTipper(focus), instanceOf(ReplaceCurrentNode.class));
  }
  @Test public void B$140findTipperDemands() {
    A$040_init();
    assert ((ReplaceCurrentNode<ClassInstanceCreation>) Toolbox.defaultInstance().firstTipper(focus)).canTip(focus);
  }
  @Test public void B$150findTipperCanSuggest() {
    A$040_init();
    assert ((ReplaceCurrentNode<ClassInstanceCreation>) Toolbox.defaultInstance().firstTipper(focus)).canTip(focus);
  }
  @Test public void B$160findTipperReplacmenentNotNull() {
    A$040_init();
    assert ((ReplaceCurrentNode<ClassInstanceCreation>) Toolbox.defaultInstance().firstTipper(focus)).replacement(focus) != null;
  }
  @Test public void replaceClassInstanceCreationWithFactoryInfixExpression() {
    trimmingOf("Integer x = new Integer(1 + 9);").gives("Integer x = Integer.valueOf(1+9);").gives("Integer x = Integer.valueOf(10);").stays();
  }
  @Test public void replaceClassInstanceCreationWithFactoryInvokeMethode() {
    trimmingOf("String x = new String(f());").gives("String x = String.valueOf(f());");
  }
  @Test public void vanilla() {
    trimmingOf("new Integer(3)").gives("Integer.valueOf(3)").stays();
  }
  @Test public void vanilla01() {
    trimmingOf("new Integer(3)").gives("Integer.valueOf(3)");
  }
  @Test public void vanilla02() {
    final Operand a = trimmingOf("new Integer(3)");
    assert "Integer.valueOf(3)" != null;
    final Wrap w = Wrap.find(a.get());
    final String wrap = w.on(a.get());
    if (wrap.equals(TrimmerTestsUtils.applyTrimmer(new Trimmer(), wrap)))
      azzert.fail("Nothing done on " + a.get());
  }
  @Test public void vanilla03() {
    final Operand a = trimmingOf("new Integer(3)");
    final Wrap w = Wrap.find(a.get());
    final String wrap = w.on(a.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Document $ = TESTUtils.rewrite(new Trimmer(), u, d);
    assert $ != null;
    if (wrap.equals($.get()))
      azzert.fail("Nothing done on " + a.get());
  }
  @Test public void vanilla04() {
    final Operand o = trimmingOf("new Integer(3)");
    final Wrap w = Wrap.find(o.get());
    final String wrap = w.on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Trimmer a = new Trimmer();
    try {
      a.createRewrite(u).rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }
  @Test public void vanilla05() {
    final Operand o = trimmingOf("new Integer(3)");
    final Wrap w = Wrap.find(o.get());
    final String wrap = w.on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Trimmer a = new Trimmer();
    try {
      final IProgressMonitor pm = wizard.nullProgressMonitor;
      pm.beginTask("Creating rewrite operation...", IProgressMonitor.UNKNOWN);
      final ASTRewrite $ = ASTRewrite.create(u.getAST());
      a.consolidateTips($, u, (IMarker) null);
      pm.done();
      $.rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }
  private ClassInstanceCreation findMe(final Statement c) {
    return findFirst.instanceOf(SUBJECT_CLASS, c);
  }
  private ClassInstanceCreationValueTypes makeTipper() {
    return new ClassInstanceCreationValueTypes();
  }
}
