package il.org.spartan.bench;

/** An interface for a metric returning a <code><b>double</b></code> value.
 *
 * @author Yossi Gil
 * @since 02/05/2011 */
public class Metric {
  private String name;

  public Metric() {
    this(null);
  }

  public Metric(final String __name) {
    name = __name;
  }

  public String name() {
    return name;
  }

  public Metric name(final String __name) {
    name = __name;
    return this;
  }

  public abstract static class Double extends Metric {
    private Double() {
    }

    private Double(final String name) {
      super(name);
    }

    public abstract double __();
  }

  public abstract static class Int extends Metric {
    public Int(final String name) {
      super(name);
    }

    public abstract int __();
  }

  public abstract static class Long extends Metric {
    /** Instantiate {@link Long} . */
    public Long() {
    }

    /** Instantiate {@link Long} .
     *
     * @param name */
    public Long(final String name) {
      super(name);
    }

    public abstract long __();

    @Override public Long name(final String name) {
      super.name(name);
      return this;
    }
  }
}
