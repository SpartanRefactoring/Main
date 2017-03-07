package il.org.spartan.spartanizer.cmdline;

import java.util.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2016 */
public enum Essence {
  ;
  public static void main(final String[] args) {
    for (final Scanner ¢ = new Scanner(System.in);;) {
      System.out.print("Enter some code: ");
      System.out.println(Essence.of(¢.nextLine()));
    }
  }

  public static String of(final String codeFragment) {
    return codeFragment//
        .replaceAll("\\r\\n", "\n") // DOS Junk
        .replaceAll("\\n\\r", "\n") // Mac Junk
        .replaceAll("\\s+", " ") // Runs of spaces
        .replaceAll("\\s$", "") // Spaces at EOLN
        .replaceAll("^\\s+", "") // Spaces at BOLN
        .replaceAll("^\\s*$", "") // Erase spaces of empty lines
        .replaceAll("^\\s*\\n", "") // Erase all empty lines
        .replaceAll("\\n\\n", "\n") // Consecutive new lines
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

  public static String stringRemove(final String $) {
    return $
        // Unquoted double quote by two consecutive double quotes
        .replaceAll("([^\"])\"", "$1\"\"")
        // Non greedy search and replacement of ""text"" by nothing
        .replaceAll("\"\".*?\"\"", "") //
        // Undo doubling of double quotes
        .replaceAll("\"\"", "\"") ///
    ;
  }
}
