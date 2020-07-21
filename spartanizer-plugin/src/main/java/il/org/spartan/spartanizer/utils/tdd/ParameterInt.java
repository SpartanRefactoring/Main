package il.org.spartan.spartanizer.utils.tdd;

/** approximation of int with default value and initializations check
 * @author kobybs
 * @since 27-11-2016 */
public final class ParameterInt {
  private int value;
  private boolean hasValue;
  private boolean hasDefault;

  public ParameterInt() {}
  public ParameterInt(final int defaultValue) {
    value = defaultValue;
    hasDefault = true;
  }
  public boolean hasValue() {
    return hasValue;
  }
  public int intValue() {
    if (!hasValue && !hasDefault)
      throw new IllegalArgumentException();
    return value;
  }
  public void set(final int v) {
    if (hasValue)
      throw new IllegalArgumentException();
    value = v;
    hasValue = true;
  }
  public boolean hasDefault() {
    return hasDefault;
  }
}
