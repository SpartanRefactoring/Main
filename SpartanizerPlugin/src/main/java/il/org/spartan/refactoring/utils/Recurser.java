package il.org.spartan.refactoring.utils;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Dor */
public class Recurser<T> {
  private final ASTNode root;
  private T current;

  private Recurser(ASTNode root, T current) {
    this.root = root;
    this.current = current;
  }

  private Recurser(ASTNode root) {
    this(root, null);
  }

  public T getCurrent() {
    return current;
  }

  public ASTNode getRoot() {
    return root;
  }

  public T go(Function<Recurser<T>, T> f) {
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
    public static <T> Recurser<T> recurse(ASTNode root) {
      return new Recurser<T>(root);
    }

    public static <T> Recurser<T> recurse(ASTNode n, T t) {
      return new Recurser<T>(n, t);
    }

    final ASTNode ourCase = makeCaseNode();

    @Test(expected=NullPointerException.class) public void explainAPI_briefly() {
      Integer i = recurse(null, 0).go((r) -> (1 + r.getCurrent().hashCode())//
      );
      assert barrier() : "Hold the stpartanization horses from inlining";
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    @Test public void explainAPI_differently() {
      Integer i = recurse(makeCaseNode(), Integer.valueOf(0))//
          .go(//
              (r) -> (2 + r.hashCode())//
      );
      assert barrier() : "Hold the stpartanization horses from inlining";
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    @Test public void explainAPI_sensibly() {
      Integer i = recurse(ourCase, 0)//
          .go(//
              (x) -> (2 + x.hashCode())//
      );
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    @Test public void explainAPI_shortly() {
      Integer i = recurse(ourCase, 0).go(//
          (r) -> (2 + r.hashCode())//
      );
      assert barrier() : "Hold the stpartanization horses from inlining";
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    @Test public void explainAPI_Slowly() {
      ASTNode n = makeCaseNode();
      assert barrier() : "Hold the stpartanization horses from inlining";
      Recurser<Integer> r = recurse(n, Integer.valueOf(0));
      assert barrier() : "Hold the stpartanization horses from inlining";
      Function<Recurser<Integer>, Integer> random = (x) -> (2 + x.hashCode());
      assert barrier() : "Hold the stpartanization horses from inlining";
      Integer i = r.go(random);
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
}
