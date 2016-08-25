package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Trimmer.*;
import il.org.spartan.refactoring.wring.TrimmerWithToolbox.*;

/** Fluent API for @Testing: <code>trimming.of("a+(b-c)").gives("a+b-c")</code>,
 * or<br/>
 * <code>trimming//<br/>
 * .withWring(new InfixTermsExpand())// <br/>
 * .of("a+(b-c)")// <br/>
 * .gives("a+b+c")</code> */
public interface trimming {
  public static ToolboxApplication of(String codeFragment) {
    return new TrimmerWithToolbox(new Trimmer()).new ToolboxApplication(codeFragment);
  }

  public static <N extends ASTNode> TrimmerWithSpecificWring<N> withWringOfType(Wring<N> w, Class<N> clazz) {
    TrimmerWithToolbox t = new TrimmerWithToolbox(new Trimmer(w));
    return null;
  }

  /** Starting point of fluent API for @Testing:
   * <code>trimming.repeatedly.of("a+(b-c)").gives("a+b-c")</code>, or <br/>
   * <code>trimming // See {@link trimming} <br/>
   * .repeatedly //  See {@link trimming.repeatedely} <br/>
   * .withWring(new InfixTermsExpand() // See {@link #withWring(Wring)} <br/>
   * .of("a+(b-c)") //  See {@link #of(String)} <br/>
   * .gives("a+b-c")</code> */
  interface repeatedely {
    static TrimmerWithToolbox withWring(Wring<?> w) {
      return new TrimmerWithToolbox(new Trimmer(w));
    }

    static TrimmerWithSpecificWring of(String codeFragment) {
      TrimmerWithToolbox a = new TrimmerWithToolbox(new Trimmer());
      TrimmerWithSpecificWring $;
      return $;
    }
  }

  class TrimmerWithSpecificWring<N extends ASTNode> //
      extends TrimmerWithToolbox {
    final Class<N> clazz;

    public TrimmerWithSpecificWring(Wring<N> w) {
      super(new Trimmer(new Toolbox(w)));
    }

    /** creates an ASTRewrite which contains the changes
     * @return an ASTRewrite which contains the changes */
    public final ASTRewrite createRewrite() {
      return createRewrite(new NullProgressMonitor());
    }

    /** creates an ASTRewrite which contains the changes
     * @param pm a progress monitor in which the progress of the refactoring is
     *        displayed
     * @return an ASTRewrite which contains the changes */
    public final ASTRewrite createRewrite(final IProgressMonitor m) {
      return createRewrite(m, (IMarker) null);
    }

    private ASTRewrite createRewrite(final IProgressMonitor pm, final IMarker m) {
      pm.beginTask("Creating rewrite operation...", 1);
      final ASTRewrite $ = ASTRewrite.create(compilationUnit.getAST());
      fillRewrite($, m);
      pm.done();
      return $;
    }

    public TrimmerWithSpecificWring<N> notIn(final Wring<N> w) {
      azzert.that(w.claims(findNode(w)), is(false));
      return this;
    }

    protected final void fillRewrite(final ASTRewrite r, final IMarker m) {
      compilationUnit.accept(trimmer().new DispatchingVisitor() {
        @Override <N extends ASTNode> boolean go(N n) {
          if (!trimmer().inRange(m, n))
            return true;
          final Wring<N> w = trimmer().toolbox.find(n);
          if (w != null) {
            final Rewrite make = w.make(n, exclude);
            if (make != null)
              make.go(r, null);
          }
          return true;
        }
      });
    }

    private N firstInstance(final CompilationUnit u) {
      final Wrapper<N> $ = new Wrapper<>();
      u.accept(new ASTVisitor() {
        /** The implementation of the visitation procedure in the JDT seems to
         * be buggy. Each time we find a node which is an instance of the sought
         * class, we return false. Hence, we do not anticipate any further calls
         * to this function after the first such node is found. However, this
         * does not seem to be the case. So, in the case our wrapper is not
         * null, we do not carry out any further tests.
         * @param n the node currently being visited.
         * @return <code><b>true</b></code> <i>iff</i> the sought node is
         *         found. */
        @SuppressWarnings("unchecked") @Override public boolean preVisit2(final ASTNode n) {
          if ($.get() != null)
            return false;
          if (!clazz.isAssignableFrom(n.getClass()))
            return true;
          $.set((N) n);
          return false;
        }
      });
      return $.get();
    }

    public TrimmerWithSpecificWring<N> in(final Wring<N> w) {
      final N findNode = findNode(w);
      azzert.that(w.claims(findNode), is(true));
      return this;
    }

    private N findNode(final Wring<N> w) {
      azzert.notNull(w);
      final GuessedContext guessContext = GuessedContext.find(codeFragment);
      azzert.notNull(guessContext);
      final CompilationUnit u = compilationUnit;
      azzert.notNull(u);
      final N $ = firstInstance(u);
      azzert.notNull($);
      return $;
    }
  }

  public static int countOpportunities(final Spartanization s, final CompilationUnit u) {
    return s.findOpportunities(u).size();
  }

  static <N extends ASTNode> TrimmerWithSpecificWring<N> included(final String from, final Class<N> clazz) {
  }
}