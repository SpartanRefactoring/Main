package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import java.awt.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.engine.*;

/** A test class constructed by TDD for {@link dig.stringLiterals}
 * @author Yossi Gil
 * @author Dan Greenstein
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue404 {
  /** Ensure that there is a type named {@link dig}
   * <p>
   * Meta information: There are no established rules on names of test methods.
   * This class demonstrates the <b>Dewey<b> notation: A pattern of -of naming
   * methods as follows (variations are possible):
   * <ul>
   * <li><code>a()<code>, <code>b<()code>, <code>c()<code>, ...
   * <li>and then, when you need to study a failure of <code>w()</code> better,
   * <code>wa()</code>, <code>wb()</code> <code>wc()</code>, etc.
   * <li>and then, when you fixed the fault at <code>w()</code>, proceed with
   * series, <code>x()</code>, <code>y()</code>, etc.
   * <li>and then, when you reached <code>z()</code>, and more names are needed,
   * rename the sequence of methods generated so far:
   *
   * <pre>
   a(), b(), c(), ..., w(),  wa(), wb(), wc(), ..., x(), y(), z()
   * </pre>
   *
   * to (say)
   *
   * <pre>
   Aa(), Ab(), Ac(), ..., Aw(),  Awa(), Awb(), Awc(), ..., Ax(), Ay(), Az()
   * </pre>
   *
   * and proceed to generating tests named
   *
   * <pre>
   a(), b(), c(), ..., w(),  wa(), wb(), wc(), ..., x(), y(), z()
   * </pre>
   **
   * <li>and then, when you finish the entire
   * </ul>
   * <p>
   * <b>be sure to use</b>
   *
   * <pre>
  &#64;FixMethodOrder(MethodSorters.NAME_ASCENDING) //
   * </pre>
   *
   * annotation on your test class */
  @Test public void a() {
    dig.class.hashCode();
  }

  /** Make sure that {@link dig} is an <code>interface</code> */
  @Test public void b() {
    assert dig.class.isInterface();
  }

  @Test public void c() {
    assert !dig.class.isEnum();
  }

  @Test public void d() {
    dig.stringLiterals(null);
  }

  @Test public void e() {
    (dig.stringLiterals(null) + "").hashCode();
  }

  @Test public void f() {
    dig.stringLiterals(null).hashCode();
  }

  @Test public void g() {
    assert dig.stringLiterals(null) != null;
  }
  
  @Test public void h() {
    assert dig.stringLiterals(null).isEmpty();
  }
  
  @Test public void i() {
    assert dig.stringLiterals(into.e("\"\"")).size() == 1;
    assert "".equals(dig.stringLiterals(into.e("\"\"")).get(0));
  }
  
  @Test public void j() {
    assert dig.stringLiterals(into.e("\"str\"")).size() == 1;
    assert "str".equals(dig.stringLiterals(into.e("\"\"")).get(0));
  }

  /** Correct way of trimming does not change */
  @Test public void Z$140() {
    trimmingOf("a").stays();
  }
}
