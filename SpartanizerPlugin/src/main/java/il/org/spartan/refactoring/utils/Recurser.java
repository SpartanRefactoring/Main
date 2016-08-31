package il.org.spartan.refactoring.utils;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

@SuppressWarnings({ "all", "Dor should remove this" })
/** @author Dor Ma'ayan
 * @since 2016 */
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

  public Recurser<T> from(final T value) {
    this.current = value;
    return this;
  }

  /** T is the type of accumulator that is passed to each function, */
  public T preVisit(final Function<Recurser<T>, T> f) {
    this.current = f.apply(this);
    final List<ASTNode> childrenList = getChildren(this.root);
    final List<Recurser<T>> recurserList = new ArrayList<>();
    for (final ASTNode child : childrenList)
      recurserList.add(new Recurser(child));
    int index = 0;
    for (final Recurser<T> rec : recurserList) {
      if (index == 0)
        rec.from(current).preVisit(f);
      else
        rec.from(recurserList.get(index - 1).getCurrent()).preVisit(f);
      index++;
    }
    if (recurserList.isEmpty())
      return this.current;
    return recurserList.get(index - 1).getCurrent();
  }

  public T postVisit(final Function<Recurser<T>, T> f) {
    final List<ASTNode> childrenList = getChildren(this.root);
    final List<Recurser<T>> recurserList = new ArrayList<>();
    for (final ASTNode child : childrenList)
      recurserList.add(new Recurser(child));
    int index = 0;
    for (final Recurser<T> rec : recurserList) {
      if (index == 0)
        rec.from(current).preVisit(f);
      else
        rec.from(recurserList.get(index - 1).getCurrent()).preVisit(f);
      index++;
    }
    this.current = f.apply(this);
    if (recurserList.isEmpty())
      return this.current;
    return recurserList.get(index - 1).getCurrent();
  }

  /** supply self to each node in the tree. */
  public void preVisit(final Consumer<Recurser<T>> f) {
    f.accept(this);
    final List<ASTNode> childrenList = getChildren(this.root);
    final List<Recurser<T>> recurserList = new ArrayList<>();
    for (final ASTNode child : childrenList)
      recurserList.add(new Recurser(child));
    int index = 0;
    for (final Recurser<T> rec : recurserList) {
      if (index == 0)
        rec.from(current).preVisit(f);
      else
        rec.from(recurserList.get(index - 1).getCurrent()).preVisit(f);
      index++;
    }
  }

  public void postVisit(final Consumer<Recurser<T>> f) {
    final List<ASTNode> childrenList = getChildren(this.root);
    final List<Recurser<T>> recurserList = new ArrayList<>();
    for (final ASTNode child : childrenList)
      recurserList.add(new Recurser(child));
    int index = 0;
    for (final Recurser<T> rec : recurserList) {
      if (index == 0)
        rec.from(current).preVisit(f);
      else
        rec.from(recurserList.get(index - 1).getCurrent()).preVisit(f);
      index++;
    }
    f.accept(this.from(recurserList.get(index - 1).getCurrent()));
  }

  public static List<ASTNode> getChildren(final ASTNode node) {
    final List<ASTNode> childrenList = new ArrayList<>();
    final List list = node.structuralPropertiesForType();
    for (int i = 0; i < list.size(); i++) {
      final Object child = node.getStructuralProperty((StructuralPropertyDescriptor) list.get(i));
      if (child instanceof ASTNode)
        childrenList.add((ASTNode) child);
    }
    return childrenList;
  }
}
// /** This is where you place the challenge {@link Test} test methods. This
// * class should be {@link Ignored} when pushed.
// * @author Yossi Gil */
// @Ignore public static class Challenge {
// @Test public void firstTest() {
// // TODO: Dor put tests that fail here
// }
// }
//
// /** This is where you place the {@link Test} test methods that work. They
// * should be never {@link Ignored} when pushed.
// * @author Yossi Gil */
// // TODO: Dor, import here the standard header of test classes
// @SuppressWarnings("static-method") public static class Working {
// public static <T> Recurser<T> recurse(final ASTNode root) {
// return new Recurser<>(root);
// }
//
// public static <T> Recurser<T> recurse(final ASTNode n, final T t) {
// return new Recurser<>(n, t);
// }
//
// final ASTNode ourCase = makeCaseNode();
//
// @Ignore("Until we can make a real case by Mockito") @Test(expected =
// NullPointerException.class) public void explainAPI_briefly() {
// final Integer i = recurse(null, 0).preVisit((r) -> (1 +
// r.getCurrent().hashCode())//
// );
// assert barrier() : "Hold the stpartanization horses from inlining";
// assert i != 0 : "wow, we really got unlucky; run again";
// }
//
// @Ignore("Until we can make a real case by Mockito") @Test public void
// explainAPI_differently() {
// final Integer i = recurse(makeCaseNode(), Integer.valueOf(0))//
// .preVisit(//
// (r) -> (2 + r.hashCode())//
// );
// assert barrier() : "Hold the stpartanization horses from inlining";
// assert i != 0 : "wow, we really got unlucky; run again";
// }
//
// @Ignore("Until we can make a real case by Mockito") @Test public void
// explainAPI_shortly() {
// final Integer i = recurse(ourCase, 0).preVisit(//
// (r) -> (2 + r.hashCode())//
// );
// assert barrier() : "Hold the stpartanization horses from inlining";
// assert i != 0 : "wow, we really got unlucky; run again";
// }
//
// @Ignore("Until we can make a real case by Mockito") @Test public void
// explainAPI_cryptically() {
// recurse(ourCase, 0).preVisit((r) -> 0);
// }
//
// @Ignore("Until we can make a real case by Mockito") @Test public void
// explainAPI_RSA() {
// recurse(ourCase).preVisit((__) -> 0);
// }
//
// @Ignore("Until we can make a real case by Mockito") @Test public void
// explainAPI_laconically() {
// recurse(ourCase).preVisit((__) -> {
// return (Void) null;
// });
// }
//
// @Ignore("Until we can make a real case by Mockito") public void
// explainAPI_Slowly() {
// final ASTNode n = makeCaseNode();
// assert barrier() : "Hold the stpartanization horses from inlining";
// final Recurser<Integer> r = recurse(n, Integer.valueOf(0));
// assert barrier() : "Hold the stpartanization horses from inlining";
// final Function<Recurser<Integer>, Integer> random = (x) -> (2 +
// x.hashCode());
// assert barrier() : "Hold the stpartanization horses from inlining";
// final Integer i = r.preVisit(random);
// assert barrier() : "Hold the stpartanization horses from inlining";
// assert i != 0 : "wow, we really got unlucky; run again";
// }
//
// private boolean barrier() {
// return true;
// }
//
// private ASTNode makeCaseNode() {
// // Todo: DOR use Mockito
// return null;
// }
// }
//
// public void go(final Object f) {
// // TODO Auto-generated method stub
// }
// }
