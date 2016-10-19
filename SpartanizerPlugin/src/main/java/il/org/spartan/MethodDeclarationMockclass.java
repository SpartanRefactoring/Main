package il.org.spartan;

/** @author Ori Marcovitch
 * @since 2016 */
public class MethodDeclarationMockclass {
  private int field;

  void f(final int a) {
    field += a;
  }

  public void fooSet(final int input) {
    field = input * (input + 3);
  }

  public String fooGet() {
    return field + "";
  }
}
