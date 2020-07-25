package il.org.spartan.tables;

import static fluent.ly.azzert.is;

import org.junit.Test;

import fluent.ly.as;
import fluent.ly.azzert;
import fluent.ly.cCamelCase;
import fluent.ly.separate;
import fluent.ly.the;

/** Unit test for {@link Table#classToNormalizedFileName(String)}
 * @author Yossi Gil
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class TableTest {
  @Test public void test() {
    azzert.that(Table.classToNormalizedFileName("Table_NanosByCategories"), is("nanos-by-categories"));
  }
  @Test public void test2() {
    final String[] components = cCamelCase.components("Table_NanosByCategories");
    azzert.that(components, is(new String[] { "Table", "Nanos", "By", "Categories" }));
    azzert.that(separate.these(the.tailOf(components)).by('-').toLowerCase(), is("nanos-by-categories"));
  }
  @Test public void testi1() {
    azzert.that(separate.these(as.array("a", "b")).by('-'), is("a-b"));
  }
}
