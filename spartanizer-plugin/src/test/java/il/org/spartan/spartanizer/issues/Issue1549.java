package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.IfCommandsSequencerNoElseSingletonSequencer;

/** Test case for bug in {@link IfCommandsSequencerNoElseSingletonSequencer}
 * @author Yuval Simon
 * @since 2017-06-27 */
@SuppressWarnings("static-method")
public class Issue1549 {
  @Test public void t1() {
    trimmingOf("static boolean equalsImpl(Set<?> os, @Nullable Object object) {" + //
        "if (os == object) {" + //
        "return true;" + //
        "}" + //
        "if (object instanceof Set) {" + //
        "Set<?> o = (Set<?>) object;" + //
        "try {" + //
        "return os.size() == o.size() && os.containsAll(o);" + //
        "} catch (NullPointerException ignored) {" + //
        "return false;" + //
        "}" + //
        "}" + //
        "return false;" + //
        "}").using(new IfCommandsSequencerNoElseSingletonSequencer()).gives("static boolean equalsImpl(Set<?> os, @Nullable Object object) {" + //
            "if (os == object) {" + //
            "return true;" + //
            "}" + //
            "if (!(object instanceof Set))" + //
            "return false;" + //
            "Set<?> o = (Set<?>) object;" + //
            "try {" + //
            "return os.size() == o.size() && os.containsAll(o);" + //
            "} catch (NullPointerException ignored) {" + //
            "return false;" + //
            "}" + //
            "}");
  }
}
