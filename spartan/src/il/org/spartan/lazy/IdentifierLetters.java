/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

/** TODO(2016) Javadoc: document class <code></code>
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
public class IdentifierLetters {
  public static void main(String[] args) {
    for (int i = Character.MIN_CODE_POINT; i <= Character.MAX_CODE_POINT; i++)
      if (Character.isJavaIdentifierStart(i) && !Character.isAlphabetic(i))
        System.out.println("\t" + hex(i) + "\t" + (char) i + " ");
  }
  static String hex(int n) {
    return String.format("0x%4s", Integer.toHexString(n)).replace(' ', '0');
  }
}
