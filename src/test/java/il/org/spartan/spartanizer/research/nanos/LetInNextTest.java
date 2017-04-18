package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link LetInNext}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-01 */
@SuppressWarnings("static-method")
public class LetInNextTest {
  @Test public void a() {
    trimminKof("{{A λ = foo(); return bar(λ,λ);} another();}")//
        .using(new LetInNext(), VariableDeclarationFragment.class)//
        .gives("{{{return let(()->foo()).in(λ->bar(λ,λ));}}another();}") //
        .gives("return let(()->foo()).in(λ->bar(λ,λ));another();") //
        .gives("return let(()->foo()).in(λ->bar(λ,λ));") //
        .stays();
  }

  @Test public void b() {
    trimminKof("{{A x = foo(); return bar(x,x);} another();}")//
        .gives("A x = foo(); return bar(x,x); another();")//
        .gives("A x = foo(); return bar(x,x);")//
        .stays();
  }

  @Test public void c() {
    trimminKof("{{A x = foo(); return bar(y,y);} another();}")//
        .gives("A x=foo();return bar(y,y);another();") //
        .gives("foo(); return bar(y,y);") //
        .stays();
  }

  @Test public void d() {
    trimminKof("{{A x = foo(); bar(x,x);} another();}")//
        .using(new LetInNext(), VariableDeclarationFragment.class)//
        .gives("{{{let(()->foo()).in(x->bar(x,x));}}another();}") //
        .gives("let(()->foo()).in(x->bar(x,x));another();") //
        .gives("let(()->foo()).in(λ->bar(λ,λ));another();") //
        .stays();
  }

  @Test public void e() {
    trimminKof("{{A y = bar(), x = foo(); bar(x,x); print(y);} another();}")//
        .using(new LetInNext(), VariableDeclarationFragment.class)//
        .stays();
  }

  @Test public void i() {
    trimminKof("X x = foo(); bar1(x,x); bar2(x);")//
        .using(new LetInNext(), VariableDeclarationFragment.class)//
        .stays();
  }

  @Test public void f() {
    trimminKof("{{A x = foo(); bar(x,x); print(x);} another();}")//
        .using(new LetInNext(), VariableDeclarationFragment.class)//
        .stays();
  }

  @Test public void g() {
    trimminKof("{"//
        + "    final Object value=m.invoke(a);"//
        + "    if (value == null) {"//
        + "      throw new IllegalStateException(String.format(\"Annotation method %s returned null\",m));"//
        + "    }"//
        + "    $+=hashMember(m.getName(),value); }")//
            .using(new LetInNext(), VariableDeclarationFragment.class)//
            .stays();
  }

  @Test public void h() {
    trimminKof("{"//
        + "final AtmosphereInterceptor a=(AtmosphereInterceptor)f.newClassInstance(AtmosphereInterceptor.class,annotatedClass);"//
        + "f.getAtmosphereConfig().startupHook("//
        + "  new AtmosphereConfig.StartupHook(){"//
        + "    @Override public void started(    AtmosphereFramework framework){"//
        + "     framework.interceptor(a);"//
        + "    }"//
        + "  });"//
        + "}")//
            .using(new LetInNext(), VariableDeclarationFragment.class)//
            .gives(
                "{{let(()->(AtmosphereInterceptor)f.newClassInstance(AtmosphereInterceptor.class,annotatedClass)).in(a->f.getAtmosphereConfig().startupHook(new AtmosphereConfig.StartupHook(){@Override public void started(AtmosphereFramework framework){framework.interceptor(a);}}));}}") //
            .gives(
                "let(()->(AtmosphereInterceptor)f.newClassInstance(AtmosphereInterceptor.class,annotatedClass)).in(a->f.getAtmosphereConfig().startupHook(new AtmosphereConfig.StartupHook(){@Override public void started(AtmosphereFramework framework){framework.interceptor(a);}}));") //
            .gives(
                "let(()->(AtmosphereInterceptor)f.newClassInstance(AtmosphereInterceptor.class,annotatedClass)).in(a->f.getAtmosphereConfig().startupHook(new AtmosphereConfig.StartupHook(){@Override public void started(AtmosphereFramework ¢){¢.interceptor(a);}}));") //
            .stays();
  }
}
