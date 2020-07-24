package let;

import java.util.Stack;

public class it<T> {
  private static Stack<Object> stack = new Stack<>();

  @SuppressWarnings("unchecked") public static <T> T get() {
    return (T) stack.peek();
  }
  @SuppressWarnings("unchecked") public static <T> T eat() {
    return (T) stack.pop();
  }

  public T it;

  public it(final T it) {
    this.it = it;
  }
  public static <T> it<T> be(final T $) {
    stack.push($);
    return new it<>($);
  }
  public it<T> is(final T ¢) {
    this.it = ¢;
    return this;
  }
}
