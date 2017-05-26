package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link Examiner}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class ExaminerTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new Examiner());
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
    assert is("@Override public boolean apply( Object ¢){ return ¢ == null;}");
  }
  @Test public void f() {
    assert is("public boolean matchesNoneOf(CharSequence ¢) {    return indexIn(¢) == -1;  }");
  }
  @Test public void g() {
    assert is("boolean examiner(){synchronized (mutex) {return field == 7;}}");
  }
  // h and i are examiners because we killed caster
  @Test public void h() {
    assert is("boolean checker(Object a){return a instanceof A;}");
  }
  @Test public void i() {
    assert is("boolean checker(Object a){return (a instanceof A);}");
  }
}
