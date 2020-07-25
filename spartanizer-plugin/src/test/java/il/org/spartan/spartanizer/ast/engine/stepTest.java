package il.org.spartan.spartanizer.ast.engine;

import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static il.org.spartan.spartanizer.engine.parse.cu;
import static il.org.spartan.spartanizer.engine.parse.i;

import java.util.List;

import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.junit.Test;

import fluent.ly.azzert;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.navigate.step;

/** A test suite for class {@link step}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@SuppressWarnings({ "static-method", "javadoc" })
public final class stepTest {
  @Test public void chainComparison() {
    azzert.that(right(i("a == true == b == c")) + "", is("c"));
  }
  @Test public void imports() {
    final List<ImportDeclaration> li = step.importDeclarations(cu("import a.b.c; class c{}"));
    azzert.that(li.size(), is(1));
    azzert.that(the.firstOf(li).getName() + "", is("a.b.c"));
  }
  @Test public void importsNames() {
    final List<String> li = step.importDeclarationsNames(cu("import a.b.c; class c{}"));
    azzert.that(li.size(), is(1));
    azzert.that(the.firstOf(li), is("a.b.c"));
  }
  @Test public void importsNames2() {
    final List<String> li = step.importDeclarationsNames(cu("import a.b.c; import static f.g.*; import java.util.*; class c{}"));
    azzert.that(li.size(), is(3));
    azzert.that(the.firstOf(li), is("a.b.c"));
    azzert.that(li.get(1), is("static f.g.*"));
    azzert.that(li.get(2), is("java.util.*"));
  }
}
