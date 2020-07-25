package il.org.spartan.spartanizer.research.methods;

import org.junit.BeforeClass;
import org.junit.Test;

import il.org.spartan.spartanizer.research.nanos.methods.TypeChecker;

/** TODO orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class TypeCheckerTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new TypeChecker());
  }
  @Test public void a() {
    assert is("boolean checker(Object a){return a instanceof A;}");
  }
  @Test public void a1() {
    assert is("boolean checker(Object a){return (a instanceof A);}");
  }
  @Test public void a2() {
    assert not("boolean checker(Object a){return (b instanceof A);}");
  }
  @Test public void b() {
    assert not("int examiner(){return 7;}");
  }
  @Test public void c() {
    assert not("boolean examiner(Is this, The... real){return life && just.fantasy() && (caught == landslide || noEscape.from(reality));}");
  }
  @Test public void d() {
    assert not("@Override public boolean matchesNoneOf(CharSequence ¢){  return ¢.length() == 0;}");
  }
  @Test public void e() {
    assert not("@Override public boolean apply( Object ¢){ return ¢ == null;}");
  }
  @Test public void f() {
    assert not("public boolean matchesNoneOf(CharSequence ¢) {    return indexIn(¢) == -1;  }");
  }
  @Test public void g() {
    assert not("boolean examiner(){synchronized (mutex) {return field == 7;}}");
  }
  @Test public void h() {
    assert not("boolean examiner(Object a){a();}");
  }
}
