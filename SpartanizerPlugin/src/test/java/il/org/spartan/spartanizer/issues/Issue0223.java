package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.dispatch.Utils;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Unit tests for {@link ClassInstanceCreation}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue0223 {
  private static final Class<ClassInstanceCreation> SUBJECT_CLASS = ClassInstanceCreation.class;
  private static final String INPUT = "return new Integer(f());";
  Tipper<ClassInstanceCreation> tipper;
  Statement context;
  ClassInstanceCreation focus;

  @Test public void a$010_createTipper() {
    tipper = makeTipper();
    assert tipper != null;
  }

  private Tipper<ClassInstanceCreation> makeTipper() {
    return new ClassInstanceCreationBoxedValueTypes();
  }

  @Test public void a$020_CreateContext() {
    context = into.s(INPUT);
    assert context != null;
  }

  @Test public void a$030_FindFocus() {
    a$020_CreateContext();
    focus = findMe(context);
    assert focus != null;
  }

  @Test public void a$040_init() {
    a$010_createTipper();
    a$020_CreateContext();
    a$030_FindFocus();
    Utils.refresh();
  }

  @Test public void B$010init() {
    a$040_init();
  }

  @Test public void B$020findFirst() {
    a$040_init();
    azzert.that(findMe(context), instanceOf(SUBJECT_CLASS));
  }

  @Test public void B$030canSuggest() {
    a$040_init();
    assert tipper.check(focus);
  }

  @Test public void B$030demands() {
    a$040_init();
    assert tipper.check(focus);
  }

  @Test public void B$040tipNotNull() {
    a$040_init();
    assert tipper.tip(focus) != null;
  }

  @Test public void B$050toolboxCanFindTipper() {
    a$040_init();
    assert Utils.defaultInstance().firstTipper(focus) != null;
  }

  @Test public void B$060toolboxCanFindFindCorrectTipper() {
    a$040_init();
    azzert.that(Utils.defaultInstance().firstTipper(focus), instanceOf(tipper.getClass()));
  }

  @Test public void B$070callSuggest() {
    a$040_init();
    tipper.tip(focus);
  }

  @Test public void B$080descriptionNotNull() {
    a$040_init();
    assert tipper.tip(focus).description != null;
  }

  @Test public void B$090suggestNotNull() {
    a$040_init();
    assert tipper.tip(focus) != null;
  }

  @Test public void B$100descriptionContains() {
    a$040_init();
    azzert.that(tipper.tip(focus).description, containsString(focus.getType() + ""));
  }

  @Test public void B$110rangeNotEmpty() {
    a$040_init();
    assert !tipper.tip(focus).highlight.isEmpty();
  }

  @Test public void B$120findTipperNotEmpty() {
    a$040_init();
    assert Utils.defaultInstance().firstTipper(focus) != null;
  }

  @Test public void B$130findTipperOfCorretType() {
    a$040_init();
    azzert.that(Utils.defaultInstance().firstTipper(focus), instanceOf(ReplaceCurrentNode.class));
  }

  @Test public void B$140findTipperDemands() {
    a$040_init();
    assert Utils.defaultInstance().firstTipper(focus).check(focus);
  }

  @Test public void B$150findTipperCanSuggest() {
    a$040_init();
    assert Utils.defaultInstance().firstTipper(focus).check(focus);
  }

  @Test public void B$160findTipperReplacmenentNotNull() {
    a$040_init();
    assert ((ReplaceCurrentNode<ClassInstanceCreation>) Utils.defaultInstance().firstTipper(focus)).replacement(focus) != null;
  }

  private ClassInstanceCreation findMe(final Statement c) {
    return findFirst.instanceOf(SUBJECT_CLASS).in(c);
  }

  @Test public void replaceClassInstanceCreationWithFactoryInfixExpression() {
    trimminKof("Integer x = new Integer(1 + 9);")//
        .using(new ClassInstanceCreationBoxedValueTypes(), ClassInstanceCreation.class) //
        .gives("Integer x = Integer.valueOf(1+9);")//
        .gives("Integer.valueOf(1+9);")//
        .gives("Integer.valueOf(10);")//
        .stays();
  }

  @Test public void a1() {
    trimminKof("Integer x = new Integer(a);")//
        .using(new ClassInstanceCreationBoxedValueTypes(), ClassInstanceCreation.class) //
        .gives("Integer x = Integer.valueOf(a);")//
    ;
  }

  @Test public void a2() {
    trimminKof("new Integer(a);")//
        .using(new ClassInstanceCreationBoxedValueTypes(), ClassInstanceCreation.class) //
        .gives("Integer.valueOf(a);")//
    ;
  }

  @Test public void replaceClassInstanceCreationWithFactoryInvokeMethode() {
    trimminKof("String x = new String(f());")//
        .gives("new String(f());").gives("String.valueOf(f());");
  }

  @Test public void vanilla() {
    trimminKof("new Integer(3)")//
        .gives("Integer.valueOf(3)")//
        .stays();
  }

  @Test public void vanilla01() {
    trimminKof("new Integer(3)")//
        .gives("Integer.valueOf(3)");
  }

  @Test public void vanilla02() {
    final TrimmingOperand a = trimminKof("new Integer(3)");
    assert "Integer.valueOf(3)" != null;
    final String wrap = WrapIntoComilationUnit.find(a.get()).on(a.get());
    if (wrap.equals(trim.apply(new Trimmer(), wrap)))
      azzert.fail("Nothing done on " + a.get());
  }

  @Test public void vanilla03() {
    final TrimmingOperand a = trimminKof("new Integer(3)");
    final String wrap = WrapIntoComilationUnit.find(a.get()).on(a.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Document $ = trim.rewrite(new Trimmer(), u, d);
    assert $ != null;
    if (wrap.equals($.get()))
      azzert.fail("Nothing done on " + a.get());
  }

  @Test public void vanilla04() {
    final TrimmingOperand o = trimminKof("new Integer(3)");
    final String wrap = WrapIntoComilationUnit.find(o.get()).on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final IDocument d = new Document(wrap);
    assert d != null;
    final Trimmer a = new Trimmer();
    try {
      a.createRewrite(u).rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }

  @Test public void vanilla05() {
    final TrimmingOperand o = trimminKof("new Integer(3)");
    final String wrap = WrapIntoComilationUnit.find(o.get()).on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final IDocument d = new Document(wrap);
    assert d != null;
    final Trimmer a = new Trimmer();
    try {
      a.computeMaximalRewrite(u).rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }
}
