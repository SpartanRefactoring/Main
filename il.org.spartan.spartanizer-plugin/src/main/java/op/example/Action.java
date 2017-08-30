package op.example;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public abstract class Action<Self extends Action<Self>> extends Go<Events.Set, Self> {
  public static class Implementation extends Action<Implementation> {/**/}

  public int x = 13;

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
}
