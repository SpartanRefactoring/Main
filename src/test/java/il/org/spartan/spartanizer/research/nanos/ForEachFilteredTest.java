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
 .stays();
 }

 @Test public void b() {
 trimmingOf("for ( A ¢ : (B)c) if (life) justFantasy();")//
 .using(EnhancedForStatement.class, new ForEachSuchThat())//
 .gives("((B)c).stream().filter(¢->life).forEach(¢->justFantasy());")//
 .stays();
 }

 @Test public void c() {
 trimmingOf(
 "for (A r : resources) if (u(r.transport())) try { r.resume(); } catch (Throwable ¢) { logger.trace(\"resumeAll\",¢); }")//
 .using(EnhancedForStatement.class, new ForEachSuchThat())//
 .gives(
 "resources.stream().filter(r->u(r.transport())).forEach(r->{try{r.resume();}catch(Throwable ¢){{logger.trace(\"resumeAll\",¢);}}});")//
 .gives(
 "resources.stream().filter(r->u(r.transport())).forEach(r->{try{r.resume();}catch(Throwable ¢){logger.trace(\"resumeAll\",¢);}});")//
 .stays();
 }

 @Test public void d() {
 trimmingOf(
 "for (A r : (B)resources) if (u(r.transport())) try { r.resume(); } catch (Throwable ¢) { logger.trace(\"resumeAll\",¢); }")//
 .using(EnhancedForStatement.class, new ForEachSuchThat())//
 .gives(
 "((B)resources).stream().filter(r->u(r.transport())).forEach(r->{try{r.resume();}catch(Throwable ¢){{logger.trace(\"resumeAll\",¢);}}});")//
 .gives(
 "((B)resources).stream().filter(r->u(r.transport())).forEach(r->{try{r.resume();}catch(Throwable ¢){logger.trace(\"resumeAll\",¢);}});")//
 .stays();
 }

 @Test public void e() {
 trimmingOf(
 "for (final M ¢ : step.methods(reflection)) if (!¢.isConstructor() && !iz.static¢(¢) && !iz.final¢(¢) && !iz.private¢(¢)) put(mangle(¢), ¢);")//
 .using(EnhancedForStatement.class, new ForEachSuchThat())//
 .gives(
 "(step.methods(reflection)).stream().filter(λ->!λ.isConstructor()&&!iz.static¢(¢)&&!iz.final¢(¢)&&!iz.private¢(¢)).forEach(¢->put(mangle(¢),¢));")//
 .stays();
 }
}
