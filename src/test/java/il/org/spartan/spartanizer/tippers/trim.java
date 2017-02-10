package il.org.spartan.spartanizer.tippers;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ltk.core.refactoring.*;

import org.junit.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Fluent API for testing: {@code
 * trimming.of("a+(b-c)")//
 *     .gives("a+b-c")
 * } or {@code
 * trimming.with(InfixExpression.class, new InfixTermsExpand()).of("a+(b-c)")//
 *     .gives("a+b+c")
 * } */
/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016 */
public interface trim {
  static int countOpportunities(final AbstractGUIApplicator a, final CompilationUnit u) {
    return a.collectSuggestions(u).size();
  }

  static fluentTrimmerApplication of(final String codeFragment) {
    return new fluentTrimmerApplication(new Trimmer(), codeFragment);
  }

  @SafeVarargs //
  static <N extends ASTNode> fluentTrimmer with(final Class<N> clazz, final Tipper<N>... ts) {
    return new fluentTrimmer(clazz, ts);
  }

  /** Starting point of fluent API for @Testing:
   * {@code trimming.repeatedly.of("a+(b-c)")//
  .gives("a+b-c")}, or <code>trimming // See {@link trim} 
    * .repeatedly //  See {@link trim.repeatedely} 
    * .withTipper(new InfixTermsExpand() // See {@link #withTipper(Tipper)} 
    * .of("a+(b-c)") //  See {@link #of(String)} 
    * .gives("a+b-c")</code> */
  interface repeatedly {
    static fluentTrimmerApplication of(final String codeFragment) {
      return new fluentTrimmerApplication(new Trimmer(), codeFragment) {
        @Override public fluentTrimmerApplication gives(final String expected) {
          return super.gives(new InteractiveSpartanizer().fixedPoint(expected));
        }

        @Override public void stays() {
          super.stays();
        }
      };
    }

    @SafeVarargs static <N extends ASTNode> fluentTrimmer with(final Class<N> clazz, final Tipper<N>... ts) {
      return new fluentTrimmer(clazz, ts) {
        @Override public RefactoringStatus checkAllConditions(final IProgressMonitor pm) throws CoreException, OperationCanceledException {
          return super.checkAllConditions(pm);
        }

        @Override protected RefactoringTickProvider doGetRefactoringTickProvider() {
          return super.doGetRefactoringTickProvider();
        }

        @Override public fluentTrimmerApplication of(final String codeFragment) {
          return super.of(codeFragment);
        }
      };
    }
  }

  /** Unit tests demonstrating the fluent API
   * @author Yossi Gil
   * @since 2016 */
  @Ignore
  @SuppressWarnings("static-method")
  class TEST {
    @Test public void trimming_of_gives() {
      trim.of("a +=1;")//
          .gives("a++;");
    }

    @Test public void trimming_of_gives_gives_gives_stays() {
      trim.of("int b = 3; int a = b; return  a;")//
          .gives("int b = 3; int a = b; return  a;")//
          .gives("int a = 3; return  a;")//
          .gives("return 3;")//
          .stays();
    }

    @Test public void trimming_of_gives_stays() {
      trim.of("a +=1;")//
          .gives("a++;")//
          .stays();
    }

    @Test public void trimming_of_stays() {
      trim.of("a")//
          .stays();
    }

    @Test public void trimming_repeatedly_of_gives() {
      trim.repeatedly.of("int b = 3; int a = b; return  a;")//
          .gives("return 3;");
    }
  }
}
