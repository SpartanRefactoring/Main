package il.org.spartan.spartanizer.research.action.example;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public class Action extends I<E.Set, Action> {
  public static void main(String[] args) {
    Action a = new Action();
    a.withListener(new E.Idle() {
      @Override public void begin() {
        System.out.println("begin");
      }
    });
    a.withListener(new E.Idle() {
      @Override public void begin() {
        //
      }
    });
    a.withListener(new E.Idle() {
      @Override public void end() {
        System.out.println("end");
      }
    });
    a.go();
  }
}
