package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class ForEachFilteredTest {
  @Test public void a() {
    trimmingOf("for (  A ¢ : is? thiz : theReal)   if (life) justFantasy();")//
        .using(EnhancedForStatement.class, new ForEachFiltered())//
        .gives("(is?thiz:theReal).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("for (  A ¢ : (B)c)   if (life) justFantasy();")//
        .using(EnhancedForStatement.class, new ForEachFiltered())//
        .gives("((B)c).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .stays();
  }

  @Test public void c() {
    trimmingOf(
        "for (AtmosphereResource r : resources) if (Utils.resumableTransport(r.transport())) try {    r.resume();  }   catch (Throwable ¢) {    logger.trace(\"resumeAll\",¢);  }")//
            .using(EnhancedForStatement.class, new ForEachFiltered())//
            .gives(
                "resources.stream().filter(r->Utils.resumableTransport(r.transport())).forEach(r->{try{r.resume();}catch(Throwable ¢){{logger.trace(\"resumeAll\",¢);}}});")//
            .gives(
                "resources.stream().filter(r->Utils.resumableTransport(r.transport())).forEach(r->{try{r.resume();}catch(Throwable ¢){logger.trace(\"resumeAll\",¢);}});")//
            .stays();
  }

  @Test public void d() {
    trimmingOf(
        "for (AtmosphereResource r : (B)resources) if (Utils.resumableTransport(r.transport())) try {    r.resume();  }   catch (Throwable ¢) {    logger.trace(\"resumeAll\",¢);  }")//
            .using(EnhancedForStatement.class, new ForEachFiltered())//
            .gives(
                "((B)resources).stream().filter(r->Utils.resumableTransport(r.transport())).forEach(r->{try{r.resume();}catch(Throwable ¢){{logger.trace(\"resumeAll\",¢);}}});")//
            .gives(
                "((B)resources).stream().filter(r->Utils.resumableTransport(r.transport())).forEach(r->{try{r.resume();}catch(Throwable ¢){logger.trace(\"resumeAll\",¢);}});")//
            .stays();
  }
}
