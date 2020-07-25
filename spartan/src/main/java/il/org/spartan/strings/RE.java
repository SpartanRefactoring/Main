package il.org.spartan.strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import il.org.spartan.utils.Separate;

public class RE {
  public static String all(final String ¢) {
    return beginLine() + ¢ + endLine();
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
    final Matcher $ = Pattern.compile(regularExpression).matcher(text);
    return !$.find() ? null : $.group();
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
    return "(?:[a-zA-Z__$])[a-zA-Z__$0-9]*+";
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
    return parenthesis(Separate.by(λ -> parenthesis(λ), alternatives, "|"));
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
      assert "a__".matches(fulllyQualifiedIdentifier());
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
      assert "__1".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testUnderscoredDollars() {
      assert "__.__.____.$$.__$.__1".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testUnderscores() {
      assert "__.__".matches(fulllyQualifiedIdentifier());
    }

    @Test public void testUnderscrore() {
      assert "__".matches(fulllyQualifiedIdentifier());
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
      assert "a__".matches(identifier());
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
      assert "____".matches(identifier());
    }

    @Test public void testUnderscrore() {
      assert "__".matches(identifier());
    }

    @Test public void testUnderscrore1() {
      assert "__".matches(identifier());
    }
  }
}
