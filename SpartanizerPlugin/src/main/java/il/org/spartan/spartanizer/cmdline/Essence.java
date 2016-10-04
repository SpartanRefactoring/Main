package il.org.spartan.spartanizer.cmdline;

/**
 * @author Yossi Gil 
 * @since 2016 */
public class Essence {
  public static void main(String[] args) {
   // TODO Yossi 
  }

  static String of(final String codeFragment) {
    return codeFragment//
        .replaceAll("\\r\\n","\\n") // DOS Junk
        .replaceAll("\\n\\r","\\n") // Mac Junk
        .replaceAll("\\s+", " ")    // Runs of spaces
        .replaceAll("\\s*$", "")    // Spaces at EOLN
        .replaceAll("^\\s*$", "")   // Erase spaces of empty lines
        .replaceAll("^\\s*\\n", "") // Erase all empty lines
        .replaceAll("\\n\\n","\\n") // Consecutive new lines 
        .replaceAll("//.*?\n", "\n") // Line comments
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "")// All comments?
        .replaceAll("\\([[:letter:]]\\) \\([[:letter:]]\\)", "\\1\\2") // Space between two non-letters
        .replaceAll("\\([^[:letter:]]\\) \\([[:letter:]]\\)","\\1\\2") // Non-letter then letter
        .replaceAll("\\([[:letter:]]\\) \\([^[:letter:]]\\)", "\\1\\2") // Letter then non-letter
        ;
  }
}
