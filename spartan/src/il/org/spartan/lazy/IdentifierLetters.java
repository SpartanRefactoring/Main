/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

@SuppressWarnings("javadoc")
public class IdentifierLetters {
  public static void main(String[] args) {
    for (int i = Character.MIN_CODE_POINT; i <= Character.MAX_CODE_POINT; ++i)
      if (Character.isJavaIdentifierStart(i) && !Character.isAlphabetic(i))
        System.out.println("\t" + hex(i) + "\t" + (char) i + " ");
  }
  static String hex(int i) {
    return String.format("U+%4X", Integer.valueOf(i), i);
  }
}
