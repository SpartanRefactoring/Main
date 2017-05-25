package let;

import java.util.*;

public class it<T> {
  private static Stack<Object> stack = new Stack<>();
  public T it;

  public it(final T it) {
    this.it = it;
  }
  public static <T> it<T> be(final T ¢) {
    final let.it<T> $ = new it<>(¢);
    stack.push($);
    return $;
  }
  public it<T> is(final T ¢) {
    this.it = ¢;
    return this;
  }
}
