package il.org.spartan.java.temporal2;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-02 */
@SuppressWarnings({ "rawtypes", "hiding" })
public abstract class Temporal<B extends Temporal.Before, A extends Temporal.After> {
  public abstract class Before<B extends Before<B, A>, //
      A extends After<B, A>> extends Temporal<B, A> {/**/}

  public abstract class After<B extends Before<B, A>, //
      A extends After<B, A>> extends Temporal<B, A> {/**/}

  public B before() {
    return null;
  }
  public A after() {
    return null;
  }
  public abstract void body();
  public void go() {
    B b = before();
    if (b != null)
      b.go();
    body();
    A a = after();
    if (a != null)
      a.go();
  }
}
