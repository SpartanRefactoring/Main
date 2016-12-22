package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TESTUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.athenizer.inflate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Testing utils for expander
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-19 */
public class ExpanderTestUtils {
  public static final TextEditGroup g = null;
  public static final ASTRewrite r = null;

  public static class Operand extends Wrapper<String> {

    public Operand(final String inner) {
      super(inner);
    }

    public Operand gives(final String $) {
      assert $ != null;
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get());
      //final String unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
      SingleFlater singleFlater = SingleFlater.in(u).from(new InflaterProvider());
      singleFlater.go(new Trimmer().createRewrite(u), new TextEditGroup(""));
      String unpeeled = u + "";
      if (wrap.equals(unpeeled))
        azzert.fail("Nothing done on " + get());
      final String peeled = w.off(unpeeled);
      if (peeled.equals(get()))
        azzert.that("No trimming of " + get(), peeled, is(not(get())));
      if (tide.clean(peeled).equals(tide.clean(get())))
        azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
      assertSimilar($, peeled);
      return new Operand($);
      //      assert $ != null;
//      final Wrap w = Wrap.find(get());
//      final String wrap = w.on(get());
//      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
//      SingleFlater singleFlater = SingleFlater.in(u).from(new InflaterProvider());
//      singleFlater.go(new Trimmer().createRewrite(u), g);
//      //final String unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
//      String unpeeled = u + "";
//      if ($.equals(unpeeled))
//        azzert.fail("Nothing done on " + get());
//      final String peeled = w.off(unpeeled);
//      if ($.equals(get()))
//        azzert.that("No trimming of " + get(), peeled, is(not(get())));
//      if (tide.clean($).equals(tide.clean(get())))
//        azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
//      assertSimilar($, unpeeled);
//      return new Operand($);
    }

    private void checkSame() {
      final Wrap w = Wrap.find(get());
      final String wrap = w.on(get());
      //final String unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
      SingleFlater singleFlater = SingleFlater.in(u);
      singleFlater.go(r, g);
      //final String unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
      String unpeeled = u + "";
      if (wrap.equals(unpeeled))
        return;
      final String peeled = w.off(unpeeled);
      if (!peeled.equals(get()) && !tide.clean(peeled).equals(tide.clean(get())))
        assertSimilar(get(), peeled);
    }

    public void stays() {
      checkSame();
    }
   
  }
  
  public static Operand expandingOf(final String from) {
    return new Operand(from);
  }
}
