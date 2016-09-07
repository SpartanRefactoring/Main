package il.org.spartan.spartanizer.engine;

public class NotImplementedException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NotImplementedException(final String message) {
    super(message);
  }
}