/**
 *
 */
package il.org.spartan.classfiles.reify;

import static org.junit.Assert.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 28 November 2011 */
@SuppressWarnings("static-method") public class AttributeTest {
  @Test public void iterate() {
    class _ {
      // empty
    }
    for (final Attribute.Content c : Attribute.Extractor.attributes(new _()))
      c.hashCode();
  }

  @Test public void manyAttributeNames() {
    class _ {
      @Attribute public int anotherAttribute() {
        return 3;
      }

      @Override //
      @Attribute public int hashCode() {
        return 3;
      }

      @Attribute public int intAttribute() {
        return 3;
      }
    }
    assertEquals("hashCode", Attribute.Extractor.attributes(new _()).get(0).name);
    assertEquals("intAttribute", Attribute.Extractor.attributes(new _()).get(1).name);
    assertEquals("anotherAttribute", Attribute.Extractor.attributes(new _()).get(2).name);
  }

  @Test public void manyAttributeValues() {
    class _ {
      @Attribute public int anotherAttribute() {
        return 21;
      }

      @Override //
      @Attribute public int hashCode() {
        return 3;
      }

      @Attribute public int intAttribute() {
        return 19;
      }
    }
    assertEquals("3", Attribute.Extractor.attributes(new _()).get(0).value);
    assertEquals("19", Attribute.Extractor.attributes(new _()).get(1).value);
    assertEquals("21", Attribute.Extractor.attributes(new _()).get(2).value);
  }

  @Test public void methodAttributeName() {
    class _ {
      @Attribute public int intAttribute() {
        return 3;
      }
    }
    assertEquals("intAttribute", Attribute.Extractor.attributes(new _()).get(0).name);
  }

  @Test public void nonNull() {
    class _ {
      // empty
    }
    assert null != Attribute.Extractor.attributes(new _());
  }
}
