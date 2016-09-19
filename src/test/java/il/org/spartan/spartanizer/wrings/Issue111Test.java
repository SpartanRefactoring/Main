package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Alex
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue111Test {
  @Test public void A$a_1() {
    trimmingOf("public final class A {" + //
        "static public int a;" + //
        "}") //
            .gives("public final class A {" + //
                "public static int a;" + //
                "}");
  }

  @Test public void A$b_1() {
    trimmingOf("public final class A {" + //
        "static final public int a;" + //
        "}") //
            .gives("public final class A {" + //
                "public static final int a;" + //
                "}");
  }

  @Test public void A$c() {
    trimmingOf("protected public void func();").gives("public protected void func();");
  }

  @Test public void A$c_2() { // not working cause method sorting is not
                                    // integrated yet
    trimmingOf("public final class A{" + //
        "synchronized public void fun(final int a) {}" + //
        "final private String s = \"Alex\";" + //
        "}").gives("public final class A{" + //
            "public synchronized void fun(final int a) {}" + //
            "private final String s = \"Alex\";" + //
            "}").stays(); //
  }

  public void A$d() {
    trimmingOf("protected public final class A{}").gives("public protected class A{}");
  }

  @Test public void A$d_1() {
    trimmingOf("abstract class A {}").stays();
  }

  @Test public void A$e() {
    trimmingOf("protected public final class A{volatile static int a;}") //
        .gives("public protected final class A{volatile static int a;}") //
        .gives("public protected final class A{static volatile int a;}") //
        .stays();
  }

  @Test public void A$g() {
    trimmingOf("protected public final public enum Level { " + //
        "HIGH, MEDIUM, LOW" + //
        "}").gives("protected public public enum Level { " + //
            "HIGH, MEDIUM, LOW" + //
            "}").gives("public public protected enum Level { \n" + //
                "HIGH, MEDIUM, LOW\n" + //
                "}");
  }

  public void A$h() {
    trimmingOf("protected public int a;").gives("public protected int a;");
  }

  public void A$i() {
    trimmingOf("protected public int a;").gives("public protected int a;");
  }

  public void A$q() {
    trimmingOf("protected public int a;").gives("public protected int a;");
  }

  public void A$w() {
    trimmingOf("protected public int a;").gives("public protected int a;");
  }

  public void A$y() {
    trimmingOf("synchronized volatile public int a;").gives("public volatile synchronized int a;");
  }

  public void A$z() {
    trimmingOf("volatile private int a;").gives("private volatile int a;");
  }
}
