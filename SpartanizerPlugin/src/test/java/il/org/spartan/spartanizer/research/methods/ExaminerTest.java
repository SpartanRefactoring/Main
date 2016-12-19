package il.org.spartan.spartanizer.research.methods;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.methods.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ExaminerTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    spartanizer.add(MethodDeclaration.class, JAVADOCER = new Examiner());
  }

  @Test public void a() {
    assert is("boolean examiner(){return field == 7;}");
  }

  @Test public void b() {
    assert not("int examiner(){return 7;}");
  }

  @Test public void c() {
    assert is("boolean examiner(Is this, The... real){return life && just.fantasy() && (caught == landslide || noEscape.from(reality));}");
  }

  @Test public void d() {
    assert is("@Override public boolean matchesNoneOf(CharSequence ¢){  return ¢.length() == 0;}");
  }

  @Test public void e() {
    assert is("@Override public boolean apply(@Nullable Object ¢){ return ¢ == null;}");
  }

  @Test public void f() {
    assert is("public boolean matchesNoneOf(CharSequence ¢) {    return indexIn(¢) == -1;  }");
  }
}
