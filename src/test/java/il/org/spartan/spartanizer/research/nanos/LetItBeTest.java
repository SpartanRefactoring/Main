package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link LetItBeIn}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-01 */
@SuppressWarnings("static-method")
public class LetItBeTest {
  @Test public void a() {
    trimmingOf("{{A x = foo(); return bar(x,x);} another();}")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .gives("{{ return bar(foo(),foo());} another();}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("{{A x = foo(); return bar(x,x);} another();}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("{{A x = foo(); return bar(y,y);} another();}")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .stays();
  }

  @Test public void d() {
    trimmingOf("{{A x = foo(); bar(x,x);} another();}")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .gives("{{  bar(foo(),foo());} another();}")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("{{A y = bar(), x = foo(); bar(x,x); print(y);} another();}")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .gives("{{A y = bar();  bar(foo(),foo()); print(y);} another();}")//
        .stays();
  }

  @Test public void f() {
    trimmingOf("{{A x = foo(); bar(x,x); print(x);} another();}")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .stays();
  }

  @Test public void g() {
    trimmingOf("{"//
        + "    final Object value=m.invoke(a);"//
        + "    if (value == null) {"//
        + "      throw new IllegalStateException(String.format(\"Annotation method %s returned null\",m));"//
        + "    }"//
        + "    $+=hashMember(m.getName(),value); }")//
            .using(VariableDeclarationFragment.class, new LetItBeIn())//
            .stays();
  }

  @Test public void h() {
    trimmingOf("{"//
        + "final AtmosphereInterceptor a=(AtmosphereInterceptor)f.newClassInstance(AtmosphereInterceptor.class,annotatedClass);"//
        + "f.getAtmosphereConfig().startupHook("//
        + "  new AtmosphereConfig.StartupHook(){"//
        + "    @Override public void started(    AtmosphereFramework framework){"//
        + "     framework.interceptor(a);"//
        + "    }"//
        + "  });"//
        + "}")//
            .using(VariableDeclarationFragment.class, new LetItBeIn())//
            .stays();
  }
}
