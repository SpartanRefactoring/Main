package il.org.spartan.spartanizer.meta;

import static il.org.spartan.spartanizer.java.namespace.Vocabulary.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** Represents a test case, in conjunction with {@link MetaFixture}.
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
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-17 */
public class MetaTestCase extends MetaFixture {
  public static AbstractTypeDeclaration reflection = step.types(new MetaTestCase(null).reflectedCompilationUnit()).stream()
      .filter(d -> d.isPackageMemberTypeDeclaration()).findFirst().get();
  @SuppressWarnings("serial") public static final Vocabulary stensil = new Vocabulary() {
    {
      for (final MethodDeclaration ¢ : step.methods(reflection))
        if (!¢.isConstructor() && !iz.static¢(¢) && !iz.final¢(¢) && !iz.private¢(¢))
          put(mangle(¢), ¢);
    }
  };

  private MetaTestCase(final Void __) {
    ___.unused(__);
  }

  public MetaTestCase() {
    forbidden();
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

  public static Vocabulary reify(final AnonymousClassDeclaration cd) {
    final Vocabulary $ = new Vocabulary();
    for (final BodyDeclaration bd : bodyDeclarations(cd)) {
      assert bd instanceof MethodDeclaration : fault.specifically("Unexpected " + extract.name(bd), bd);
      final MethodDeclaration md = (MethodDeclaration) bd;
      final String mangle = mangle(md);
      String model = extract.name(reflection);
      assert stensil.containsKey(mangle) //
      : fault.specifically("Method " + mangle + " does not override a non-private non-static non-final method defined in " + model//
          , md, stensil);
      final String javaDoc = " have JavaDoc /** " + disabling.disabler + "*/, just like the overrriden version in " + model;
      if (disabling.specificallyDisabled(stensil.get(mangle)))
        assert disabling.specificallyDisabled(md) //
        : fault.specifically("Method " + mangle + " must " + javaDoc, md, stensil);
      else
        assert !disabling.specificallyDisabled(md) //
        : fault.specifically("Method " + mangle + " must not " + javaDoc, md, stensil);
      $.put(mangle + "", md);
    }
    return $;
  }

  public static Vocabulary reify(final ClassInstanceCreation ¢) {
    final AnonymousClassDeclaration $ = ¢.getAnonymousClassDeclaration();
    return $ == null || !(hop.name(¢.getType()) + "").equals(MetaTestCase.class.getSimpleName()) ? null : MetaTestCase.reify($);
  }
}
