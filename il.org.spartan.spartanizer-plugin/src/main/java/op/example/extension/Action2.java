package op.example.extension;

import op.example.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public class Action2 extends Action {
  public class X extends Action.X {
    public int getY() {
      return Action2.this.getY();
    }
  }

  @SuppressWarnings("static-method") public int getY() {
    return 3;
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
    Action2 a = new Action2();
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
        System.out.println("y = " + getY());
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
