package op.example.extension;

import op.example.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public abstract class Action2<Self extends Action2<Self>> extends Action<Self> {
  public static class Implementation extends Action2<Implementation> {/**/}

  public int y = 3;

  @Override public void go() {
    listeners.begin();
    System.out.println("ACTION1");
    listeners.action1();
    System.out.println("ACTION2");
    listeners.action2();
    listeners.end();
  }
  public static void main(String[] args) {
    Implementation a = new Implementation();
    a.withListener(a.new Hook() {
      @Override public void begin() {
        System.out.println("begin1");
      }
    }).withListener(a.new Hook() {
      @Override public void begin() {
        System.out.println("begin2");
      }
      @Override public void action1() {
        System.out.println("action1");
      }
    }).withListener(a.new Hook() {
      @Override public void begin() {
        System.out.println("x = " + enclosure().x);
        System.out.println("y = " + enclosure().y);
      }
    }).withListener(a.new Hook() {
      @Override public void action2() {
        System.out.println("action2");
      }
      @Override public void end() {
        System.out.println("end");
      }
    });
    a.go();
  }
  public static void main2(@SuppressWarnings("unused") String[] args) {
    Implementation a = new Implementation();
    Action.Implementation b = new Action.Implementation();
    Go<Events.Set, Implementation>.Hook al = a.new Hook() {
      @Override public void begin() {
        System.out.println(enclosure().y);
        ;
      }
    };
    Go<Events.Set, Action.Implementation>.Hook bl = b.new Hook() {
      @Override public void begin() {
        System.out.println(enclosure().x);
      }
    };
    a.withListener(al).withListener(bl);
    b.withListener(al).withListener(bl);
    a.go();
    b.go();
  }
}
