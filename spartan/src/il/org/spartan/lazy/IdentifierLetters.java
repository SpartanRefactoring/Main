/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

import il.org.spartan.*;

@SuppressWarnings("javadoc") public class IdentifierLetters {
  public static void main(final String[] args) {
    for (int i = Character.MIN_CODE_POINT; i <= Character.MAX_CODE_POINT; ++i)
      if (Character.isJavaIdentifierStart(i) && !Character.isAlphabetic(i))
        System.out.println("\t" + hex(i) + "\t" + (char) i + " ");
  }

  static String hex(final int i) {
    return String.format("U+%4X", Integer.valueOf(i), box.it(i));
  }
}
