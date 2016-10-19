package il.org.spartan.plugin;

import static il.org.spartan.plugin.Listener.*;

import java.util.*;
import java.util.concurrent.atomic.*;

import il.org.spartan.utils.*;

/** An abstract listener taking events that may have any number of parameters of
 * any kind; default implementation is empty, override to specialize, or use
 * {@link Listener.S}
 * @author Ori Roth
 * @author Yossi Gil
 * @see #tick(Object...)
 * @see #push(Object...)
 * @see #pop(Object...)
 * @since 2.6 */
public interface Listener {
  final AtomicLong eventId = new AtomicLong();

  default Listener asListener() {
    return this;
  }

  /** Create a new id for an event
   * @param ¢ notification details
   * @return */
  static long newId() {
    return eventId.incrementAndGet();
  }

  /** Main listener function.
   * @param ¢ notification details */
  void tick(final Object... os);

  /** Begin a delimited listening session
   * @param ¢ notification details
   * @see #pop */
  default void push(final Object... ¢) {
    tick(¢);
  }

  /** Used to restore a pushed listening session
   * @param ¢ notification details */
  default void pop(final Object... ¢) {
    tick(¢);
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
      $.append(newId() + ": ");
      final Separator s = new Separator(", ");
      for (final Object ¢ : os)
        $.append(s + trim(¢));
      $.append('\n');
    }

    private static String trim(final Object ¢) {
      return (¢ + "").substring(1, 35);
    }
  }

  /** An aggregating kind of {@link Listener} that dispatches the event it
   * receives to the multiple {@link Listener}s it stores internally.
   * @author Yossi Gil
   * @since 2.6 */
  class S extends ArrayList<Listener> implements Listener {
    private static final long serialVersionUID = 1L;

    @Override public void tick(final Object... os) {
      asListener().tick(os);
      for (final Listener ¢ : this)
        ¢.tick(os);
    }

    /** for fluent API use, i.e., <code>
     *
     * <pre>
         <b>public final</b>  {@link Listener}  listeners =  {@link Listener.S} . {@link #empty()}
     * </pre>
     *
     * <code>
     * @return an empty new instance */
    public static S empty() {
      return new S();
    }

    /** To be used in the following nano <code><pre> 
      public interface Applicator { 
       /** Setting for the configurable object *\/
       public class Settings extends Listeners {
          /* default access *\/  int howMany;
          /* required here! *\/  boolean robustMode;
          /* the reason is: *\/  Some other; Configuration variables;
          /* class 'Action' *\/  Add as;  Many az you need; 
          /* (extending our *\/  Or require;  
          /* current class) *\/  Some fields;  
          /* may then write *\/  May be;  
          /* or read any of *\/  final If necessary; 
          /* the Settings's *\/  Other can be; 
          /* fields without *\/  static when you need it;
          /* any setters or *\/  Or even;  
          /* getters.       *\/  static final If desired; 
          // 
          // The following are typically generated automatically 
          // since this is a POJO 
          /* public access *\/  public int getHowMany() { return howMany; }  
          /* makes methods *\/  public void setHowMany(int n) { howMany = n; }
          /* the means for *\/  public int getHowMany() { return howMany; }  
          /* configurable- *\/ means for *\/  public int getHowMany() { return howMany; }  
          /* objects as in *\/ means for *\/  public int getHowMany() { return howMany; }  
          /* class Action k as in   - *\/ means for *\/  public int getHowMany() { return howMany; }  
          /* makes methods *\/  public void setHowMany(int n) { howMany = n; }
       }  
       public class Action extends Setting { 
          private T some_variable_internal_to the computation;
          int action1() {} 
          void action2(Type1 t1, Type2 t2, int i)  { ...}
        } 
      }</pre></code> parameterized solely by the name <code>Applicator</code>
     * and the body of class <code>Action</code>.
     * <p>
     * To use this nano, copy the above, changing the name
     * <code>Applicator</code> to whatever you need Fill in class
     * <p>
     * <code>Action</code> with your code. Class <code>Action</code> can then be
     * implemented with the services you provide. 
     * @return <code><b>this</b></code> */
    public Listener listeners() {
      return this;
    }
  }
}