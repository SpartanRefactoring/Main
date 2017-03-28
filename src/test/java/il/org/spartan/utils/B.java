package il.org.spartan.utils;
import java.util.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-27 */
public class B {
  static Map<B, Set<B>> better = new HashMap<>(); 

  @Override public int hashCode() {
    return 31 * (((left == null) ? 3 : left.hashCode()) + 31) + ((right == null) ? 13 : right.hashCode());
  }
  public boolean eq(B b1, B b2) {
    return b1 == null && b2 == null || b1 != null && b2 != null && eq(b1.left, b2.left) && eq(b1.right, b2.right);
  }

  @Override public boolean equals(Object ¢) {
    return ¢ == this || (getClass() == ¢.getClass() && eq(this, (B) ¢));
  }

  public B(B left, B right) {
    this.left = left;
    this.right = right;
  }

  @Override public String toString() {
    return String.format("(%s,%s)", s(left), s(right));
  }

  private static String s(B ¢) {
    return ¢ == null ? "." : ¢ + "";
  }

  B left, right;

  static List<B> enumerate(int n) {
    List<B> $ = new ArrayList<>();
    if (n <= 0) {
      $.add(null);
      return $;
    }
    for (int i = 0; i < n; ++i)
      for (B left : enumerate(i))
        for (B right : enumerate(n - i - 1))
          $.add(new B(left, right));
    return $;
  }

  public static void main(String[] args) {
    System.out.println(enumerate(0).size());
    System.out.println(enumerate(1).size());
    System.out.println(enumerate(2).size());
    System.out.println(enumerate(3).size());
    System.out.println(enumerate(4).size());
    System.out.println(enumerate(0));
    System.out.println(enumerate(1));
    System.out.println(enumerate(2));
    System.out.println(enumerate(3));
    System.out.println(enumerate(4));
  }
}
