package il.org.spartan;

/** @author Ori Marcovitch
 * @since 2016 */
  @SuppressWarnings("static-method") 
public class MethodDeclarationMockclass {
  private int field;

  void f(final int a) {
    field += a;
  }

  public void fooSet(final int input) {
    final int temp = 3 + input;
    this.field = input * temp;
  }

  public String fooGet() {
    return "";
  }
}
