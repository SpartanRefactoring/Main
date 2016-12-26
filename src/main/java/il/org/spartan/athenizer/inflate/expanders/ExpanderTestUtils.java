package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TESTUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.athenizer.inflate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Testing utils for expander Issue #961
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-19 */
public class ExpanderTestUtils {
  public static final TextEditGroup g = new TextEditGroup("");

  public static class Operand extends Wrapper<String> {
    public Operand(final String inner) {
      super(inner);
    }

    public Operand gives(final String $) {
      assert $ != null;
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get());
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
      final ASTRewrite r = ASTRewrite.create(u.getAST());
      SingleFlater.in(u).from(new InflaterProvider()).go(r, g);
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

    private void checkSame() {
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get());
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
      final ASTRewrite r = ASTRewrite.create(u.getAST());
      SingleFlater.in(u).from(new InflaterProvider()).go(r, g);
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

    public void stays() {
      checkSame();
    }
  }

  public static Operand expandingOf(final String from) {
    return new Operand(from);
  }
}
