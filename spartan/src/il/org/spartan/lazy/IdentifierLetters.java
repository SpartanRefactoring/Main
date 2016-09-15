/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

import org.eclipse.jdt.annotation.*;

import il.org.spartan.*;

/** Print out all Unicode characters that are pseudo-letters, and demonstrate
 * class {@link CSVLineWriter}
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-09-11 */
public class IdentifierLetters {
  public static void main(final String[] args) {
    final CSVLineWriter w = new CSVLineWriter();
    for (int i = Character.MIN_CODE_POINT; i <= Character.MAX_CODE_POINT; ++i)
      if (Character.isJavaIdentifierStart(i) && !Character.isAlphabetic(i)) {
        w.put("Character", "'" + (char) i + "'").put("Unicode", hex(i)).put("Decimal", i);
        w.put("Ideographic", Character.isIdentifierIgnorable(i));
        w.put("Mirrored", Character.isMirrored(i));
        w.put("Title", Character.isTitleCase(i));
        w.put("CharCount", Character.charCount(i));
        w.put("hashCode", Character.hashCode((char) i));
        w.nl();
      }
  }

  static @NonNull String hex(final int i) {
    if (i > 255)
      return String.format("U+%04X", Integer.valueOf(i));
    else
      return String.format("U+%02X", Integer.valueOf(i));
  }
}
