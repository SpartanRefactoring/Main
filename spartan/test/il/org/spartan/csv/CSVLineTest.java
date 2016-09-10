// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.csv;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.streotypes.*;

@SuppressWarnings("static-method") @TestCase public class CSVLineTest {
  @Test public void anObjectTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Some Object", new Object() {
      @Override public String toString() {
        return "some object";
      }
    });
    assertEquals("Some Object", c.header());
    assertEquals("some object", c.line());
  }

  @Test public void anObjectTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Some Object", new Object() {
      @Override public String toString() {
        return "some object";
      }
    });
    assertEquals("Some Object", c.header());
    assertEquals("some object", c.line());
  }

  @Test public void arrayNullTest() {
    final CSVLine c = new CSVLine.Sorterd().put("NullArray", (String[]) null, 2);
    assertEquals("NullArray", c.header());
    assertEquals("", c.line());
  }

  @Test public void arrayNullTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("NullArray", (String[]) null, 2);
    assertEquals("NullArray", c.header());
    assertEquals("", c.line());
  }

  @Test public void arrayOutOfBoundsTest() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Sorterd().put("OutOfBounds", a, 5);
    assertEquals("OutOfBounds", c.header());
    assertEquals("", c.line());
  }

  @Test public void arrayOutOfBoundsTestOrdered() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Ordered().put("OutOfBounds", a, 5);
    assertEquals("OutOfBounds", c.header());
    assertEquals("", c.line());
  }

  @Test public void arrayTest() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Sorterd().put("third", a, 3);
    assertEquals("third", c.header());
    assertEquals("four", c.line());
  }

  @Test public void arrayTestOrdered() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Ordered().put("third", a, 3);
    assertEquals("third", c.header());
    assertEquals("four", c.line());
  }

  @Test public void elementsSortTest() {
    final String[] a = { "one", "two", "three", "four" };
    final CSVLine c = new CSVLine.Sorterd();
    for (final String s : a)
      c.put(s + "__key", s + "__value");
    assertEquals("four_key,one_key,three_key,two_key", c.header());
    assertEquals("four_value,one_value,three_value,two_value", c.line());
  }

  @Test public void emptyTest() {
    final CSVLine c = new CSVLine.Sorterd();
    assertEquals("", c.header());
    assertEquals("", c.line());
  }

  @Test public void emptyTestOrdered() {
    final CSVLine c = new CSVLine.Ordered();
    assertEquals("", c.header());
    assertEquals("", c.line());
  }

  @Test public void enumArrayTest() {
    final Week[] a = { Week.Sunday, Week.Saturday, Week.Friday };
    final CSVLine c = new CSVLine.Sorterd().put("String Array", a);
    assertEquals("String Array", c.header());
    assertEquals("Sunday;Saturday;Friday", c.line());
  }

  @Test public void enumArrayTestOrdered() {
    final Week[] a = { Week.Sunday, Week.Saturday, Week.Friday };
    final CSVLine c = new CSVLine.Ordered().put("String Array", a);
    assertEquals("String Array", c.header());
    assertEquals("Sunday;Saturday;Friday", c.line());
  }

  @Test public void intTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Question", 42);
    assertEquals("Question", c.header());
    assertEquals("42", c.line());
  }

  @Test public void intTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Question", 42);
    assertEquals("Question", c.header());
    assertEquals("42", c.line());
  }

  @Test public void nullObjectTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Null Header", (Object) null);
    assertEquals("Null Header", c.header());
    assertEquals("", c.line());
  }

  @Test public void nullObjectTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Null Header", (Object) null);
    assertEquals("Null Header", c.header());
    assertEquals("", c.line());
  }

  @Test public void nullStringTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Null Header", (String) null);
    assertEquals("Null Header", c.header());
    assertEquals("", c.line());
  }

  @Test public void nullStringTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Null Header", (String) null);
    assertEquals("Null Header", c.header());
    assertEquals("", c.line());
  }

  @Test public void oneFieldTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Hello", "World");
    assertEquals("Hello", c.header());
    assertEquals("World", c.line());
  }

  @Test public void oneFieldTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Hello", "World");
    assertEquals("Hello", c.header());
    assertEquals("World", c.line());
  }

  @Test public void simpleArrayTest() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Sorterd().put("String Array", a);
    assertEquals("String Array", c.header());
    assertEquals("One;two;three;four", c.line());
  }

  @Test public void simpleArrayTestOrdered() {
    final String[] a = { "One", "two", "three", "four" };
    final CSVLine c = new CSVLine.Ordered().put("String Array", a);
    assertEquals("String Array", c.header());
    assertEquals("One;two;three;four", c.line());
  }

  @Test public void testOrder() {
    final String[] a = { "one", "two", "three", "four" };
    final CSVLine c = new CSVLine.Ordered();
    for (final String s : a)
      c.put(s + "__key", s + "__value");
    assertEquals("one_key,two_key,three_key,four_key", c.header());
    assertEquals("one_value,two_value,three_value,four_value", c.line());
  }

  @Test public void testReplace() {
    final CSVLine c = new CSVLine.Sorterd().put("one", 1).put("two", 2).put("three", 3).put("two", "two").put("three", "three").put("one", "one");
    assertEquals("one,three,two", c.header());
    assertEquals("one,three,two", c.line());
  }

  @Test public void testReplaceOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("one", 1).put("two", 2).put("three", 3).put("two", "two").put("three", "three").put("one", "one");
    assertEquals("one,two,three", c.header());
    assertEquals("one,two,three", c.line());
  }

  @Test public void twoFieldsTest() {
    final CSVLine c = new CSVLine.Sorterd().put("Hello", "World").put("Bye", "Earth");
    assertEquals("Bye,Hello", c.header());
    assertEquals("Earth,World", c.line());
  }

  @Test public void twoFieldsTestOrdered() {
    final CSVLine c = new CSVLine.Ordered().put("Hello", "World").put("Bye", "Earth");
    assertEquals("Hello,Bye", c.header());
    assertEquals("World,Earth", c.line());
  }

  static enum Week {
    Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
  }
}
