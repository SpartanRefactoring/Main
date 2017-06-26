package il.org.spartan.spartanizer.issues;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

/** TODO Yuval Simon: document class 
 * 
 * @author Yuval Simon
 * @since 2017-06-27 */
public class Issue1549 {
  @Test public void test() {
    trimmingOf("static boolean equalsImpl(Set<?> os, @Nullable Object object) {"
        + "if (os == object) {"
        + "return true;"
        + "}"
        + "if (object instanceof Set) {"
        + "Set<?> o = (Set<?>) object;"
        + "try {"
        + "return os.size() == o.size() && os.containsAll(o);"
        + "} catch (NullPointerException ignored) {"
        + "return false;"
        + "}"
        + "}"
        + "return false;"
        + "}")
    .using(new IfCommandsSequencerNoElseSingletonSequencer())
    .gives("static boolean equalsImpl(Set<?> os, @Nullable Object object) {"
        + "if (os == object) {"
        + "return true;"
        + "}"
        + "if (!(object instanceof Set))"
        + "return false;"
        + "Set<?> o = (Set<?>) object;"
        + "try {"
        + "return os.size() == o.size() && os.containsAll(o);"
        + "} catch (NullPointerException ignored) {"
        + "return false;"
        + "}"
        + "}");
  }
}
