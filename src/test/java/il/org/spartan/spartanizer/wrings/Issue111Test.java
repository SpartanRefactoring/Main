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
    trimming("public final class A {" + //
        "static public int a;" + //
        "}") //
            .to("public final class A {" + //
                "public static int a;" + //
                "}");
  }

  @Test public void issue111b_1() {
    trimming("public final class A {" + //
        "static final public int a;" + //
        "}") //
            .to("public final class A {" + //
                "public static final int a;" + //
                "}");
  }

  @Test public void issue111c() {
    trimming("protected public void func();").to("public protected void func();");
  }

  @Test public void issue111c_2() { // not working cause method sorting is not
                                    // integrated yet
    trimming("public final class A{" + //
        "synchronized public void fun(final int a) {}" + //
        "final private String s = \"Alex\";" + //
        "}").to("public final class A{" + //
            "public synchronized void fun(final int a) {}" + //
            "private final String s = \"Alex\";" + //
            "}").stays(); //
  }

  public void issue111d() {
    trimming("protected public final class A{}").to("public protected class A{}");
  }

  @Test public void issue111d_1() {
    trimming("abstract class A {}").stays();
  }

  @Test public void issue111e() {
    trimming("protected public final class A{volatile static int a;}") //
        .to("public protected final class A{volatile static int a;}") //
        .to("public protected final class A{static volatile int a;}") //
        .stays();
  }

  @Test public void issue111g() {
    trimming("protected public final public enum Level { " + //
        "HIGH, MEDIUM, LOW" + //
        "}").to("protected public public enum Level { " + //
            "HIGH, MEDIUM, LOW" + //
            "}").to("public public protected enum Level { \n" + //
                "HIGH, MEDIUM, LOW\n" + //
                "}");
  }

  public void issue111h() {
    trimming("protected public int a;").to("public protected int a;");
  }

  public void issue111i() {
    trimming("protected public int a;").to("public protected int a;");
  }

  public void issue111q() {
    trimming("protected public int a;").to("public protected int a;");
  }

  public void issue111w() {
    trimming("protected public int a;").to("public protected int a;");
  }

  public void issue111y() {
    trimming("synchronized volatile public int a;").to("public volatile synchronized int a;");
  }

  public void issue111z() {
    trimming("volatile private int a;").to("private volatile int a;");
  }
}
