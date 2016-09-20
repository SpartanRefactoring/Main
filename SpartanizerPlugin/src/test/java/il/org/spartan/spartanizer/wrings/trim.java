package il.org.spartan.spartanizer.wrings;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ltk.core.refactoring.*;
import org.junit.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** Fluent API for testing:
 *
 * <pre>
 * trimming.of("a+(b-c)").gives("a+b-c")
 * </pre>
 *
 * or
 *
 * <pre>
 * trimming.with(InfixExpression.class, new InfixTermsExpand()).of("a+(b-c)").gives("a+b+c")
 * </pre>
*/
/** ??
 * @author Yossi Gil
 * @since 2016 */
public interface trim {
  static int countOpportunities(final GUI$Applicator a, final CompilationUnit u) {
    return a.collectSuggesions(u).size();
  }

  static fluentTrimmerApplication of(final String codeFragment) {
    return new fluentTrimmerApplication(new Trimmer(), codeFragment);
  }

  @SafeVarargs //
  static <N extends ASTNode> fluentTrimmer with(final Class<N> clazz, final Wring<N>... ns) {
    return new fluentTrimmer(clazz, ns);
  }

  /** Starting point of fluent API for @Testing:
   * <code>trimming.repeatedly.of("a+(b-c)").gives("a+b-c")</code>, or <br/>
   * <code>trimming // See {@link trim} <br/>
   * .repeatedly //  See {@link trim.repeatedely} <br/>
   * .withWring(new InfixTermsExpand() // See {@link #withWring(Wring)} <br/>
   * .of("a+(b-c)") //  See {@link #of(String)} <br/>
   * .gives("a+b-c")</code> */
  interface repeatedly {
    static fluentTrimmerApplication of(final String codeFragment) {
      return new fluentTrimmerApplication(new Trimmer(), codeFragment) {
        @Override public fluentTrimmerApplication gives(final String expected) {
          return super.gives(BatchApplicator.fixedPoint(expected));
        }

        @Override public void stays() {
          super.stays();
        }
      };
    }

    @SafeVarargs static <N extends ASTNode> fluentTrimmer with(final Class<N> clazz, final Wring<N>... ns) {
      return new fluentTrimmer(clazz, ns) {
        @Override public RefactoringStatus checkAllConditions(final IProgressMonitor pm) throws CoreException, OperationCanceledException {
          // TODO Auto-generated method stub
          return super.checkAllConditions(pm);
        }

        /* @Override public <T> T getAdapter(final Class<T> adapter) { // TODO
         * Auto-generated method stub return (T) super.getAdapter(adapter); } */
        @Override public fluentTrimmerApplication of(final String codeFragment) {
          // TODO Auto-generated method stub
          return super.of(codeFragment);
        }

        @Override protected RefactoringTickProvider doGetRefactoringTickProvider() {
          // TODO Auto-generated method stub
          return super.doGetRefactoringTickProvider();
        }
      };
    }
  }

  /** Unit tests demonstrating the fluent API
   * @author Yossi Gil
   * @since 2016 */
  @SuppressWarnings("static-method") @Ignore static class TEST {
    @Test public void trimming_of_gives() {
      trim.of("a +=1;").gives("a++;");
    }

    @Test public void trimming_of_gives_gives_gives_stays() {
      trim.of("int b = 3; int a = b; return  a;").gives("int b = 3; int a = b; return  a;").gives("int a = 3; return  a;").gives("return 3;").stays();
    }

    @Test public void trimming_of_gives_stays() {
      trim.of("a +=1;").gives("a++;").stays();
    }

    @Test public void trimming_of_stays() {
      trim.of("a").stays();
    }

    @Test public void trimming_repeatedly_of_gives() {
      trim.repeatedly.of("int b = 3; int a = b; return  a;").gives("return 3;");
    }
  }
}
