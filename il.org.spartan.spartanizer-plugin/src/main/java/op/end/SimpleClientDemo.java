package op.end;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-08-29 */
public enum SimpleClientDemo {
  // All access is via static methods
  ;
  public static void main(String[] args) {
    simpleClientDemo();
  }

  private static void simpleClientDemo() {
    System.out.print("There should be nothing between the following angular brackets: <");
    new Go<>().go();
    System.out.println(">");
    System.out.print("There should be nothing between the following angular brackets: <");
    new Go<>().withListener(new Listener.Listener.Default.Template() {/**/}).go();
    System.out.println(">!");
    System.out.print("There should be 'Hello World' between the following angular brackets: <");
    go();
    System.out.println(">!");
    System.err.printf(
        "There should be 'null' between the following angular brackets: <%s>!\n" + //
            " but there should be 'Hello, World!' (no \\n) somewhere else from stdout\n", //
        go());
  }
  private static <T> T go() {
    return new Go<>().withListener(new Listener.Listener.Default.Template() {
      /** Reified ops ar as expressive as anything.. */
      @Override public void begin() {
        System.out.print("Hello, World!");
      }
    }).go();
  }
}
