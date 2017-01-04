package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** A test clas for {@link ParenthesizedRemoveExtraParenthesis}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-02 */
@SuppressWarnings("static-method")
public class ParenthesizedRemoveExtraParenthesisTest {
  @Test public void a() {
    trimmingOf("((a + b))")//
        .gives("(a+b)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("((T)b)")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("a((b))")//
        .gives("a(b)")//
        .stays();
  }
}
