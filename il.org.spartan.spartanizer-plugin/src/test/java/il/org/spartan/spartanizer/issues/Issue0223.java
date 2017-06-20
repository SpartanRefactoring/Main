package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;
import il.org.spartan.spartanizer.utils.*;

/** Unit tests for {@link ClassInstanceCreation}
 * @author Yossi Gil
 * @since 2016 */
//
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
  }
  @Test public void b010init() {
    a$040_init();
  }
  @Test public void b020findFirst() {
    a$040_init();
    azzert.that(findMe(context), instanceOf(SUBJECT_CLASS));
  }
  @Test public void b030canSuggest() {
    a$040_init();
    assert tipper.check(focus);
  }
  @Test public void b030demands() {
    a$040_init();
    assert tipper.check(focus);
  }
  @Test public void b040tipNonNull() {
    a$040_init();
    assert tipper.tip(focus) != null;
  }
  @Test public void b050ConfigCanFindTipper() {
    a$040_init();
    assert Toolbox.all().firstTipper(focus) != null;
  }
  @Test public void b060ConfigCanFindFindCorrectTipper() {
    a$040_init();
    azzert.that(Toolbox.all().firstTipper(focus), instanceOf(tipper.getClass()));
  }
  @Test public void b070callSuggest() {
    a$040_init();
    tipper.tip(focus);
  }
  @Test public void b080descriptionNonNull() {
    a$040_init();
    assert tipper.tip(focus).description != null;
  }
  @Test public void b090suggestNonNull() {
    a$040_init();
    assert tipper.tip(focus) != null;
  }
  @Test public void b100descriptionContains() {
    a$040_init();
    azzert.that(tipper.tip(focus).description, containsString(focus.getType() + ""));
  }
  @Test public void b110rangeNotEmpty() {
    a$040_init();
    assert !tipper.tip(focus).highlight.isEmpty();
  }
  @Test public void b120findTipperNotEmpty() {
    a$040_init();
    assert Toolbox.all().firstTipper(focus) != null;
  }
  @Test public void b130findTipperOfCorretType() {
    a$040_init();
    azzert.that(Toolbox.all().firstTipper(focus), instanceOf(ReplaceCurrentNode.class));
  }
  @Test public void b140findTipperDemands() {
    a$040_init();
    assert Toolbox.all().firstTipper(focus).check(focus);
  }
  @Test public void b150findTipperCanSuggest() {
    a$040_init();
    assert Toolbox.all().firstTipper(focus).check(focus);
  }
  @Test public void b160findTipperReplacmenentNonNull() {
    a$040_init();
    assert ((ReplaceCurrentNode<ClassInstanceCreation>) Toolbox.all().firstTipper(focus)).replacement(focus) != null;
  }
  private ClassInstanceCreation findMe(final Statement c) {
    return findFirst.instanceOf(SUBJECT_CLASS).in(c);
  }
  @Test public void replaceClassInstanceCreationWithFactoryInfixExpression() {
    trimmingOf("Integer x = new Integer(1 + 9);")//
        .using(new ClassInstanceCreationBoxedValueTypes(), ClassInstanceCreation.class) //
        .gives("Integer x = Integer.valueOf(1+9);")//
        .gives("Integer.valueOf(1+9);")//
        .gives("Integer.valueOf(10);")//
        .stays();
  }
  @Test public void a1() {
    trimmingOf("Integer x = new Integer(a);")//
        .using(new ClassInstanceCreationBoxedValueTypes(), ClassInstanceCreation.class) //
        .gives("Integer x = Integer.valueOf(a);")//
    ;
  }
  @Test public void a2() {
    trimmingOf("new Integer(a);")//
        .using(new ClassInstanceCreationBoxedValueTypes(), ClassInstanceCreation.class) //
        .gives("Integer.valueOf(a);")//
    ;
  }
  @Test public void replaceClassInstanceCreationWithFactoryInvokeMethode() {
    trimmingOf("String x = new String(f());")//
        .gives("new String(f());").gives("String.valueOf(f());");
  }
  @Test public void vanilla() {
    trimmingOf("new Integer(3)")//
        .gives("Integer.valueOf(3)")//
        .stays();
  }
  @Test public void vanilla01() {
    trimmingOf("new Integer(3)")//
        .gives("Integer.valueOf(3)");
  }
  @Test public void vanilla02() {
    final TestOperand a = trimmingOf("new Integer(3)");
    assert "Integer.valueOf(3)" != null;
    final String wrap = WrapIntoComilationUnit.find(a.get()).on(a.get());
    if (wrap.equals(trim.apply(new TraversalImplementation(), wrap)))
      azzert.fail("Nothing done on " + a.get());
  }
  @Test public void vanilla03() {
    final TestOperand a = trimmingOf("new Integer(3)");
    final String wrap = WrapIntoComilationUnit.find(a.get()).on(a.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Document $ = trim.rewrite(new TraversalImplementation(), u, d);
    assert $ != null;
    if (wrap.equals($.get()))
      azzert.fail("Nothing done on " + a.get());
  }
  @Test public void vanilla04() {
    final TestOperand o = trimmingOf("new Integer(3)");
    final String wrap = WrapIntoComilationUnit.find(o.get()).on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final IDocument d = new Document(wrap);
    assert d != null;
    final Traversal a = new TraversalImplementation();
    try {
      a.go(u).rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }
  @Test public void vanilla05() {
    final TestOperand o = trimmingOf("new Integer(3)");
    final String wrap = WrapIntoComilationUnit.find(o.get()).on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final IDocument d = new Document(wrap);
    assert d != null;
    final TraversalImplementation a = new TraversalImplementation();
    try {
      a.go(u).rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }
}
