package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Test Class For Issue #1014 (Eliminate conditional continue before last
 * statement in loop)
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-01-04 */
@SuppressWarnings("static-method")
public class Issue1014 {
  @Test public void test0() {
    trimmingOf("for (final MarkerAnnotation a : new definitionTest().markers()) {final String key = (a + \"\").substring(1);"
        + "if (!definition.Kind.has(key))continue;for (final SimpleName ¢ : annotees.of(a))$.add(as.array(definition.Kind.valueOf(key), ¢));}") //
            .gives("for (final MarkerAnnotation a : new definitionTest().markers()) {final String key = (a + \"\").substring(1);"
                + "if (definition.Kind.has(key))for (final SimpleName ¢ : annotees.of(a))$.add(as.array(definition.Kind.valueOf(key), ¢));}")
            .stays();
  }
  @Test public void test1() {
    trimmingOf("for (int i=0 ;i<length;++i){int a;if(a==b)continue; c= a+3;}") //
        .gives("for (int i=0 ;i<length;++i){int a;if(a!=b)c=a+3;}")//
        .stays();
  }
  @Test public void test2() {
    trimmingOf("while (q){int a;if(a==b)continue; c= a+3;}") //
        .gives("while (q){int a;if(a!=b)c=a+3;}")//
        .stays();
  }
}
