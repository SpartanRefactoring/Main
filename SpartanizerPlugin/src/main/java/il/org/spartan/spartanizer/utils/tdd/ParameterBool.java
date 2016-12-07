package il.org.spartan.spartanizer.utils.tdd;

/** approximation of boolean with default value and initializations check
 * @author kobybs
 * @since 27-11-2016 */
public final class ParameterBool {
  private boolean value;
  private boolean hasValue;
  private boolean hasDefault;

  public ParameterBool() {}

  public ParameterBool(final boolean defaultValue) {
    value = defaultValue;
    hasDefault = true;
  }

  public boolean hasValue() {
    return hasValue;
  }

  public boolean boolValue() {
    if (!hasValue && !hasDefault)
      throw new IllegalArgumentException();
    return value;
  }

  public void set(final boolean v) {
    if (hasValue)
      throw new IllegalArgumentException();
    value = v;
    hasValue = true;
  }

  public boolean hasDefault() {
    return hasDefault;
  }
}
