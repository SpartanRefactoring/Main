package il.org.spartan.spartanizer.tipping;

public abstract class TipperException extends Exception {
  private static final long serialVersionUID = 1L;

  public TipperException() {
  }

  abstract public String what();

  public static class TipNotImplementedException extends TipperException {
    private static final long serialVersionUID = 1L;

    @Override public String what() {
      return "NotImplemented";
    }
  }
}
