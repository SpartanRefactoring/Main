/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

import org.eclipse.jdt.annotation.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;

/** Print out all Unicode characters that are pseudo-letters, and demonstrate
 * class {@link CSVLineWriter}
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-09-11 */
public class IdentifierLetters {
  public static void main(final String[] args) {
    @NotNull final CSVLineWriter w = new CSVLineWriter();
    for (int ¢ = Character.MIN_CODE_POINT; ¢ <= Character.MAX_CODE_POINT; ++¢)
      if (Character.isJavaIdentifierStart(¢) && !Character.isAlphabetic(¢)) {
        w.put("Character", "'" + (char) ¢ + "'").put("Unicode", hex(¢)).put("Decimal", ¢);
        w.put("Ideographic", Character.isIdentifierIgnorable(¢));
        w.put("Mirrored", Character.isMirrored(¢));
        w.put("Title", Character.isTitleCase(¢));
        w.put("CharCount", Character.charCount(¢));
        w.put("hashCode", Character.hashCode((char) ¢));
        w.nl();
      }
  }

  @NonNull static String hex(final int ¢) {
    return String.format("U+%0" + (¢ > 255 ? "4" : "2") + "X", Integer.valueOf(¢));
  }
}
