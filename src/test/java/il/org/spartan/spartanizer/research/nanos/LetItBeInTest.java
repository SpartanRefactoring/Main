package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link LetItBeIn}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-01 */
@SuppressWarnings("static-method")
public class LetItBeInTest {
  @Test public void a() {
    topDownTrimming("{{A λ = foo(); return bar(λ,λ);} another();}")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .gives("{{{return let(()->foo()).in(λ->bar(λ,λ));}}another();}") //
        .gives("return let(()->foo()).in(λ->bar(λ,λ));another();") //
        .gives("return let(()->foo()).in(λ->bar(λ,λ));") //
        .stays();
  }

  @Test public void b() {
    topDownTrimming("{{A x = foo(); return bar(x,x);} another();}")//
        .gives("A x = foo(); return bar(x,x); another();")//
        .gives("A x = foo(); return bar(x,x);")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("{{A x = foo(); return bar(y,y);} another();}")//
        .gives("A x=foo();return bar(y,y);another();") //
        .gives("foo(); return bar(y,y);") //
        .stays();
  }

  @Test public void d() {
    topDownTrimming("{{A x = foo(); bar(x,x);} another();}")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .gives("{{{let(()->foo()).in(x->bar(x,x));}}another();}") //
        .gives("let(()->foo()).in(x->bar(x,x));another();") //
        .gives("let(()->foo()).in(λ->bar(λ,λ));another();") //
        .stays();
  }

  @Test public void e() {
    topDownTrimming("{{A y = bar(), x = foo(); bar(x,x); print(y);} another();}")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .stays();
  }

  @Test public void i() {
    topDownTrimming("X x = foo(); bar1(x,x); bar2(x);")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .stays();
  }

  @Test public void f() {
    topDownTrimming("{{A x = foo(); bar(x,x); print(x);} another();}")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .stays();
  }

  @Test public void g() {
    topDownTrimming("{"//
        + "    final Object value=m.invoke(a);"//
        + "    if (value == null) {"//
        + "      throw new IllegalStateException(String.format(\"Annotation method %s returned null\",m));"//
        + "    }"//
        + "    $+=hashMember(m.getName(),value); }")//
            .using(VariableDeclarationFragment.class, new LetItBeIn())//
            .stays();
  }

  @Test public void h() {
    topDownTrimming("{"//
        + "final AtmosphereInterceptor a=(AtmosphereInterceptor)f.newClassInstance(AtmosphereInterceptor.class,annotatedClass);"//
        + "f.getAtmosphereConfig().startupHook("//
        + "  new AtmosphereConfig.StartupHook(){"//
        + "    @Override public void started(    AtmosphereFramework framework){"//
        + "     framework.interceptor(a);"//
        + "    }"//
        + "  });"//
        + "}")//
            .using(VariableDeclarationFragment.class, new LetItBeIn())//
            .gives(
                "{{let(()->(AtmosphereInterceptor)f.newClassInstance(AtmosphereInterceptor.class,annotatedClass)).in(a->f.getAtmosphereConfig().startupHook(new AtmosphereConfig.StartupHook(){@Override public void started(AtmosphereFramework framework){framework.interceptor(a);}}));}}") //
            .gives(
                "let(()->(AtmosphereInterceptor)f.newClassInstance(AtmosphereInterceptor.class,annotatedClass)).in(a->f.getAtmosphereConfig().startupHook(new AtmosphereConfig.StartupHook(){@Override public void started(AtmosphereFramework framework){framework.interceptor(a);}}));") //
            .gives(
                "let(()->(AtmosphereInterceptor)f.newClassInstance(AtmosphereInterceptor.class,annotatedClass)).in(a->f.getAtmosphereConfig().startupHook(new AtmosphereConfig.StartupHook(){@Override public void started(AtmosphereFramework ¢){¢.interceptor(a);}}));") //
            .stays();
  }
}
