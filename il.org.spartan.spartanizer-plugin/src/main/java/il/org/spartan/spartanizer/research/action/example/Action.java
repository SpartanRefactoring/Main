package il.org.spartan.spartanizer.research.action.example;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public class Action extends I<E.Set, Action> {
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
    a.withListener(new E.Idle() {
      @Override public void begin() {
        System.out.println("begin1");
      }
    }).withListener(new E.Idle() {
      @Override public void begin() {
        System.out.println("begin2");
      }
      @Override public void action1() {
        System.out.println("action1");
      }
    }).withListener(new E.Idle() {
      @Override public void begin() {
        //
      }
    }).withListener(new E.Idle() {
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
