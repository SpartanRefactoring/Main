package il.org.spartan.refactoring.utils;

import org.eclipse.jdt.annotation.*;

public class Maybe<@Nullable T> {
  private T inner;

  public static <@Nullable T> Maybe<T> no() {
    return new Maybe<>();
  }
  public static <@Nullable T> Maybe<T> yes(T t) {
    return new Maybe<>(t);
  }
  private Maybe() {
    inner = null;
  }
  public Maybe(T inner) {
    this.inner = inner;
  }
  public boolean present() {
    return inner != null;
  }
  public boolean missing() {
    return inner == null;
  }
  public @Nullable T get() {
    return inner;
  }
  public Maybe<@Nullable T> clear() {
    inner = null;
    return this;
  }
  public Maybe<@Nullable T> set(T inner) {
    this.inner = inner;
    return this;
  }
}
