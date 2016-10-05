package il.org.spartan.spartanizer.cmdline;

import java.util.*;
import il.org.spartan.azzert.*;
import org.junit.*;

import il.org.spartan.spartanizer.leonidas.*;

/** @author Yossi Gil
 * @since 2016 */
public class Essence {
  public static void main(final String[] args) {
    for (final Scanner ¢ = new Scanner(System.in);;) {
      System.out.print("Enter some code: ");
      System.out.println(Essence.of(¢.nextLine()));
    }
  }

  @Test public void chocolate0() {
    il.org.spartan.azzert.that(stringRemove("abc"), il.org.spartan.azzert.iz("abc"));
  }

  @Test public void chocolate1() {
    il.org.spartan.azzert.that(stringRemove("abc"), il.org.spartan.azzert.instanceOf(String.class));
  }

  @Test public void chocolate2() {
    il.org.spartan.azzert.that(stringRemove(stringRemove("hello")), il.org.spartan.azzert.instanceOf(String.class));
  }

  @Test public void idempotent() {
    for (String caze : new String[] { "This", "This 'is'", "This \"is" })
      il.org.spartan.azzert.that(stringRemove(stringRemove(caze)), il.org.spartan.azzert.iz(caze));
  }

  @Ignore("Work later with Itai") @Test public void vanilla() {
    il.org.spartan.azzert.that(stringRemove("\"Who\" is on \"First\""), il.org.spartan.azzert.iz(" is on "));
  }

  private static String stringRemove(String ¢) {
    return ¢;
  }

  public static String of(final String codeFragment) {
    return codeFragment//
        .replaceAll("\\r\\n", "\\n") // DOS Junk
        .replaceAll("\\n\\r", "\\n") // Mac Junk
        .replaceAll("\\s+", " ") // Runs of spaces
        .replaceAll("\\s$", "") // Spaces at EOLN
        .replaceAll("^\\s+", "") // Spaces at BOLN
        .replaceAll("^\\s*$", "") // Erase spaces of empty lines
        .replaceAll("^\\s*\\n", "") // Erase all empty lines
        .replaceAll("\\n\\n", "\\n") // Consecutive new lines
        .replaceAll("//.*$", "") // Line comments
        // All comments?
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "")
        // Space between two non-letters:
        .replaceAll("([^\\p{L}]) ([^\\p{L}])", "$1$2")//
        // Letter, then non-letter
        .replaceAll("([\\p{L}]) ([^\\p{L}])", "$1$2")//
        // Non letter, then letter
        .replaceAll("([^\\p{L}]) ([\\p{L}])", "$1$2")//
    ;
  }
}
