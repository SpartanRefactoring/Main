package il.org.spartan.spartanizer.meta;

import static il.org.spartan.spartanizer.java.namespace.Vocabulary.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.traversal.*;
import il.org.spartan.utils.*;

/** Represents a test case, in conjunction with {@link ReflectiveTester}.
 * <p>
 * This class is not intended to be instantiated. You should use it in only one
 * way: <b>create a anonymous class instance of this class</b>. Further, an
 * {@link AssertionError} is thrown if this anonymous creation is executed.
 * <p>
 * The body of this anonymous class represents steps in the process of
 * simplifying a {@link Block} of {@link Statement}. Take a look at the
 * repertoire of methods here. Override any combination of these. The class test
 * engine knows how to process them in order.
 * <p>
 * Don't be afraid to experiment. The error messages should guide you.
 * @author Yossi Gil
 * @since 2017-01-17 */
public class AlphabeticallySortedSentence extends MetaFixture {
  public static final AlphabeticallySortedSentence instance = new AlphabeticallySortedSentence(null);
  public static final AbstractTypeDeclaration reflection = types(instance.reflectedCompilationUnit()).stream()
      .filter(AbstractTypeDeclaration::isPackageMemberTypeDeclaration).findFirst().get();
  @SuppressWarnings("serial") public static final Vocabulary stencil = new Vocabulary() {
    {
      methods(reflection).stream().filter(λ -> !λ.isConstructor() && !iz.static¢(λ) && !iz.final¢(λ) && !iz.private¢(λ))
          .forEach(λ -> put(mangle(λ), λ));
    }
  };

  public static Vocabulary reify(final AnonymousClassDeclaration cd) {
    final Vocabulary $ = new Vocabulary();
    for (final BodyDeclaration bd : bodyDeclarations(cd)) {
      assert bd instanceof MethodDeclaration : fault.specifically("Unexpected " + extract.name(bd), bd);
      final MethodDeclaration md = (MethodDeclaration) bd;
      final String mangle = mangle(md), model = extract.name(reflection);
      assert stencil.containsKey(mangle) //
      : fault.specifically("Method " + mangle + " does not override a non-private non-static non-final method defined in " + model//
          , md, stencil);
      final String javaDoc = " have JavaDoc /** " + disabling.ByComment.disabler + "*/, just like the overrriden version in " + model;
      if (disabling.specificallyDisabled(md))
        assert disabling.specificallyDisabled(md) //
        : fault.specifically("Method " + mangle + " must " + javaDoc, md, stencil);
      else
        assert !disabling.specificallyDisabled(md) //
        : fault.specifically("Method " + mangle + " must not " + javaDoc, md, stencil);
      $.put(mangle, md);
    }
    return $;
  }
  public static Vocabulary reify(final ClassInstanceCreation ¢) {
    final AnonymousClassDeclaration $ = ¢.getAnonymousClassDeclaration();
    return $ == null || !(hop.name(¢.getType()) + "").equals(AlphabeticallySortedSentence.class.getSimpleName()) ? null : reify($);
  }
  public AlphabeticallySortedSentence() {
    forbidden();
  }
  private AlphabeticallySortedSentence(final Void __) {
    forget.em(__);
  }
  private void forbidden() {
    assert reflection != null : fault.specifically("Class " + getClass().getSimpleName() + " should not be instantiated by client");
  }
  /** [[SuppressWarningsSpartan]] */
  protected void startingWith() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto0() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto1() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto2() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto3() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto4() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto5() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto6() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto7() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto8() {
    forbidden();
  }
  /** [[SuppressWarningsSpartan]] */
  protected void trimmingInto9() {
    forbidden();
  }
  protected void trimmingStopsAt() {
    forbidden();
  }
}
