package il.org.spartan.strings;

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
      assert "A1".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testDollar() {
      assert "$".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testDollars() {
      assert "$.$".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testDot() {
      assert !".".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testEmpty() {
      assert !"".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testJavaLangObject() {
      assert "java.lang.Object".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testJavaLangObjectSpaceBefore() {
      assert !" java.lang.Object".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testJavaLangObjectSpaces() {
      assert "java .  lang .    Object".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testJavaLangObjectSpacesTabs() {
      assert "java \t.  lang\t . \t   Object".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testJavaLangObjectSpacesTabsNewLines() {
      assert "java \t.  \nlang\t \r\n. \t   \nObject".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testJavaLangObjectSpacesTabsNewLinesSlashes() {
      assert !"java \t. \\ \nlang\t \r\n. \t   \nObject".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testJavaLangObjectSpacesTabsNewLinesSpaceAtEnd() {
      assert !"java \t.  \nlang\t \r\n. \t   \nObject ".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testNumber() {
      assert !"1".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testOneLetterLower() {
      assert "a".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testOneLetterLowerUnderscore() {
      assert "a_".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testOneLetterUpper() {
      assert "A".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testOneLetterUpperDollar() {
      assert "A$".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testOneLetterUpperDollarNumber() {
      assert "A$3".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testOneLetterUpperDollarNumberDot() {
      assert !"A$3.".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testSpace() {
      assert !" ".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testUnderscore1() {
      assert "_1".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testUnderscoredDollars() {
      assert "_._.__.$$._$._1".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testUnderscores() {
      assert "_._".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testUnderscrore() {
      assert "_".matches(fulllyQualifiedIdentifier());
    }
  }

  @SuppressWarnings("static-method") public static class TestIdentifier {
    @Test public void testDollar() {
      assert "$".matches(identifier());
    }

    @Test public void testDot() {
      assert !".".matches(identifier());
    }

    @Test public void testEmpty() {
      assert !"".matches(identifier());
    }

    @Test public void testNumber() {
      assert !"1".matches(identifier());
    }

    @Test public void testOneLetterLower() {
      assert "a".matches(identifier());
    }

    @Test public void testOneLetterLowerDot() {
      assert !"a.".matches(identifier());
    }

    @Test public void testOneLetterLowerUnderscore() {
      assert "a_".matches(identifier());
    }

    @Test public void testOneLetterUpper() {
      assert "A".matches(identifier());
    }

    @Test public void testOneLetterUpperDollar() {
      assert "A$".matches(identifier());
    }

    @Test public void testOneLetterUpperDollarNumber() {
      assert "A$3".matches(identifier());
    }

    @Test public void testOneLetterUpperDollarNumberDot() {
      assert !"A$3.".matches(identifier());
    }

    @Test public void testSpace() {
      assert !" ".matches(identifier());
    }

    @Test public void testTwoDollar() {
      assert "$$".matches(identifier());
    }

    @Test public void testTwoUnderscrores() {
      assert "__".matches(identifier());
    }

    @Test public void testUnderscrore() {
      assert "_".matches(identifier());
    }

    @Test public void testUnderscrore1() {
      assert "_".matches(identifier());
    }
  }
}
