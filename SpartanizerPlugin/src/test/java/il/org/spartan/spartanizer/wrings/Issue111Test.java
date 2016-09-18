package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Alex
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue111Test {
  @Test public void issue111a_1() {
    trimmingOf("public final class A {" + //
        "static public int a;" + //
        "}") //
            .gives("public final class A {" + //
                "public static int a;" + //
                "}");
  }

  @Test public void issue111b_1() {
    trimmingOf("public final class A {" + //
        "static final public int a;" + //
        "}") //
            .gives("public final class A {" + //
                "public static final int a;" + //
                "}");
  }

  @Test public void issue111c() {
    trimmingOf("protected public void func();").gives("public protected void func();");
  }

  @Test public void issue111c_2() { // not working cause method sorting is not
                                    // integrated yet
    trimmingOf("public final class A{" + //
        "synchronized public void fun(final int a) {}" + //
        "final private String s = \"Alex\";" + //
        "}").gives("public final class A{" + //
            "public synchronized void fun(final int a) {}" + //
            "private final String s = \"Alex\";" + //
            "}").stays(); //
  }

  public void issue111d() {
    trimmingOf("protected public final class A{}").gives("public protected class A{}");
  }

  @Test public void issue111d_1() {
    trimmingOf("abstract class A {}").stays();
  }

  @Test public void issue111e() {
    trimmingOf("protected public final class A{volatile static int a;}") //
        .gives("public protected final class A{volatile static int a;}") //
        .gives("public protected final class A{static volatile int a;}") //
        .stays();
  }

  @Test public void issue111g() {
    trimmingOf("protected public final public enum Level { " + //
        "HIGH, MEDIUM, LOW" + //
        "}").gives("protected public public enum Level { " + //
            "HIGH, MEDIUM, LOW" + //
            "}").gives("public public protected enum Level { \n" + //
                "HIGH, MEDIUM, LOW\n" + //
                "}");
  }

  public void issue111h() {
    trimmingOf("protected public int a;").gives("public protected int a;");
  }

  public void issue111i() {
    trimmingOf("protected public int a;").gives("public protected int a;");
  }

  public void issue111q() {
    trimmingOf("protected public int a;").gives("public protected int a;");
  }

  public void issue111w() {
    trimmingOf("protected public int a;").gives("public protected int a;");
  }

  public void issue111y() {
    trimmingOf("synchronized volatile public int a;").gives("public volatile synchronized int a;");
  }

  public void issue111z() {
    trimmingOf("volatile private int a;").gives("private volatile int a;");
  }
}
