package let;

import java.util.*;

import org.jetbrains.annotations.*;

public class it<T> {
  private static Stack<Object> stack = new Stack<>();
  public T it;

  public it(final T it) {
    this.it = it;
  }

  public static <T> it<T> be(final T it) {
    final let.it<T> $ = new it<>(it);
    stack.push($);
    return $;
  }

  public it<T> is(String string) {
    return this;
  }
}
