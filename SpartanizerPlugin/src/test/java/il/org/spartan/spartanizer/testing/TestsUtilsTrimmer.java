package il.org.spartan.spartanizer.testing;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

public enum TestsUtilsTrimmer {
  ;
  public static int countOpportunities(final AbstractGUIApplicator a, final CompilationUnit u) {
    return a.collectSuggestions(u).size();
  }

  public static <N extends ASTNode> OperandToTipper<N> included(final String from, final Class<N> clazz) {
    return new OperandToTipper<>(from, clazz);
  }

  public static Operand trimmingOf(final String from) {
    return new Operand(from);
  }

  public static class OperandToTipper<N extends ASTNode> extends Operand {
    final Class<N> clazz;

    OperandToTipper(final String from, final Class<N> clazz) {
      super(from);
      this.clazz = clazz;
    }

    private N findNode(final Rule<N, Tip> t) {
      assert t != null;
      final Wrap wrap = Wrap.find(get());
      assert wrap != null;
      final CompilationUnit u = wrap.intoCompilationUnit(get());
      assert u != null;
      final N $ = firstInstance(u);
      assert $ != null;
      return $;
    }

    private N firstInstance(final CompilationUnit u) {
      final Wrapper<N> $ = new Wrapper<>();
      u.accept(new ASTVisitor(true) {
        /** The implementation of the visitation procedure in the JDT seems to
         * be buggy. Each time we find a node which is an instance of the sought
         * class, we return false. Hence, we do not anticipate any further calls
         * to this function after the first such node is found. However, this
         * does not seem to be the case. So, in the case our wrapper is not
         * null, we do not carry out any further tests.
         * @param pattern the node currently being visited.
         * @return <code><b>true</b></code> <i>iff</i> the sought node is
         *         found. */
        @Override @SuppressWarnings("unchecked") public boolean preVisit2(final ASTNode ¢) {
          if ($.get() != null)
            return false;
          if (!clazz.isAssignableFrom(¢.getClass()))
            return true;
          $.set((N) ¢);
          return false;
        }
      });
      return $.get();
    }

    public OperandToTipper<N> in(final Rule<N, Tip> ¢) {
      assert ¢.interesting(findNode(¢));
      return this;
    }

    public OperandToTipper<N> notIn(final Rule<N, Tip> ¢) {
      assert !¢.interesting(findNode(¢));
      return this;
    }
  }
}
