package il.org.spartan.bench;

/** An interface for a metric returning a <code><b>double</b></code> value.
 * @author Yossi Gil
 * @since 02/05/2011 */
public class Metric {
  private String name;

  public Metric() {
    this(null);
  }

  public Metric(final String _name) {
    name = _name;
  }

  public String name() {
    return name;
  }

  public Metric name(final String _name) {
    name = _name;
    return this;
  }

  /* * An interface for a metric returning a <code><b>double</b></code> value.
   *
   * @author Yossi Gil
   *
   * @since 02/05/2011 */
  public static abstract class Double extends Metric {
    private Double() {
      super();
    }

    private Double(final String name) {
      super(name);
    }

    /* @return the metric's value */
    public abstract double _();
  }

  /* * An interface for a metric returning an <code><b>int</b></code> value.
   *
   * @author Yossi Gil
   *
   * @since 02/05/2011 */
  public static abstract class Int extends Metric {
    public Int(final String name) {
      super(name);
    }

    /* @return the metric's value */
    public abstract int _();
  }

  /* * An interface for a metric returning a <code><b>long</b></code> value.
   *
   * @author Yossi Gil
   *
   * @since 02/05/2011 */
  public static abstract class Long extends Metric {
    /** Instantiate {@link Long}. */
    public Long() {
      // TODO Auto-generated constructor stub
    }

    /** Instantiate {@link Long}.
     * @param name */
    public Long(final String name) {
      super(name);
    }

    /* @return the metric's value */
    public abstract long _();

    @Override public Long name(final String name) {
      super.name(name);
      return this;
    }
  }
}
