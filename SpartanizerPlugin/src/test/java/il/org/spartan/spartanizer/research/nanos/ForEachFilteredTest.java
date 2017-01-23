package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class ForEachFilteredTest{
  @Test public void a(){
    trimmingOf("for(A ¢ : is? thiz : theReal) if(f) j();")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives("(is?thiz:theReal).stream().filter(¢->f).forEach(¢->j());")//
        .stays();
  }

  @Test public void b(){
    trimmingOf("for(A ¢ :(B)c) if(f) j();")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives("((B)c).stream().filter(¢->f).forEach(¢->j());")//
        .stays();
  }

  @Test public void c(){
    trimmingOf("for(A r : rs) if(U.t(r.tr())) try{r.es(); }catch(Throwable ¢){l.ac(\"ma\",¢); }")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives("rs.stream().filter(r->U.t(r.tr())).forEach(r->{try{r.es();}catch(Throwable ¢){{l.ac(\"ma\",¢);}}});")//
        .gives("rs.stream().filter(¢->U.t(¢.tr())).forEach(¢->{try{¢.es();}catch(Throwable ¢){{l.ac(\"ma\",¢);}}});")//
        .gives("rs.stream().filter(¢->U.t(¢.tr())).forEach(¢->{try{¢.es();}catch(Throwable ¢){l.ac(\"ma\",¢);}});")//
        .stays();
  }

  @Test public void d(){
    trimmingOf("for(A r :(B)rs) if(U.t(r.tr())) try{r.es(); }catch(Throwable ¢){l.ac(\"ma\",¢); }")//
        .using(EnhancedForStatement.class, new ForEachSuchThat())//
        .gives("((B)rs).stream().filter(r->U.t(r.tr())).forEach(r->{try{r.es();}catch(Throwable ¢){{l.ac(\"ma\",¢);}}});")//
        .gives("((B)rs).stream().filter(¢->U.t(r.tr())).forEach(¢->{try{r.es();}catch(Throwable ¢){l.ac(\"ma\",¢);}});")//
        .stays();
  }

  @Test public void e() {
    trimmingOf(
        "for(final D ¢ : s.th(ref)) if(!¢.isConstructor() && !iz.static¢(¢) && !iz.final¢(¢) && !iz.private¢(¢)) put(mangle(¢), ¢);")//
            .using(EnhancedForStatement.class, new ForEachSuchThat())//
            .gives(
                "(s.th(ref)).stream().filter(¢->!¢.isConstructor()&&!iz.static¢(¢)&&!iz.final¢(¢)&&!iz.private¢(¢)).forEach(¢->put(mangle(¢),¢));")//
            .stays();
  }
}
