package il.org.spartan.spartanizer.utils;

import static il.org.spartan.spartanizer.engine.parse.e;
import static il.org.spartan.spartanizer.engine.parse.s;

import org.junit.Test;

import il.org.spartan.spartanizer.java.haz;

/** Unit tests for {@link haz}
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class hazTest {
  @Test public void seriesA_01() {
    assert !haz.variableDefinition(e("0"));
  }
  @Test public void seriesA_02() {
    assert haz.variableDefinition(s("int i;"));
  }
  @Test public void seriesA_03() {
    assert !haz.variableDefinition(s("class C {}"));
  }
  @Test public void seriesA_04() {
    assert !haz.variableDefinition(s("class C {void f(){}}"));
  }
  @Test public void seriesA_05() {
    assert haz.variableDefinition(s("class C {void f(){}int a = f();}"));
  }
  @Test public void seriesA_06() {
    assert haz.variableDefinition(s("class C {void f(){int a = new C().hashCode();}}"));
  }
  @Test public void seriesA_07() {
    assert haz.variableDefinition(s("class C {void f(){for (int a = 0;i <100; i++) ++i;}}"));
  }
  @Test public void seriesA_08() {
    assert haz.variableDefinition(s("class C {void f(int a){return a;}}"));
  }
  @Test public void seriesA_09() {
    assert haz.variableDefinition(s("try (O a = new O()) {}"));
  }
  @Test public void seriesA_10() {
    assert haz.variableDefinition(s("try {} catch (Exception x) {}"));
  }
}
