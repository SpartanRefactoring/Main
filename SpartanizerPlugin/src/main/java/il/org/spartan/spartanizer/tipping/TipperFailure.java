package il.org.spartan.spartanizer.tipping;

public abstract class TipperFailure extends Exception {
  private static final long serialVersionUID = -0x6C899A62287B5409L;

  public abstract String what();

  public static class TipNotImplementedException extends TipperFailure {
    private static final long serialVersionUID = -0x294DA35DB2AF2592L;

    @Override public String what() {
      return "NotImplemented";
    }
  }
}
