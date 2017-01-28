package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/** Tests of inline into next statment even if not last in block
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0856 {
  @Test public void a() {
    trimmingOf("A a(){A b=One;B.d(b);return Two;}") //
        .using(VariableDeclarationFragment.class, new FragmentInitializerInlineIntoNext()) //
        .gives("A a(){B.d(One);return Two;}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("A foo(){A a=One;C c=B.d(a);print(c);return Two;}").using(VariableDeclarationFragment.class, new FragmentInitializerInlineIntoNext())
        .gives("A foo(){C c=B.d(One);print(c);return Two;}").using(VariableDeclarationFragment.class, new FragmentInitializerInlineIntoNext())
        .gives("A foo(){print(B.d(One));return Two;}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("A a(){A b=One;B.d(b);print(b);return Two;}").using(VariableDeclarationFragment.class, new FragmentInitializerInlineIntoNext())//
        .stays();
  }

  @Ignore // TODO Yossi Gil
  @Test public void d() {
    trimmingOf("L<O> t=new R<>();int len=A.t(d);for(int ¢=0; ¢ <len; ++¢)t.add(A.get(d, ¢));$.a(t);")//
        .stays();
  }

  @Ignore // TODO Yossi Gil
  @Test public void d1() {
    trimmingOf("t=new R<>();int len=A.t(d);for(int ¢=0; ¢ <len; ++¢)t.add(A.get(d, ¢));$.a(t);")//
        .stays();
  }

  @Ignore // TODO Yossi Gil
  @Test public void d2() {
    trimmingOf("int len=t();for(int ¢=0; ¢ <len; ++¢)t.add(x);")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("I il=(I)((T)l).g();il.finalize(); return 0;")//
        .gives("((I)((T)l).g()).finalize();return 0;")//
        .stays();
  }
}
