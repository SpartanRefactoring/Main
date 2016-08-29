package il.org.spartan.refactoring.utils;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

@SuppressWarnings({ "all", "Dor should remove this" })
/** @author Dor */
public class Recurser<T> {
  private final ASTNode root;
  private T current;

  private Recurser(final ASTNode root, final T current) {
    this.root = root;
    this.current = current;
  }

  private Recurser(final ASTNode root) {
    this(root, null);
  }

  public T getCurrent() {
    return current;
  }

  public ASTNode getRoot() {
    return root;
  }

  /** T is the type of accumulator that is passed to each function, */
  public T go(final Function<Recurser<T>, T> f) {
    return null;
  }

  /** supply self to each node in the tree. */
  public T go(final Consumer<Recurser<T>> f) {
    return null;
  }

  /** This is where you place the challenge {@link Test} test methods. This
   * class should be {@link Ignored} when pushed.
   * @author Yossi Gil */
  @Ignore public static class Challenge {
    @Test public void firstTest() {
      // TODO: Dor put tests that fail here
    }
  }

  /** This is where you place the {@link Test} test methods that work. They
   * should be never {@link Ignored} when pushed.
   * @author Yossi Gil */
  // TODO: Dor, import here the standard header of test classes
  @SuppressWarnings("static-method") public static class Working {
    public static <T> Recurser<T> recurse(final ASTNode root) {
      return new Recurser<>(root);
    }

    public static <T> Recurser<T> recurse(final ASTNode n, final T t) {
      return new Recurser<>(n, t);
    }

    final ASTNode ourCase = makeCaseNode();

    @Ignore("Until we can make a real case by Mockito") @Test(expected = NullPointerException.class) public void explainAPI_briefly() {
      final Integer i = recurse(null, 0).go((r) -> (1 + r.getCurrent().hashCode())//
      );
      assert barrier() : "Hold the stpartanization horses from inlining";
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    @Ignore("Until we can make a real case by Mockito") @Test public void explainAPI_differently() {
      final Integer i = recurse(makeCaseNode(), Integer.valueOf(0))//
          .go(//
              (r) -> (2 + r.hashCode())//
      );
      assert barrier() : "Hold the stpartanization horses from inlining";
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    @Ignore("Until we can make a real case by Mockito") @Test public void explainAPI_shortly() {
      final Integer i = recurse(ourCase, 0).go(//
          (r) -> (2 + r.hashCode())//
      );
      assert barrier() : "Hold the stpartanization horses from inlining";
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    @Ignore("Until we can make a real case by Mockito") @Test public void explainAPI_cryptically() {
      recurse(ourCase, 0).go((r) -> 0);
    }

    @Ignore("Until we can make a real case by Mockito") @Test public void explainAPI_RSA() {
      recurse(ourCase).go((__) -> 0);
    }

    @Ignore("Until we can make a real case by Mockito") @Test public void explainAPI_laconically() {
      recurse(ourCase).go((__) -> {
        return (Void) null;
      });
    }

    @Ignore("Until we can make a real case by Mockito") public void explainAPI_Slowly() {
      final ASTNode n = makeCaseNode();
      assert barrier() : "Hold the stpartanization horses from inlining";
      final Recurser<Integer> r = recurse(n, Integer.valueOf(0));
      assert barrier() : "Hold the stpartanization horses from inlining";
      final Function<Recurser<Integer>, Integer> random = (x) -> (2 + x.hashCode());
      assert barrier() : "Hold the stpartanization horses from inlining";
      final Integer i = r.go(random);
      assert barrier() : "Hold the stpartanization horses from inlining";
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    private boolean barrier() {
      return true;
    }

    private ASTNode makeCaseNode() {
      // Todo: DOR use Mockito
      return null;
    }
  }

  public void go(final Object f) {
    // TODO Auto-generated method stub
  }
}
