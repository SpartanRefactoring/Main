package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class ForEachTest {
  @Test public void a() {
    trimmingOf(" for (Ls ¢ : br ? listeners : rImpl.al()) ¢.oB(e);")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("(br?listeners:rImpl.al()).forEach(¢->¢.oB(e));")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("for (Entry<URI, O> entry : oC.entrySet()) { types.gt().add(entry.getValue());}")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("for(Entry<URI,O>¢:oC.entrySet())types.gt().add(entry.getValue());")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("for (Class i : in) try { " //
        + " l.add((A)f.newClassInstance(H.class,i));} " //
        + "catch (Throwable ¢) {" //
        + "logger.warn(\"\",¢);" //
        + "}")//
            .using(EnhancedForStatement.class, new ForEach())//
            .gives("in.forEach(i->{try{l.add((A)f.newClassInstance(H.class,i));}catch(Throwable ¢){{logger.warn(\"\",¢);}}});")//
            .gives("in.forEach(i->{try{l.add((A)f.newClassInstance(H.class,i));}catch(Throwable ¢){logger.warn(\"\",¢);}});")//
            .stays();
  }

  @Test public void d() {
    trimmingOf("for (Class<? extends B> ¢ : bf) f.broadcasterFilters(f.newClassInstance(B.class,b));")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("bf.forEach(¢->f.broadcasterFilters(f.newClassInstance(B.class,b)));")//
        .stays();
  }

  @Test public void e() {
    trimmingOf(" for (final Statement k : ss) $.append(k);")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("ss.forEach(k -> $.append(k));")//
        .gives("ss.forEach(¢ -> $.append(¢));")//
        .stays();
  }

  @Test public void f() {
    trimmingOf("for (final I ¢ : us) sc(¢, eclipse.newS(pM));")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("us.forEach(¢->sc(¢,eclipse.newS(pM)));")//
        .stays();
  }
}
