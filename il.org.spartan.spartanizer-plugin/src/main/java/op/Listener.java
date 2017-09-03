package op;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-09-03 */
public interface Listener {
  interface Protocol {
    void go();
  }

  interface Protocol2 extends Protocol {
    default void init(Dispatcher ¢) {
      ¢.hook(Primary.of(Protocol::go).before(), () -> begin());
      ¢.hook(Primary.of(Protocol::go).after(), () -> end());
    }
    void begin();
    void end();
  }
  interface Protocol3 extends Protocol2 {
    default void init(Dispatcher ¢) {
      ¢.hook(Primary.of(Protocol::go).before(), () -> begin());
      ¢.hook(Primary.of(Protocol::go).after(), () -> end());
    }
  }
}

class OP1 {
  Dispatcher d; 
  OP1() {
    d.disptach(Listener.Protocol::go);
  }
}
