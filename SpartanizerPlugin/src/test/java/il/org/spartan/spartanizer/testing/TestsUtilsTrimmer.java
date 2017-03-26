package il.org.spartan.spartanizer.testing;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

public enum TestsUtilsTrimmer {
  ;
  public static int countOpportunities(@NotNull final AbstractGUIApplicator a, @NotNull final CompilationUnit u) {
    return a.collectSuggestions(u).size();
  }

  @NotNull public static <N extends ASTNode> OperandToTipper<N> included(final String from, final Class<N> clazz) {
    return new OperandToTipper<>(from, clazz);
  }

  @NotNull public static TrimmingOperand trimmingOf(final String from) {
    return new TrimmingOperand(from);
  }

  public static class OperandToTipper<N extends ASTNode> extends TrimmingOperand {
    final Class<N> clazz;

    OperandToTipper(final String from, final Class<N> clazz) {
      super(from);
      this.clazz = clazz;
    }

    private N findNode(@NotNull final Rule<N, Tip> t) {
      assert t != null;
      @NotNull final WrapIntoComilationUnit wrapIntoComilationUnit = WrapIntoComilationUnit.find(get());
      assert wrapIntoComilationUnit != null;
      @NotNull final CompilationUnit u = wrapIntoComilationUnit.intoCompilationUnit(get());
      assert u != null;
      final N $ = firstInstance(u);
      assert $ != null;
      return $;
    }

    private N firstInstance(@NotNull final CompilationUnit u) {
      @NotNull final Wrapper<N> $ = new Wrapper<>();
      u.accept(new ASTVisitor(true) {
        /** The implementation of the visitation procedure in the JDT seems to
         * be buggy. Each time we find a node which is an instance of the sought
         * class, we return false. Hence, we do not anticipate any further calls
         * to this function after the first such node is found. However, this
         * does not seem to be the case. So, in the case our wrapper is not
         * null, we do not carry out any further tests.
         * @param pattern the node currently being visited.
         * @return whether the sought node is found. */
        @Override @SuppressWarnings("unchecked") public boolean preVisit2(@NotNull final ASTNode ¢) {
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

    @NotNull public OperandToTipper<N> in(@NotNull final Rule<N, Tip> ¢) {
      assert ¢.check(findNode(¢));
      return this;
    }

    @NotNull public OperandToTipper<N> notIn(@NotNull final Rule<N, Tip> ¢) {
      assert !¢.check(findNode(¢));
      return this;
    }
  }
}
