package op.example;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public class Action extends Implementation<Events.Set, Action> {
  public class X implements Events.Listener.Idle {
    public int getX() {
      return Action.this.getX();
    }
  }

  @SuppressWarnings("static-method") public int getX() {
    return 13;
  }
  @Override public void go() {
    listeners.begin();
    System.out.println("ACTION1");
    listeners.action1();
    System.out.println("ACTION2");
    listeners.action2();
    listeners.end();
  }
  public static void main(String[] args) {
    Action a = new Action();
    a.withListener(a.new X() {
      @Override public void begin() {
        System.out.println("begin1");
      }
    }).withListener(a.new X() {
      @Override public void begin() {
        System.out.println("begin2");
      }
      @Override public void action1() {
        System.out.println("action1");
      }
    }).withListener(a.new X() {
      @Override public void begin() {
        System.out.println("x = " + getX());
      }
    }).withListener(a.new X() {
      @Override public void action2() {
        System.out.println("action2");
      }
      @Override public void end() {
        System.out.println("end");
      }
    });
    a.go();
  }
}
