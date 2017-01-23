package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-18 */
@SuppressWarnings("static-method")
public class CollectTest {
  @Test public void a() {
    trimmingOf("final L<SimpleName> $ = new ArrayList<>();    for (final VariableDeclarationFragment ¢ : fs)      $.add(¢.getName());")//
        .withTippers(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
        .gives("L<SimpleName>$=(fs).stream().map(¢->¢.getName()).collect(Collectors.toList());")//
        .stays();
  }

  @Test public void b() {
    trimmingOf(
        "final L<SimpleName> $ = new ArrayList<>();    for (final VariableDeclarationFragment ¢ : fs)  if(iLikeTo(a))    $.add(¢.getName());")//
            .withTippers(EnhancedForStatement.class, new ForEachSuchThat(), new ForEach(), new Collect())//
            .gives("L<SimpleName>$=(fs).stream().filter(¢->iLikeTo(a)).map(¢->¢.getName()).collect(Collectors.toList());")//
            .stays();
  }

  @Test public void c() {
    trimmingOf("final L<SimpleName> $ = new ArrayList<>();    for (final VariableDeclarationFragment ¢ : fs)  if(iLikeTo(a))    $.add(¢);")//
        .withTippers(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
        .gives("L<SimpleName>$=(fs).stream().filter(¢->iLikeTo(a)).collect(Collectors.toList());")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("final L<SimpleName> $ = new ArrayList<>();    for (final VariableDeclarationFragment ¢ : fs) $.add(¢);")//
        .withTippers(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
        .gives("L<SimpleName>$=(fs).stream().collect(Collectors.toList());")//
        .stays();
  }

  @Test public void e() {
    trimmingOf(
        "for (final L<MethodDeclaration> sentence : allSentences()) for (final MethodDeclaration ¢ : sentence)    if (disabling.specificallyDisabled(¢))      $.add(____(¢));")//
            .withTippers(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
            .gives(
                "for(final L<MethodDeclaration>sentence:allSentences())$.addAll((sentence).stream().filter(¢->disabling.specificallyDisabled(¢)).map(¢->____(¢)).collect(Collectors.toList()));")//
            .stays();
  }

  @Test public void e0() {
    trimmingOf("for (S s : as()) for (M ¢ : s) if (a.b(¢)) $.add(____(¢));")//
        .withTippers(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
        .gives("for(S s:as())$.addAll((s).stream().filter(¢->a.b(¢)).map(¢->____(¢)).collect(Collectors.toList()));")//
        .stays();
  }

  @Test public void f() {
    trimmingOf("final Set<M> $ = new LinkedHashSet<>();  for (final IExtendedModifier ¢ : ms)    if (test(¢, ps))      $.add((M) ¢);")//
        .withTippers(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
        .gives("Set<M>$=(ms).stream().filter(¢->test(¢,ps)).map(¢->(M)¢).collect(Collectors.toList());")//
        .stays();
  }

  @Test public void g() {
    trimmingOf(
        "final L<String> $ = new ArrayList<>();  for (final Element e : J.parse(d.getHtml()).select(\"div.Section1\").first().children())    $.add(e.tagName()); return $;")//
            .withTippers(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
            .gives(
                "L<String>$=(J.parse(d.getHtml()).select(\"div.Section1\").first().children()).stream().map(e->e.tagName()).collect(Collectors.toList());return $;")//
            .gives(
                "return(J.parse(d.getHtml()).select(\"div.Section1\").first().children()).stream().map(e->e.tagName()).collect(Collectors.toList());")//
            .stays();
  }
}
