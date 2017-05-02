package il.org.spartan.classfiles.reify;

import static il.org.spartan.azzert.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since 28 November 2011 */
@SuppressWarnings("static-method") public class AttributeTest {
  @Test public void iterate() {
    class __ {
      // empty
    }
    for (@NotNull final Attribute.Content ¢ : Attribute.Extractor.attributes(new __()))
      ¢.hashCode();
  }

  @Test public void manyAttributeNames() {
    class __ {
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
    azzert.that(Attribute.Extractor.attributes(new __()).get(0).name, is("hashCode"));
    azzert.that(Attribute.Extractor.attributes(new __()).get(1).name, is("intAttribute"));
    azzert.that(Attribute.Extractor.attributes(new __()).get(2).name, is("anotherAttribute"));
  }

  @Test public void manyAttributeValues() {
    class __ {
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
    azzert.that(Attribute.Extractor.attributes(new __()).get(0).value, is("3"));
    azzert.that(Attribute.Extractor.attributes(new __()).get(1).value, is("19"));
    azzert.that(Attribute.Extractor.attributes(new __()).get(2).value, is("21"));
  }

  @Test public void methodAttributeName() {
    class __ {
      @Attribute public int intAttribute() {
        return 3;
      }
    }
    azzert.that(Attribute.Extractor.attributes(new __()).get(0).name, is("intAttribute"));
  }

  @Test public void nonNull() {
    class __ {
      // empty
    }
    assert Attribute.Extractor.attributes(new __()) != null;
  }
}
