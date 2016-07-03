package il.org.spartan;

import org.eclipse.jdt.annotation.*;

/**
 
 @author Yossi Gil <Yossi.Gil@GMail.COM>
 
 @param <T> JD
 
 @since @{year}-@{month}-@{day}
 */
public class maybe<@Nullable T> {
  /**
   
 @param <T> JD
   
 @return TODO document return type
   */
  public static <@Nullable T> maybe<T> no() {
    return new maybe<>();
  }

  /**
   
 @param <T> JD
   
 @param t JD
   
 @return TODO document return type
   */
  public static <@Nullable T> maybe<T> yes(final T t) {
    return new maybe<>(t);
  }
  /**
   * Instantiates this class.
   *
   
 @param inner
   */
  public maybe(final @Nullable T inner) {
    this.inner = inner;
  }
  private maybe() {
    inner = null;
  }
  /**
   
 @return TODO document return type
   */
  public maybe<@Nullable T> clear() {
    inner = null;
    return this;
  }
  /**
   
 @return TODO document return type
   */
  public @Nullable T get() {
    return inner;
  }
  /**
   
 @return TODO document return type
   */
  public boolean missing() {
    return inner == null;
  }
  /**
   
 @return TODO document return type
   */
  public boolean present() {
    return inner != null;
  }
  /**
   
 @param inner TODO document this parameter
   
 @return TODO document return type
   */
  public maybe<@Nullable T> set(final T inner) {
    this.inner = inner;
    return this;
  }
  private @Nullable T inner;
}
