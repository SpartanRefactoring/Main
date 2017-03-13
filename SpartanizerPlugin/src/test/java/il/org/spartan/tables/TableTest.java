package il.org.spartan.tables;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** Unit test for {@link Table#classToNormalizedFileName(String)}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class TableTest {
  @Test public void test() {
    azzert.that(Table.classToNormalizedFileName("Table_NanosByCategories"), is("nanos-by-categories"));
  }

  @Test public void test2() {
    final String[] components = namer.components("Table_NanosByCategories");
    azzert.that(components, is(new String[] { "Table", "Nanos", "By", "Categories" }));
    azzert.that(separate.these(lisp.rest(as.iterable(components))).by('-').toLowerCase(), is("nanos-by-categories"));
  }

  @Test public void testi1() {
    azzert.that(separate.these(as.array("a", "b")).by('-'), is("a-b"));
  }
}
