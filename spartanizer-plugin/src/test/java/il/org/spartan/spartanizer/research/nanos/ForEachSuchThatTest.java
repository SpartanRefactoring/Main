package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link ForEachSuchThat}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class ForEachSuchThatTest {
  @Test public void a() {
    trimmingOf("for ( A ¢ : is? thiz : theReal) if (life) justFantasy();")//
        .using(new ForEachSuchThat(), EnhancedForStatement.class)//
        .gives("(is?thiz:theReal).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .using(new ForEachSuchThat(), EnhancedForStatement.class)//
        .stays();
  }
  @Test public void b() {
    trimmingOf("for ( A ¢ : (B)c) if (life) justFantasy();")//
        .using(new ForEachSuchThat(), EnhancedForStatement.class)//
        .gives("((B)c).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .gives("((B)c).stream().filter(λ->life).forEach(λ->justFantasy());")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("for (A r : rs) if (u(r.tr())) try { r.um(); } catch (Tr ¢) { g.tr(\"ra\",¢); }")//
        .using(new ForEachSuchThat(), EnhancedForStatement.class)//
        .gives("rs.stream().filter(r->u(r.tr())).forEach(r->{try{r.um();}catch(Tr ¢){{g.tr(\"ra\",¢);}}});")//
        .gives("rs.stream().filter(λ->u(λ.tr())).forEach(r->{try{r.um();}catch(Tr ¢){g.tr(\"ra\",¢);}});")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("for (A r : (B)rs) if (u(r.tr())) try { r.um(); } catch (Tr ¢) { g.tr(\"ra\",¢); }")//
        .using(new ForEachSuchThat(), EnhancedForStatement.class)//
        .gives("((B)rs).stream().filter(r->u(r.tr())).forEach(r->{try{r.um();}catch(Tr ¢){{g.tr(\"ra\",¢);}}});")
        .gives("((B)rs).stream().filter(λ->u(λ.tr())).forEach(r->{try{r.um();}catch(Tr ¢){g.tr(\"ra\",¢);}});")//
        .stays();
  }
  @Test public void e() {
    trimmingOf("for (M ¢ : mt(ref)) if (!¢.isCtr() && !iz.st¢(¢) && !iz.¢(¢) && !iz.pr¢(¢)) put(gl(¢), ¢);")//
        .using(new ForEachSuchThat(), EnhancedForStatement.class)//
        .gives("(mt(ref)).stream().filter(¢->!¢.isCtr()&&!iz.st¢(¢)&&!iz.¢(¢)&&!iz.pr¢(¢)).forEach(¢->put(gl(¢),¢));")//
        .gives("mt(ref).stream().filter(λ->!λ.isCtr()&&!iz.st¢(λ)&&!iz.¢(λ)&&!iz.pr¢(λ)).forEach(λ->put(gl(λ),λ));")//
        .stays();
  }
}
