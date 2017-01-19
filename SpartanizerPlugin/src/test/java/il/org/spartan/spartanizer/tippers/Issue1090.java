package il.org.spartan.spartanizer.tippers;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.meta.*;

/** Unit tests for {@link
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-17 */
@Ignore
@RunWith(Parameterized.class)
public class Issue1090 extends MetaFixture {
  static class Fixture {
    byte byteField;
    char charField;
    double doubleField;
    float floatField;
    int intField;
    long longField;
    short shortField;
    MetaTestCase case1 = new MetaTestCase() {
      /** [[SuppressWarningsSpartan]] */
      @Override protected void startingWith() {
        intField = 0;
        charField = 0;
      }

      @Override protected void trimmingStopsAt() {
        intField = charField = 0;
      }
    };

    byte getByteField() {
      return byteField;
    }

    char getCharField() {
      return charField;
    }

    int getIntField() {
      return intField;
    }

    short getShortField() {
      return shortField;
    }

    void go() {
      byteField = getByteField();
      // byteField = 2* charField;
      // byteField = 2* doubleField;
      // byteField = 2* floatField;
      // byteField = 2* intField;
      // byteField = 2* longField;
      // byteField = 2* shortField;
      // charField = 2* byteField;
      charField = getCharField();
      // charField = 2* doubleField;
      // charField = 2* floatField;
      // charField = 2* intField;
      // charField = 2* longField;
      // charField = 2* shortField;
      doubleField = 2 * getByteField();
      doubleField = 2 * charField;
      doubleField = 2 * doubleField + doubleField;
      doubleField = 2 * floatField;
      doubleField = 2 * intField;
      doubleField = 2 * longField;
      doubleField = 2 * getShortField();
      floatField = 2 * getByteField();
      floatField = 2 * charField;
      // floatField = 2* doubleField;
      floatField = 2 * floatField + 1;
      floatField = 2 * intField;
      floatField = 2 * longField;
      floatField = 2 * getShortField();
      intField = 2 * getByteField();
      intField = 2 * charField;
      // intField = 2* doubleField;
      // intField = 2* floatField;
      intField = getIntField();
      // intField = 2* longField;
      intField = 2 * getShortField();
      longField = 2 * getByteField();
      longField = 2 * charField;
      // longField = 2* doubleField;
      // longField = 2* floatField;
      longField = 2 * intField;
      // longField = 2* longField;
      longField = 2 * getShortField();
      setShortField(setByteField(getByteField()));
      // shortField = 2* charField;
      // shortField = 2* doubleField;
      // shortField = 2* floatField;
      // shortField = 2* intField;
      // shortField = 2* longField;
      shortField = getShortField();
    }

    byte setByteField(final byte byteField) {
      this.byteField = byteField;
      return byteField;
    }

    short setShortField(final short shortField) {
      this.shortField = shortField;
      return shortField;
    }
  }
}
