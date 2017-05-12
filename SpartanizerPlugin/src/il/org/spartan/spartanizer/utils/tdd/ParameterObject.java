package il.org.spartan.spartanizer.utils.tdd;

/** approximation of generic Type with default value and initializations check
 * @author kobybs
 * @since 27-11-2016 */
public final class ParameterObject<T> {
  private T value;
  private boolean hasValue;
  private boolean hasDefault;

  public ParameterObject() {}

  public ParameterObject(final T defaultValue) {
    value = defaultValue;
    hasDefault = true;
  }

  public boolean hasValue() {
    return hasValue;
  }

  public T objectValue() {
    if (!hasValue && !hasDefault)
      throw new IllegalArgumentException();
    return value;
  }

  public void set(final T v) {
    if (hasValue)
      throw new IllegalArgumentException();
    value = v;
    hasValue = true;
  }

  public boolean hasDefault() {
    return hasDefault;
  }
}
