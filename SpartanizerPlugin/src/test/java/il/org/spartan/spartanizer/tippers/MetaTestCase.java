package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

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
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-17 */
public abstract class MetaTestCase {
  public MetaTestCase(final String name) {
    this.name = name;
    forbidden();
  }

  public final String name;

  private void forbidden() {
    assert fault.unreachable() : fault.specifically("Class " + getClass().getSimpleName() + " should forbidden be instantiated", this);
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

  static Reify reify(final AnonymousClassDeclaration cd) {
    final Reify $ = new Reify();
    for (Object bd : cd.bodyDeclarations()) {
      assert bd instanceof MethodDeclaration : fault.specifically("Unexpected ", bd);
      MethodDeclaration md = (MethodDeclaration) bd;
      if (!expected(md))
        assert fault.unreachable() : fault.specifically("Unexpected method declaration", md);
      $.shapes.put(md.getName() + "", md);
    }
    return $;
  }

  private static boolean expected(MethodDeclaration x) {
    return x.hashCode() >0;
  }

  public static MetaTestCase.Reify reify(final ClassInstanceCreation ¢) {
    final AnonymousClassDeclaration $ = ¢.getAnonymousClassDeclaration();
    return $ == null || !(hop.name(¢.getType()) + "").equals(MetaTestCase.class.getSimpleName()) ? null : reify($);
  }

  public static class Reify {
    public final Map<String, MethodDeclaration> shapes = new TreeMap<>();
  }
}
