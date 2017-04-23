package il.org.spartan.utils;

/** Boolean or reducer
 * @author oran1248
 * @since 2017-04-20 */
public class BooleanOrReducer extends Reduce<Boolean> {
  @Override @SuppressWarnings("boxing") public Boolean reduce() {
    return false;
  }

  @Override @SuppressWarnings("boxing") public Boolean reduce(final Boolean r1, final Boolean r2) {
    return Boolean.TRUE.equals(r1) || Boolean.TRUE.equals(r2);
  }
}
