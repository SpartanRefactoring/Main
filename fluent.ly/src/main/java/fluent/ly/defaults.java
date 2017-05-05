package fluent.ly;

import org.jetbrains.annotations.*;

/** A bunch of <code><b>static</b></code> functions to manage the frequent
 * conditional of replacing a <code><b>null</b></code> value with some default.
 * Writing
 *
 * <pre>
 * final String guess = Defaults.to(answer,&quot;A&quot;)
 * </pre>
 *
 * will assign the {@link String} <code>"A"</code> to variable
 * <code>guess</code> in the case that <code>answer</code> is
 * <code><b>null</b></code>. If however, <code>answer</code> is not
 * <code><b>null</b></code> then <code>guess</code>'s value will be that of
 * <code>answer</code>.
 * @author Yossi Gil, 2008/06/20 */
public enum defaults {
  ;
  /** Return a default value for an {@link Integer} type.
   * @param v a possibly <code><b>null</b></code> value
   * @param defaultValue a value to be used in case <code>v</code> is
   *        <code><b>null</b></code>
   * @return <code>v</code> if it is not <code><b>null</b></code>, otherwise
   *         <code>defaultValue</code> */
  public static int to(@Nullable final Integer v, final int defaultValue) {
    return v != null ? v.intValue() : defaultValue;
  }
  /** Return a default value for an {@link Integer} type.
   * @param v a possibly <code><b>null</b></code> value
   * @param defaultValue a value to be used in case <code>v</code> is
   *        <code><b>null</b></code>
   * @return <code>v</code> if it is not <code><b>null</b></code>, otherwise
   *         <code>defaultValue</code> */
  public static int to(@Nullable final Integer v, final Integer defaultValue) {
    return (v != null ? v : defaultValue).intValue();
  }
  /** Return a default value for an arbitrary type.
   * @param <T> type of parameter to receive a default value
   * @param v a possibly <code><b>null</b></code> value
   * @param defaultValue a value to be used in case <code>v</code> is
   *        <code><b>null</b></code>
   * @return <code>v</code> if it is not <code><b>null</b></code>, otherwise
   *         <code>defaultValue</code> */
  @Nullable public static <T> T to(@Nullable final T v, final T defaultValue) {
    return v != null ? v : defaultValue;
  }
}
