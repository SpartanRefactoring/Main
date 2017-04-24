// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.csv;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.streotypes.*;

@TestCase @SuppressWarnings("static-method") public class CSVLineTest {
  @Test public void anObjectTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Some Object", new Object() {
      @Override public String toString() {
        return "some object";
      }
    });
    azzert.that(c.header(), is("Some Object"));
    azzert.that(c.line(), is("some object"));
  }

  @Test public void anObjectTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Some Object", new Object() {
      @Override public String toString() {
        return "some object";
      }
    });
    azzert.that(c.header(), is("Some Object"));
    azzert.that(c.line(), is("some object"));
  }

  @Test public void arrayNullTest() {
    final CSVLine c = new CSVLine.Sorterd().put("NullArray", (String[]) null, 2);
    azzert.that(c.header(), is("NullArray"));
    azzert.that(c.line(), is(""));
  }

  @Test public void arrayNullTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("NullArray", (String[]) null, 2);
    azzert.that(c.header(), is("NullArray"));
    azzert.that(c.line(), is(""));
  }

  @Test public void arrayOutOfBoundsTest() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Sorterd().put("OutOfBounds", a, 5);
    azzert.that(c.header(), is("OutOfBounds"));
    azzert.that(c.line(), is(""));
  }

  @Test public void arrayOutOfBoundsTestOrdered() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Ordered().put("OutOfBounds", a, 5);
    azzert.that(c.header(), is("OutOfBounds"));
    azzert.that(c.line(), is(""));
  }

  @Test public void arrayTest() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Sorterd().put("third", a, 3);
    azzert.that(c.header(), is("third"));
    azzert.that(c.line(), is("four"));
  }

  @Test public void arrayTestOrdered() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Ordered().put("third", a, 3);
    azzert.that(c.header(), is("third"));
    azzert.that(c.line(), is("four"));
  }

  @Test public void elementsSortTest() {
    final String[] a = { "one", "two", "three", "four" };
    final CSVLine c = new CSVLine.Sorterd();
    for (final String ¢ : a)
      c.put(¢ + "__key", ¢ + "__value");
    azzert.that(c.header(), is("four_key,one_key,three_key,two_key"));
    azzert.that(c.line(), is("four_value,one_value,three_value,two_value"));
  }

  @Test public void emptyTest() {
    final CSVLine c = new CSVLine.Sorterd();
    azzert.that(c.header(), is(""));
    azzert.that(c.line(), is(""));
  }

  @Test public void emptyTestOrdered() {
    final CSVLine c = new CSVLine.Ordered();
    azzert.that(c.header(), is(""));
    azzert.that(c.line(), is(""));
  }

  @Test public void enumArrayTest() {
    final Week[] a = { Week.Sunday, Week.Saturday, Week.Friday };
    final CSVLine c = new CSVLine.Sorterd().put("String Array", a);
    azzert.that(c.header(), is("String Array"));
    azzert.that(c.line(), is("Sunday;Saturday;Friday"));
  }

  @Test public void enumArrayTestOrdered() {
    final Week[] a = { Week.Sunday, Week.Saturday, Week.Friday };
    final CSVLine c = new CSVLine.Ordered().put("String Array", a);
    azzert.that(c.header(), is("String Array"));
    azzert.that(c.line(), is("Sunday;Saturday;Friday"));
  }

  @Test public void intTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Question", 42);
    azzert.that(c.header(), is("Question"));
    azzert.that(c.line(), is("42"));
  }

  @Test public void intTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Question", 42);
    azzert.that(c.header(), is("Question"));
    azzert.that(c.line(), is("42"));
  }

  @Test public void nullObjectTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Null Header", (Object) null);
    azzert.that(c.header(), is("Null Header"));
    azzert.that(c.line(), is(""));
  }

  @Test public void nullObjectTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Null Header", (Object) null);
    azzert.that(c.header(), is("Null Header"));
    azzert.that(c.line(), is(""));
  }

  @Test public void nullStringTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Null Header", (String) null);
    azzert.that(c.header(), is("Null Header"));
    azzert.that(c.line(), is(""));
  }

  @Test public void nullStringTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Null Header", (String) null);
    azzert.that(c.header(), is("Null Header"));
    azzert.that(c.line(), is(""));
  }

  @Test public void oneFieldTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Hello", "World");
    azzert.that(c.header(), is("Hello"));
    azzert.that(c.line(), is("World"));
  }

  @Test public void oneFieldTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Hello", "World");
    azzert.that(c.header(), is("Hello"));
    azzert.that(c.line(), is("World"));
  }

  @Test public void simpleArrayTest() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Sorterd().put("String Array", a);
    azzert.that(c.header(), is("String Array"));
    azzert.that(c.line(), is("One;two;three;four"));
  }

  @Test public void simpleArrayTestOrdered() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Ordered().put("String Array", a);
    azzert.that(c.header(), is("String Array"));
    azzert.that(c.line(), is("One;two;three;four"));
  }

  @Test public void testOrder() {
    final String[] a = { "one", "two", "three", "four" };
    final CSVLine c = new CSVLine.Ordered();
    for (final String ¢ : a)
      c.put(¢ + "__key", ¢ + "__value");
    azzert.that(c.header(), is("one_key,two_key,three_key,four_key"));
    azzert.that(c.line(), is("one_value,two_value,three_value,four_value"));
  }

  @Test public void testReplace() {
    final CSVLine c = new CSVLine.Sorterd().put("one", 1).put("two", 2).put("three", 3).put("two", "two").put("three", "three").put("one", "one");
    azzert.that(c.header(), is("one,three,two"));
    azzert.that(c.line(), is("one,three,two"));
  }

  @Test public void testReplaceOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("one", 1).put("two", 2).put("three", 3).put("two", "two").put("three", "three").put("one", "one");
    azzert.that(c.header(), is("one,two,three"));
    azzert.that(c.line(), is("one,two,three"));
  }

  @Test public void twoFieldsTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Hello", "World").put("Bye", "Earth");
    azzert.that(c.header(), is("Bye,Hello"));
    azzert.that(c.line(), is("Earth,World"));
  }

  @Test public void twoFieldsTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Hello", "World").put("Bye", "Earth");
    azzert.that(c.header(), is("Hello,Bye"));
    azzert.that(c.line(), is("World,Earth"));
  }

  enum Week {
    Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
  }
}
