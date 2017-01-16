package il.org.spartan.bloater.bloaters;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TESTUtils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.bloater.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Testing utils for expander Issue #961
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-19 */
public class BloatingTestUtilities {
  static final TextEditGroup textEditGroup = new TextEditGroup("");

  public static class Operand extends Wrapper<String> {
    ASTNode ast;
    String classText;

    public Operand(final String inner) {
      super(inner);
    }

    public Operand(final ASTNode inner, final String classText) {
      ast = inner;
      this.classText = classText;
    }

    public Operand gives(final String $) {
      assert $ != null;
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get());
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
      final ASTRewrite r = ASTRewrite.create(u.getAST());
      SingleFlater.in(u).from(new InflaterProvider()).go(r, textEditGroup);
      try {
        final Document doc = new Document(wrap);
        r.rewriteAST(doc, null).apply(doc);
        final String unpeeled = doc.get();
        if (wrap.equals(unpeeled))
          azzert.fail("Nothing done on " + get());
        final String peeled = w.off(unpeeled);
        if (peeled.equals(get()))
          azzert.that("No trimming of " + get(), peeled, is(not(get())));
        if (tide.clean(peeled).equals(tide.clean(get())))
          azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
        assertSimilar($, peeled);
        return new Operand($);
      } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
        ¢.printStackTrace();
      }
      return null;
    }

    public Operand givesWithBinding(final String $) {
      assert $ != null;
      final CompilationUnit u = az.compilationUnit(ast);
      final String wrap = classText;
      final ASTRewrite r = ASTRewrite.create(u.getAST());
      SingleFlater.in(u).usesDisabling(false).from(new InflaterProvider()).go(r, textEditGroup);
      try {
        final Document doc = new Document(wrap);
        r.rewriteAST(doc, null).apply(doc);
        final String unpeeled = doc.get();
        if (wrap.equals(unpeeled))
          azzert.fail("Nothing done on " + get());
        final String peeled = unpeeled;
        if (peeled.equals(get()))
          azzert.that("No trimming of " + get(), peeled, is(not(get())));
        assertSimilar($, peeled);
        return new Operand(createCUWithBinding(unpeeled), unpeeled);
      } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
        ¢.printStackTrace();
      }
      return null;
    }

    /** @param $ java code
     * @param f tested method name. expanders will be applied only for this
     *        method
     * @return */
    public Operand givesWithBinding(final String $, final String f) {
      assert $ != null;
      final CompilationUnit u = az.compilationUnit(ast);
      final String wrap = classText;
      final ASTRewrite r = ASTRewrite.create(u.getAST());
      MethodDeclaration m = getMethod(u, f);
      SingleFlater.in(m).usesDisabling(false).from(new InflaterProvider()).go(r, textEditGroup);
      try {
        final Document doc = new Document(wrap);
        r.rewriteAST(doc, null).apply(doc);
        final String unpeeled = doc.get();
        if (wrap.equals(unpeeled))
          azzert.fail("Nothing done on " + get());
        final String peeled = unpeeled;
        if (peeled.equals(get()))
          azzert.that("No trimming of " + get(), peeled, is(not(get())));
        m = getMethod(az.compilationUnit(makeAST.COMPILATION_UNIT.from(unpeeled)), f);
        assertSimilar($, m + "");
        final ASTParser p = Make.COMPILATION_UNIT.parser(unpeeled);
        p.setResolveBindings(true);
        return new Operand(az.compilationUnit(p.createAST(null)), unpeeled);
      } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
        ¢.printStackTrace();
      }
      return null;
    }

    private static MethodDeclaration getMethod(final CompilationUnit u, final String f) {
      final List<MethodDeclaration> $ = searchDescendants.forClass(MethodDeclaration.class).suchThat(t -> t.getName().getIdentifier().equals(f))
          .from(u);
      if ($.isEmpty())
        azzert.fail("Don't Such Method Exists");
      return $.get(0);
    }

    private static CompilationUnit createCUWithBinding(final String text) {
      final ASTParser $ = Make.COMPILATION_UNIT.parser(text);
      $.setResolveBindings(true);
      return az.compilationUnit($.createAST(null));
    }

    private void checkSame() {
      if (get().length() == 0)
        return;
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get());
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
      final ASTRewrite r = ASTRewrite.create(u.getAST());
      SingleFlater.in(u).from(new InflaterProvider()).go(r, textEditGroup);
      try {
        final Document doc = new Document(wrap);
        r.rewriteAST(doc, null).apply(doc);
        final String unpeeled = doc.get();
        if (wrap.equals(unpeeled))
          return;
        final String peeled = w.off(unpeeled);
        if (!peeled.equals(get()) && !tide.clean(peeled).equals(tide.clean(get())))
          assertSimilar(get(), peeled);
      } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
        ¢.printStackTrace();
      }
    }

    private void checkSameWithBinding() {
      final String wrap = classText;
      final CompilationUnit u = az.compilationUnit(ast);
      final ASTRewrite r = ASTRewrite.create(u.getAST());
      SingleFlater.in(u).from(new InflaterProvider()).go(r, textEditGroup);
      try {
        final Document doc = new Document(wrap);
        r.rewriteAST(doc, null).apply(doc);
        final String unpeeled = doc.get();
        if (wrap.equals(unpeeled))
          return;
        if (!unpeeled.equals(get()) && unpeeled.equals(get()))
          assertSimilar(get(), unpeeled);
      } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
        ¢.printStackTrace();
      }
    }

    public void stays() {
      checkSame();
    }

    public void staysWithBinding() {
      checkSameWithBinding();
    }
  }

  public static Operand bloatingOf(final String from) {
    return new Operand(from);
  }

  public static Operand bloatingOf(final ReflectiveTester ¢) {
    return new Operand(¢.myCompilationUnit(), ¢.myClassText());
  }
}
