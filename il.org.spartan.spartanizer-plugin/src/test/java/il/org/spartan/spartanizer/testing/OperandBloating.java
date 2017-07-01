package il.org.spartan.spartanizer.testing;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.testing.TestUtilsAll.*;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.athenizer.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.utils.*;

/** Part of the test utils for bloaters\zoomers
 * @author Dor Ma'ayan
 * @since 2017-06-17 */
public class OperandBloating extends TestOperand {
  ASTNode ast;
  String xclassText;
  boolean needRenaming = true;

  public OperandBloating(final String inner) {
    super(inner);
    traversal.toolbox = InflaterProvider.freshCopyOfAllExpanders();
  }
  public OperandBloating(final ASTNode inner, final String classText) {
    this(classText);
    ast = inner;
  }
  public OperandBloating needRenaming(final boolean ¢) {
    needRenaming = ¢;
    return this;
  }
  @Override protected void copyPasteReformat(final String format, final Object... os) {
    rerun();
    System.err.printf(QUICK + format, os);
    System.err.println(NEW_UNIT_TEST + JUnitTestMethodFacotry.makeBloaterUnitTest(get()));
  }
  public static String bloat(final String source) {
    final WrapIntoComilationUnit ret = WrapIntoComilationUnit.find(source);
    final String wrap = ret.on(source);
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    SingleFlater.in(u).from(new InflaterProvider()).go(r, TestUtilsBloating.textEditGroup);
    try {
      final IDocument $ = new Document(wrap);
      r.rewriteAST($, null).apply($);
      return ret.off($.get());
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      return note.bug(¢);
    }
  }
  @Override public OperandBloating gives(final String $) {
    assert $ != null;
    final WrapIntoComilationUnit w = WrapIntoComilationUnit.find(get());
    final String wrap = w.on(get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    SingleFlater.in(u).from(new InflaterProvider(traversal.toolbox).provideAll()).go(r, TestUtilsBloating.textEditGroup);
    try {
      final IDocument doc = new Document(wrap);
      r.rewriteAST(doc, null).apply(doc);
      final String ret, peeled1;
      if (!needRenaming) {
        ret = makeAST.COMPILATION_UNIT.from(WrapIntoComilationUnit.find($).on($)) + "";
        peeled1 = w.off(makeAST.COMPILATION_UNIT.from(doc.get()) + "");
      } else {
        ret = rename((CompilationUnit) makeAST.COMPILATION_UNIT.from(WrapIntoComilationUnit.find($).on($))) + "";
        peeled1 = w.off(rename((CompilationUnit) makeAST.COMPILATION_UNIT.from(doc.get())) + "");
      }
      if (peeled1.equals(get()))
        azzert.that("No Bloating of " + get(), peeled1, is(not(get())));
      if (tide.clean(peeled1).equals(tide.clean(get())))
        azzert.that("Bloatong of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled1))));
      if (ret.equals(peeled1) || Trivia.essence(peeled1).equals(Trivia.essence(ret)))
        return new OperandBloating(ret);
      copyPasteReformat("  .gives(\"%s\") //\nCompare with\n .gives(\"%s\") //\n", Trivia.escapeQuotes(Trivia.essence(peeled1)),
          Trivia.escapeQuotes(Trivia.essence(ret)));
      azzert.that(Trivia.essence(peeled1), is(Trivia.essence(ret)));
      return new OperandBloating(ret);
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      note.bug(this, ¢);
    }
    return null;
  }
  public OperandBloating givesWithBinding(final String $) {
    assert $ != null;
    final CompilationUnit u = az.compilationUnit(ast);
    final String wrap = get();
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    SingleFlater.in(u).usesDisabling(false).from(new InflaterProvider(traversal.toolbox)).go(r, TestUtilsBloating.textEditGroup);
    try {
      final String $1 = rename((CompilationUnit) makeAST.COMPILATION_UNIT.from(WrapIntoComilationUnit.find($).on($))) + "";
      final IDocument doc = new Document(wrap);
      r.rewriteAST(doc, null).apply(doc);
      final String ret = rename((CompilationUnit) makeAST.COMPILATION_UNIT.from(doc)) + "";
      if (wrap.equals(ret))
        azzert.fail("Nothing done on " + get());
      if (ret.equals(get()))
        azzert.that("No trimming of " + get(), ret, is(not(get())));
      assertSimilar($1, ret);
      return new OperandBloating(createCUWithBinding(ret), ret);
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      note.bug(¢);
    }
    return null;
  }
  /** @param $ java code
   * @param f tested method name. expanders will be applied only for this method
   * @return */
  public OperandBloating givesWithBinding(final String $, final String f) {
    assert $ != null;
    final CompilationUnit u = az.compilationUnit(ast);
    final String wrap = get();
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    MethodDeclaration m = getMethod(u, f);
    SingleFlater.in(m).usesDisabling(false).from(new InflaterProvider(traversal.toolbox)).go(r, TestUtilsBloating.textEditGroup);
    try {
      final IDocument doc = new Document(wrap);
      r.rewriteAST(doc, null).apply(doc);
      final String ret = doc.get();
      if (wrap.equals(ret))
        azzert.fail("Nothing done on " + get());
      if (ret.equals(get()))
        azzert.that("No trimming of " + get(), ret, is(not(get())));
      m = getMethod(az.compilationUnit(makeAST.COMPILATION_UNIT.from(ret)), f);
      assertSimilar($, m + "");
      final ASTParser p = make.COMPILATION_UNIT.parser(ret);
      p.setResolveBindings(true);
      return new OperandBloating(az.compilationUnit(p.createAST(null)), ret);
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      note.bug(this, ¢);
    }
    return null;
  }
  /** Rename all the SimpleNames in a compilation-unit to toList consistent
   * names : v1,v2,....
   * @author Dor Ma'ayan
   * @since 19-01-2017
   * @param b
   * @return */
  private static CompilationUnit rename(final CompilationUnit u) {
    if (u == null)
      return null;
    TestUtilsBloating.counter = 0;
    final CompilationUnit ret = copy.of(u);
    ret.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode an) {
        if (!iz.simpleName(an))
          return;
        az.simpleName(an).setIdentifier("v" + TestUtilsBloating.counter);
        ++TestUtilsBloating.counter;
      }
    });
    return ret;
  }
  private static MethodDeclaration getMethod(final CompilationUnit u, final String f) {
    final List<MethodDeclaration> ret = descendants.whoseClassIs(MethodDeclaration.class).suchThat(λ -> λ.getName().getIdentifier().equals(f)).from(u);
    if (ret.isEmpty())
      azzert.fail("No such method Exists");
    return the.firstOf(ret);
  }
  private static CompilationUnit createCUWithBinding(final String text) {
    final ASTParser ret = make.COMPILATION_UNIT.parser(text);
    ret.setResolveBindings(true);
    return az.compilationUnit(ret.createAST(null));
  }
  public void staysWithBinding() {
    final String wrap = get();
    final CompilationUnit u = az.compilationUnit(ast);
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    SingleFlater.in(u).from(new InflaterProvider(traversal.toolbox)).go(r, TestUtilsBloating.textEditGroup);
    try {
      final IDocument doc = new Document(wrap);
      r.rewriteAST(doc, null).apply(doc);
      checkDiff(wrap, doc.get());
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      note.bug(this, ¢);
    }
  }
  public void staysWithBinding(final String f) {
    final CompilationUnit u = az.compilationUnit(ast);
    final String wrap = get();
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    final MethodDeclaration m = getMethod(u, f);
    SingleFlater.in(m).usesDisabling(false).from(new InflaterProvider(traversal.toolbox)).go(r, TestUtilsBloating.textEditGroup);
    try {
      final IDocument doc = new Document(wrap);
      r.rewriteAST(doc, null).apply(doc);
      checkDiff(wrap, doc.get());
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      note.bug(this, ¢);
    }
  }
  public void checkDiff(final String wrap, final String unpeeled) {
    if (!wrap.equals(unpeeled) && !Trivia.essence(get()).equals(Trivia.essence(unpeeled)) && !unpeeled.equals(get()) && unpeeled.equals(get()))
      assertSimilar(get(), unpeeled);
  }
}
