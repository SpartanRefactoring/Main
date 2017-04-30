package il.org.spartan.spartanizer.testing;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.traversal.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

public enum TestsUtilsSpartanizer {
  ;
  public static int countOpportunities(final Traversal t, final CompilationUnit u) {
    return t.collectTips(u).size();
  }

  public static <N extends ASTNode> OperandToTipper<N> included(final String from, final Class<N> clazz) {
    return new OperandToTipper<>(from, clazz);
  }

  /** About four hundred tests depend on a particular trimming policy. We shall
   * call it {@link #trimmingOf(String)} */
  public static TestOperand trimmingOf(final String from) {
    return new TestOperand(from);
  }

  public static class OperandToTipper<N extends ASTNode> extends TestOperand {
    final Class<N> clazz;

    OperandToTipper(final String from, final Class<N> clazz) {
      super(from);
      this.clazz = clazz;
    }

    private N findNode(final Rule<N, Tip> t) {
      assert t != null;
      final WrapIntoComilationUnit wrapIntoComilationUnit = WrapIntoComilationUnit.find(get());
      assert wrapIntoComilationUnit != null;
      final CompilationUnit u = wrapIntoComilationUnit.intoCompilationUnit(get());
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
         * @return whether the sought node is found. */
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
      assert ¢.check(findNode(¢));
      return this;
    }

    public OperandToTipper<N> notIn(final Rule<N, Tip> ¢) {
      assert !¢.check(findNode(¢));
      return this;
    }
  }
}
