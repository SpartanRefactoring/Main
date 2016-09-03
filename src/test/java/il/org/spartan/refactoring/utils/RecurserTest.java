package il.org.spartan.refactoring.utils;

import static org.mockito.Mockito.*;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.refactoring.engine.*;

/** @author Dor Ma'ayan
 * @since 2016 */
public class RecurserTest {
  private static boolean barrier() {
    return true;
  }

  private static ASTNode makeCaseNode() {
    return mock(SwitchCase.class);
  }

  final ASTNode ourCase = makeCaseNode();

  @Test(expected = NullPointerException.class) public void explainAPI_briefly() {
    final Integer i = recurse(null, 0).preVisit((r) -> (1 + r.getCurrent().hashCode())//
    );
    assert barrier() : "Hold the stpartanization horses from inlining";
    assert i != 0 : "wow, we really got unlucky; run again";
  }

  @Test public void explainAPI_differently() {
    final Integer i = recurse(makeCaseNode(), Integer.valueOf(0))//
        .preVisit(//
            (r) -> (2 + r.hashCode())//
    );
    assert barrier() : "Hold the stpartanization horses from inlining";
    assert i != 0 : "wow, we really got unlucky; run again";
  }

  @Test public void explainAPI_sensibly() {
    @SuppressWarnings("boxing") final Integer i = recurse(ourCase, 0)//
        .preVisit(//
            (x) -> (2 + x.hashCode())//
    );
    assert i != 0 : "wow, we really got unlucky; run again";
  }

  @SuppressWarnings("boxing") @Test public void explainAPI_shortly() {
    final Integer i = recurse(ourCase, 0).preVisit(//
        (r) -> (2 + r.hashCode())//
    );
    assert barrier() : "Hold the stpartanization horses from inlining";
    assert i != 0 : "wow, we really got unlucky; run again";
  }

  @Test public void explainAPI_Slowly() {
    final ASTNode n = makeCaseNode();
    assert barrier() : "Hold the stpartanization horses from inlining";
    final Recurser<Integer> r = recurse(n, Integer.valueOf(0));
    assert barrier() : "Hold the stpartanization horses from inlining";
    final Function<Recurser<Integer>, Integer> random = (x) -> (2 + x.hashCode());
    assert barrier() : "Hold the stpartanization horses from inlining";
    final Integer i = r.preVisit(random);
    assert barrier() : "Hold the stpartanization horses from inlining";
    assert i != 0 : "wow, we really got unlucky; run again";
  }

  /** This is where you place the {@link Test} test methods that work. They
   * should be never {@link Ignored} when pushed.
   * @author Yossi Gil */
  // TODO: Dor, import here the standard header of test classes
  public <T> Recurser<T> recurse(final ASTNode root) {
    return new Recurser<>(root);
  }

  public <T> Recurser<T> recurse(final ASTNode n, final T t) {
    return new Recurser<>(n, t);
  }
}
