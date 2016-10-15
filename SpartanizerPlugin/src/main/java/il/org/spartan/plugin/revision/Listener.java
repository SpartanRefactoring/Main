package il.org.spartan.plugin.revision;

import static il.org.spartan.plugin.revision.Listener.*;

import java.util.*;
import java.util.concurrent.atomic.*;

import il.org.spartan.utils.*;

/** An abstract listener taking events that may have any number of parameters.
 * parameters; default implementation is empty, extend to specialize, or use
 * {@link Listener.S}
 * @author Ori Roth
 * @author Yossi Gil
 * @since 2.6 */
public interface Listener {
  final AtomicInteger id = new AtomicInteger();

  static int id() {
    return id.get();
  }

  /** Main listener function.
   * @param ¢ object to be listened to */
  default void tick(final Object... ¢) {
    id.incrementAndGet();
    ___.unused(¢);
  }

  /** A listener that records a long string of the message it got.
   * @author Yossi Gil
   * @since 2016 */
  class Tracing implements Listener {
    private final StringBuilder $ = new StringBuilder();

    public String $() {
      return $ + "";
    }

    @Override public void tick(final Object... os) {
      $.append(id() + ": ");
      for (final Object ¢ : os)
        pack(¢);
      $.append('\n');
    }

    private void pack(final Object ¢) {
      $.append("," + trim(¢));
    }

    private static String trim(final Object ¢) {
      return (¢ + "").substring(1, 35);
    }
  }

  /** An aggregating kind of {@link Listener} that dispatches the event it
   * receives to the multiple {@link Listener} s it stores internally.
   * @author Yossi Gil
   * @since 2.6 */
  class S extends ArrayList<Listener> implements Listener {
    private static final long serialVersionUID = 1L;

    @Override public void tick(final Object... os) {
      for (final Listener ¢ : this)
        ¢.tick(os);
    }

    /** for fluent API use, i.e., <code>
     *
     * <pre>
     *  <b>public final</b>  {@link Listener}  listeners =  {@link Listener.S} . {@link #empty()}
     * </pre>
     *
     * <code>
     * @return an empty new instance */
    public static S empty() {
      return new S();
    }

    /** To be used in the following nano <code><pre> 
             * public interface Applicator { 
             *   public class Settings extends Listeners {
             *      public class Action extends Setting { 
             *         int action1() {} ; 
             *         void action2(Type1 t1, Type2 t2, int i)  { ...}
             *      } 
             *   }  
             * } </pre></code> parameterized solely by the name
     * <code>Applicator</code> and the actions in class `Action` in
     * <code>Action</code>
     * @return <code><b>this</b></code> */
    public List<Listener> listeners() {
      return this;
    }
  }
}