package il.org.spartan.refactoring.utils;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import static org.mockito.Mockito.*;

public class RecurserTest {
  /** This is where you place the {@link Test} test methods that work. They
   * should be never {@link Ignored} when pushed.
   * @author Yossi Gil */
  // TODO: Dor, import here the standard header of test classes
    public <T> Recurser<T> recurse(ASTNode root) {
      return new Recurser<T>(root);
    }

    public <T> Recurser<T> recurse(ASTNode n, T t) {
      return new Recurser<T>(n, t);
    }

    final ASTNode ourCase = makeCaseNode();

    @Test(expected=NullPointerException.class) public void explainAPI_briefly() {
      Integer i = recurse(null, 0).preVisit((r) -> (1 + r.getCurrent().hashCode())//
      );
      assert barrier() : "Hold the stpartanization horses from inlining";
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    @Test public void explainAPI_differently() {
      Integer i = recurse(makeCaseNode(), Integer.valueOf(0))//
          .preVisit(//
              (r) -> (2 + r.hashCode())//
      );
      assert barrier() : "Hold the stpartanization horses from inlining";
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    @Test public void explainAPI_sensibly() {
      Integer i = recurse(ourCase, 0)//
          .preVisit(//
              (x) -> (2 + x.hashCode())//
      );
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    @Test public void explainAPI_shortly() {
      Integer i = recurse(ourCase, 0).preVisit(//
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
      Integer i = r.preVisit(random);
      assert barrier() : "Hold the stpartanization horses from inlining";
      assert i != 0 : "wow, we really got unlucky; run again";
    }

    private boolean barrier() {
      return true;
    }

    private ASTNode makeCaseNode() {
        final SwitchCase caseNode = mock(SwitchCase.class);
        return caseNode;
    }
  }

