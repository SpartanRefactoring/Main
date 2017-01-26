package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-18 */
@SuppressWarnings("static-method")
public class CollectTest {
  @Test public void a() {
    trimmingOf("final List<SimpleName> $ = new ArrayList<>();    for (final VariableDeclarationFragment ¢ : fs)      $.add(¢.getName());")//
        .using(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
        .gives("List<SimpleName>$=(fs).stream().map(¢->¢.getName()).collect(Collectors.toList());")//
        ;
  }

  @Test public void b() {
    trimmingOf(
        "final List<SimpleName> $ = new ArrayList<>();    for (final VariableDeclarationFragment ¢ : fs)  if(iLikeTo(a))    $.add(¢.getName());")//
            .using(EnhancedForStatement.class, new ForEachSuchThat(), new ForEach(), new Collect())//
            .gives("List<SimpleName>$=(fs).stream().filter(¢->iLikeTo(a)).map(¢->¢.getName()).collect(Collectors.toList());")//
            ;
  }

  @Test public void c() {
    trimmingOf("final List<SimpleName> $ = new ArrayList<>();    for (final VariableDeclarationFragment ¢ : fs)  if(iLikeTo(a))    $.add(¢);")//
        .using(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
        .gives("List<SimpleName>$=(fs).stream().filter(¢->iLikeTo(a)).collect(Collectors.toList());")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("final List<SimpleName> $ = new ArrayList<>();    for (final VariableDeclarationFragment ¢ : fs) $.add(¢);")//
        .using(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
        .gives("List<SimpleName>$=(fs).stream().collect(Collectors.toList());")//
        .stays();
  }

  @Test public void e() {
    trimmingOf(
        "for (final List<MethodDeclaration> sentence : allSentences()) for (final MethodDeclaration ¢ : sentence)    if (disabling.specificallyDisabled(¢))      $.add(____(¢));")//
            .using(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
            .gives(
                "for(final List<MethodDeclaration>sentence:allSentences())$.addAll((sentence).stream().filter(¢->disabling.specificallyDisabled(¢)).map(¢->____(¢)).collect(Collectors.toList()));")//
            .stays();
  }

  @Test public void e0() {
    trimmingOf("for (S s : as()) for (M ¢ : s) if (a.b(¢)) $.add(____(¢));")//
        .using(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
        .gives("for(S s:as())$.addAll((s).stream().filter(¢->a.b(¢)).map(¢->____(¢)).collect(Collectors.toList()));")//
        .stays();
  }

  @Test public void f() {
    trimmingOf("final Set<Modifier> $ = new LinkedHashSet<>();  for (final IExtendedModifier ¢ : ms)    if (test(¢, ps))      $.add((Modifier) ¢);")//
        .using(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
        .gives("Set<Modifier>$=(ms).stream().filter(¢->test(¢,ps)).map(¢->(Modifier)¢).collect(Collectors.toList());")//
        .stays();
  }

  @Test public void g() {
    trimmingOf(
        "final List<String> $ = new ArrayList<>();  for (final Element e : Jsoup.parse(d.getHtml()).select(\"div.Section1\").first().children())    $.add(e.tagName()); return $;")//
            .using(EnhancedForStatement.class, new ForEach(), new ForEachSuchThat(), new Collect())//
            .gives(
                "List<String>$=(Jsoup.parse(d.getHtml()).select(\"div.Section1\").first().children()).stream().map(e->e.tagName()).collect(Collectors.toList());return $;")//
            .gives(
                "return(Jsoup.parse(d.getHtml()).select(\"div.Section1\").first().children()).stream().map(e->e.tagName()).collect(Collectors.toList());")//
            .stays();
  }
}
