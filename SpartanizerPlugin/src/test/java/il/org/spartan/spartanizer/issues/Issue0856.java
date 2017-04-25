package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Tests of inline into next statement even if not last in block
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0856 {
  @Test public void a() {
    trimmingOf("A a(){A b=\"one expression\";B.d(b);return \"and another\";}") //
        .using(new LocalInitializedInlineIntoNext(), VariableDeclarationFragment.class) //
        .gives("A a(){B.d(\"one expression\");return \"and another\";}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("A foo(){A a=\"one expression\";C c=B.d(a);print(c);return \"and another\";}")
        .using(new LocalInitializedInlineIntoNext(), VariableDeclarationFragment.class)
        .gives("A foo(){C c=B.d(\"one expression\");print(c);return \"and another\";}")
        .using(new LocalInitializedInlineIntoNext(), VariableDeclarationFragment.class)
        .gives("A foo(){print(B.d(\"one expression\"));return \"and another\";}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("A a(){A b=\"one expression\";B.d(b);print(b);return \"and another\";}")
        .using(new LocalInitializedInlineIntoNext(), VariableDeclarationFragment.class)//
        .stays();
  }

  @Test public void d() {
    trimmingOf("L<O> t=new R<>();int len=A.t(d);for(int ¢=0; ¢ <len; ++¢)t.add(A.get(d, ¢));$.a(t);")//
        .gives("L<O> t=new R<>();for(int len=A.t(d), ¢=0; ¢ <len; ++¢)t.add(A.get(d, ¢));$.a(t);")//
        .stays();
  }

  @Test public void d1() {
    trimmingOf("t=new R<>();int len=A.t(d);for(int ¢=0; ¢ <len; ++¢)t.add(A.get(d, ¢));$.a(t);")//
        .gives("t=new R<>();for(int len=A.t(d), ¢=0; ¢ <len; ++¢)t.add(A.get(d, ¢));$.a(t);")//
        .stays();
  }

  @Test public void d2() {
    trimmingOf("int len=t();for(int ¢=0; ¢ <len; ++¢)t.add(x);")//
        .gives("for(int len=t(), ¢=0; ¢ <len; ++¢)t.add(x);")//
        .stays();
  }

  @Test public void d3() {
    trimmingOf("L<O> t=new R<>();int len=A.t(d);for(int ¢=0; ¢ <len; ++¢)t.add(A.get(d, ¢));$.a(t);")//
        .using(new LocalInitializedStatementToForInitializers(), VariableDeclarationFragment.class) //
        .gives("L<O> t=new R<>();for(int len=A.t(d), ¢=0; ¢ <len; ++¢)t.add(A.get(d, ¢));$.a(t);")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("I il=(I)((T)l).g();il.finalize(); return 0;")//
        .gives("((I)((T)l).g()).finalize();return 0;")//
        .stays();
  }
}
