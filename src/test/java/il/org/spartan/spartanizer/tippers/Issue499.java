package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/**
 * @author  Dor Ma'ayan
 * @since  17-11-2016 
 */
@Ignore
@SuppressWarnings("static-method")
public class Issue499 {
  @Test public void test0() {
    trimmingOf("public Spartanizer defaultRunAction(final GUI$Applicator a) {" + "assert a != null;"
        + "runAction(/*¢ -> Integer.valueOf(a.apply(¢, selection()))*/);" + "return this;" + "}")
            .gives("public Spartanizer defaultRunAction(final GUI$Applicator ¢) {" + "assert ¢ != null;"
                + "runAction(/*¢ -> Integer.valueOf(a.apply(¢, selection()))*/);" + "return this;" + "}")
            .stays();
  }

  @Test public void test1() {
    trimmingOf("public Spartanizer defaultRunAction(final GUI$Applicator a) {" + "assert a != null;"
        + "runAction(x -> Integer.valueOf(a.apply(x, selection())));" + "return this;" + "}")
            .gives("public Spartanizer defaultRunAction(final GUI$Applicator ¢) {" + "assert ¢ != null;"
                + "runAction(x -> Integer.valueOf(¢.apply(x, selection())));" + "return this;" + "}")
            .stays();
  }
}
