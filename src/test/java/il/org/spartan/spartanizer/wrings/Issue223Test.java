package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.spartanizations.*;
import il.org.spartan.spartanizer.wringing.*;
import il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

/** Unit tests for {@link ClassInstanceCreation}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue223Test {
  private static final Class<ClassInstanceCreation> SUBJECT_CLASS = ClassInstanceCreation.class;
  private static final String INPUT = "return new Integer(f());";
  Wring<ClassInstanceCreation> wring;
  Statement context;
  ClassInstanceCreation focus;

  @Test public void A$010_createWring() {
    wring = makeWring();
    assert wring != null;
  }

  @Test public void A$020_CreateContext() {
    context = into.s(INPUT);//
    assert context != null;
  }

  @Test public void A$030_FindFocus() {
    A$020_CreateContext();
    focus = findMe(context);
    assert focus != null;
  }

  @Test public void A$040_init() {
    A$010_createWring();
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
    assert wring.canSuggest(focus);
  }

  @Test public void B$030demands() {
    A$040_init();
    assert wring.demandsToSuggestButPerhapsCant(focus);
  }

  @Test public void B$040suggestionNotNull() {
    A$040_init();
    assert wring.suggest(focus) != null;
  }

  @Test public void B$050toolboxCanFindWring() {
    A$040_init();
    final Wring<?> w = Toolbox.defaultInstance().find(focus);
    assert w != null;
  }

  @Test public void B$060toolboxCanFindFindCorrectWring() {
    A$040_init();
    final Wring<?> w = Toolbox.defaultInstance().find(focus);
    azzert.that(w, instanceOf(wring.getClass()));
  }

  @Test public void B$070callSuggest() {
    A$040_init();
    wring.suggest(focus);
  }

  @Test public void B$080descriptionNotNull() {
    A$040_init();
    assert wring.suggest(focus).description != null;
  }

  @Test public void B$090suggestNotNull() {
    A$040_init();
    assert wring.suggest(focus) != null;
  }

  @Test public void B$100descriptionContains() {
    A$040_init();
    azzert.that(wring.suggest(focus).description, //
        containsString(focus.getType() + ""));
  }

  @Test public void B$110rangeNotEmpty() {
    A$040_init();
    assert !wring.suggest(focus).isEmpty();
  }

  @Test public void B$120findWringNotEmpty() {
    A$040_init();
    assert Toolbox.defaultInstance().find(focus) != null;
  }

  @Test public void B$130findWringOfCorretType() {
    A$040_init();
    final Wring<ClassInstanceCreation> w = Toolbox.defaultInstance().find(focus);
    azzert.that(w, instanceOf(ReplaceCurrentNode.class));
  }

  @Test public void B$140findWringDemands() {
    A$040_init();
    final ReplaceCurrentNode<ClassInstanceCreation> w = (ReplaceCurrentNode<ClassInstanceCreation>) Toolbox.defaultInstance().find(focus);
    assert w.demandsToSuggestButPerhapsCant(focus);
  }

  @Test public void B$150findWringCanSuggest() {
    A$040_init();
    final ReplaceCurrentNode<ClassInstanceCreation> w = (ReplaceCurrentNode<ClassInstanceCreation>) Toolbox.defaultInstance().find(focus);
    assert w.canSuggest(focus);
  }

  @Test public void B$160findWringReplacmenentNotNull() {
    A$040_init();
    final ReplaceCurrentNode<ClassInstanceCreation> w = (ReplaceCurrentNode<ClassInstanceCreation>) Toolbox.defaultInstance().find(focus);
    assert w.replacement(focus) != null;
  }

  @Ignore @Test public void replaceClassInstanceCreationWithFactoryInfixExpression() {
    trimming("Integer x = new Integer(1 + 9);")//
        .to("Integer x = new Integer(10);")//
        .to("Integer x = Integer.valueOf(10);")//
        .stays();
  }

  @Ignore @Test public void replaceClassInstanceCreationWithFactoryInvokeMethode() {
    trimming("String x = new String(f());").to("String x = String.valueOf(f());");
  }

  @Ignore @Test public void vanilla() {
    trimming("new Integer(3)").to("Integer.valueOf(3)").stays();
  }

  @Ignore @Test public void vanilla01() {
    trimming("new Integer(3)").to("Integer.valueOf(3)");
  }

  @Ignore @Test public void vanilla02() {
    final Operand a = trimming("new Integer(3)");
    assert "Integer.valueOf(3)" != null;
    final Wrap w = Wrap.find(a.get());
    final String wrap = w.on(a.get());
    final String unpeeled = TrimmerTestsUtils.apply(new Trimmer(), wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + a.get());
  }

  @Ignore @Test public void vanilla03() {
    final Operand a = trimming("new Integer(3)");
    final Wrap w = Wrap.find(a.get());
    final String wrap = w.on(a.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Document $ = TESTUtils.rewrite(new Trimmer(), u, d);
    assert $ != null;
    final String unpeeled = $.get();
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + a.get());
  }

  @Ignore @Test public void vanilla04() {
    final Operand o = trimming("new Integer(3)");
    final Wrap w = Wrap.find(o.get());
    final String wrap = w.on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Trimmer a = new Trimmer();
    try {
      final ASTRewrite x = a.createRewrite(u, wizard.nullProgressMonitor);
      x.rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
    assert d != null;
    final String unpeeled = d.get();
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + o.get());
  }

  private ClassInstanceCreation findMe(final Statement c) {
    return findFirst.instanceOf(SUBJECT_CLASS, c);
  }

  private ClassInstanceCreationValueTypes makeWring() {
    return new ClassInstanceCreationValueTypes();
  }
}
