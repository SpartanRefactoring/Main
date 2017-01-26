package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class ForEachFilteredTest {
  @Test public void a() {
    trimmingOf("for ( A ¢ : is? thiz : theReal) if (life) justFantasy();")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives("(is?thiz:theReal).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .stays();
  }

  @Test public void b() {
    trimmingOf("for ( A ¢ : (B)c) if (life) justFantasy();")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives("((B)c).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .gives("((B)c).stream().filter(λ->life).forEach(λ->justFantasy());")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("for (A r : rs) if (u(r.tr())) try { r.um(); } catch (Throwable ¢) { g.tr(\"ra\",¢); }")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives("rs.stream().filter(λ->u(λ.tr())).forEach(λ->{try{λ.um();}catch(Throwable λ){{g.tr(\"ra\",λ);}}});")//
        .gives("rs.stream().filter(λ->u(λ.tr())).forEach(λ->{try{λ.um();}catch(Throwable λ){g.tr(\"ra\",λ);}});")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("for (A r : (B)rs) if (u(r.tr())) try { r.um(); } catch (Throwable ¢) { g.tr(\"ra\",¢); }")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives(
            "((B)rs).stream().filter(λ->u(λ.tr())).forEach(λ->{try{λ.um();}catch(Throwable λ){{g.tr(\"ra\",λ);}}});")//
        .gives(
            "((B)rs).stream().filter(λ->u(λ.tr())).forEach(λ->{try{λ.um();}catch(Throwable λ){g.tr(\"ra\",λ);}});")//
        .stays();
  }

  @Test public void e() {
    trimmingOf(
        "for (final M ¢ : methods(ref)) if (!¢.isCtr() && !iz.static¢(¢) && !iz.final¢(¢) && !iz.private¢(¢)) put(mangle(¢), ¢);")//
            .using(EnhancedForStatement.class, new ForEachSuchThat())//
            .gives(
                "(methods(ref)).stream().filter(λ->!λ.isCtr()&&!iz.static¢(λ)&&!iz.final¢(λ)&&!iz.private¢(λ)).forEach(λ->put(mangle(λ),λ));")//
            .stays();
  }
}
