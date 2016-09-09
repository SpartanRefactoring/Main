package il.org.spartan.strings;

import static org.junit.Assert.*;

import java.util.regex.*;

import org.junit.*;

import il.org.spartan.utils.*;
import il.org.spartan.utils.Separate.*;

public class RE {
  public static String all(final String s) {
    return beginLine() + s + endLine();
  }

  public static String anyNumberOf(final String regularExpression) {
    return parenthesis(regularExpression) + "*";
  }

  public static String anyNumberOfReluctant(final String regularExpression) {
    return parenthesis(regularExpression) + "*?";
  }

  public static String beginLine() {
    return "^";
  }

  public static String endLine() {
    return "$";
  }

  public static String find(final String regularExpression, final String text) {
    final Pattern p = Pattern.compile(regularExpression);
    final Matcher m = p.matcher(text);
    return m.find() ? m.group() : null;
  }

  public static boolean found(final String regularExpression, final String text) {
    return !text.equals(text.replaceAll(regularExpression, ""));
  }

  public static String fulllyQualifiedIdentifier() {
    return identifier() + anyNumberOf(whites() + "[.]" + whites() + identifier());
  }

  public static String group(final String regularExpression) {
    return "(" + regularExpression + ")";
  }

  public static String identifier() {
    return "(?:[a-zA-Z_$])[a-zA-Z_$0-9]*+";
  }

  public static String ignoreCase() {
    return "(?i)";
  }

  public static String lineMode() {
    return "(?m)";
  }

  public static String newLine() {
    return or("\r\n", "\n");
  }

  public static String optional(final String regularExpression) {
    return parenthesis(regularExpression) + "?";
  }

  public static String or(final String... alternatives) {
    return parenthesis(Separate.by((F<String>) s -> parenthesis(s), alternatives, "|"));
  }

  public static String padded(final String regularExpression) {
    return whites() + regularExpression + whites();
  }

  public static String parenthesis(final String regularExpression) {
    return "(?:" + regularExpression + ")";
  }

  public static String spaces() {
    return "[ \t*]*+";
  }

  public static String whites() {
    return "[ \\s]*";
  }

  @SuppressWarnings("static-method") public static class TestFullyQualifiedIdentifier {
    @Test public void testA() {
      assertTrue("A1".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testDollar() {
      assertTrue("$".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testDollars() {
      assertTrue("$.$".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testDot() {
      assertFalse(".".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testEmpty() {
      assertFalse("".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testJavaLangObject() {
      assertTrue("java.lang.Object".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testJavaLangObjectSpaceBefore() {
      assertFalse(" java.lang.Object".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testJavaLangObjectSpaces() {
      assertTrue("java .  lang .    Object".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testJavaLangObjectSpacesTabs() {
      assertTrue("java \t.  lang\t . \t   Object".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testJavaLangObjectSpacesTabsNewLines() {
      assertTrue("java \t.  \nlang\t \r\n. \t   \nObject".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testJavaLangObjectSpacesTabsNewLinesSlashes() {
      assertFalse("java \t. \\ \nlang\t \r\n. \t   \nObject".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testJavaLangObjectSpacesTabsNewLinesSpaceAtEnd() {
      assertFalse("java \t.  \nlang\t \r\n. \t   \nObject ".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testNumber() {
      assertFalse("1".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testOneLetterLower() {
      assertTrue("a".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testOneLetterLowerUnderscore() {
      assertTrue("a_".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testOneLetterUpper() {
      assertTrue("A".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testOneLetterUpperDollar() {
      assertTrue("A$".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testOneLetterUpperDollarNumber() {
      assertTrue("A$3".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testOneLetterUpperDollarNumberDot() {
      assertFalse("A$3.".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testSpace() {
      assertFalse(" ".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testUnderscore1() {
      assertTrue("_1".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testUnderscoredDollars() {
      assertTrue("_._.__.$$._$._1".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testUnderscores() {
      assertTrue("_._".matches(fulllyQualifiedIdentifier()));
    }

    @Test public void testUnderscrore() {
      assertTrue("_".matches(fulllyQualifiedIdentifier()));
    }
  }

  @SuppressWarnings("static-method") public static class TestIdentifier {
    @Test public void testDollar() {
      assertTrue("$".matches(identifier()));
    }

    @Test public void testDot() {
      assertFalse(".".matches(identifier()));
    }

    @Test public void testEmpty() {
      assertFalse("".matches(identifier()));
    }

    @Test public void testNumber() {
      assertFalse("1".matches(identifier()));
    }

    @Test public void testOneLetterLower() {
      assertTrue("a".matches(identifier()));
    }

    @Test public void testOneLetterLowerDot() {
      assertFalse("a.".matches(identifier()));
    }

    @Test public void testOneLetterLowerUnderscore() {
      assertTrue("a_".matches(identifier()));
    }

    @Test public void testOneLetterUpper() {
      assertTrue("A".matches(identifier()));
    }

    @Test public void testOneLetterUpperDollar() {
      assertTrue("A$".matches(identifier()));
    }

    @Test public void testOneLetterUpperDollarNumber() {
      assertTrue("A$3".matches(identifier()));
    }

    @Test public void testOneLetterUpperDollarNumberDot() {
      assertFalse("A$3.".matches(identifier()));
    }

    @Test public void testSpace() {
      assertFalse(" ".matches(identifier()));
    }

    @Test public void testTwoDollar() {
      assertTrue("$$".matches(identifier()));
    }

    @Test public void testTwoUnderscrores() {
      assertTrue("__".matches(identifier()));
    }

    @Test public void testUnderscrore() {
      assertTrue("_".matches(identifier()));
    }

    @Test public void testUnderscrore1() {
      assertTrue("_".matches(identifier()));
    }
  }
}
