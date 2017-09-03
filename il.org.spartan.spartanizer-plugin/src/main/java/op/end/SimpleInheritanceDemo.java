package op.end;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-08-29 */
public enum SimpleInheritanceDemo {
  ;
  public static void main(String[] args) {
    new HelloWorld().go();
    new GoodbyeWorld().go();
  }

  static class HelloWorld extends Go<Protocol.Set, HelloWorld> {
    {
      withListener(new Events.Listener() {
        @Override public void begin() {
          System.out.println("Hello, World!");
        }
      });
    }
  }

  static class GoodbyeWorld extends HelloWorld {
    {
      withListener(new Events.Listener() {
        @Override public void begin() {
          System.out.println("Goodbye, World.");
        }
      });
    }
  }
}
