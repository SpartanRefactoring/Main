package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    trimmingOf("for (A r : rs) if (u(r.tr())) try { r.um(); } catch (Tr ¢) { g.tr(\"ra\",¢); }")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives("rs.stream().filter(r->u(r.tr())).forEach(r->{try{r.um();}catch(Tr ¢){{g.tr(\"ra\",¢);}}});")//
        .gives("rs.stream().filter(λ->u(λ.tr())).forEach(r->{try{r.um();}catch(Tr ¢){g.tr(\"ra\",¢);}});")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("for (A r : (B)rs) if (u(r.tr())) try { r.um(); } catch (Tr ¢) { g.tr(\"ra\",¢); }")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives("((B)rs).stream().filter(r->u(r.tr())).forEach(r->{try{r.um();}catch(Tr ¢){{g.tr(\"ra\",¢);}}});")
        .gives("((B)rs).stream().filter(λ->u(λ.tr())).forEach(r->{try{r.um();}catch(Tr ¢){g.tr(\"ra\",¢);}});")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("for (M ¢ : mt(ref)) if (!¢.isCtr() && !iz.st¢(¢) && !iz.¢(¢) && !iz.pr¢(¢)) put(gl(¢), ¢);")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives("(mt(ref)).stream().filter(¢->!¢.isCtr()&&!iz.st¢(¢)&&!iz.¢(¢)&&!iz.pr¢(¢)).forEach(¢->put(gl(¢),¢));")//
        .gives("(mt(ref)).stream().filter(λ->!λ.isCtr()&&!iz.st¢(λ)&&!iz.¢(λ)&&!iz.pr¢(λ)).forEach(λ->put(gl(λ),λ));")//
        .stays();
  }
}
