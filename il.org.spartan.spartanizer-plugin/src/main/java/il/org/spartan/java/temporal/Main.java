package il.org.spartan.java.temporal;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-01 */
public class Main {
  static class WriteToFile extends Temporal {
    interface Before extends Operation {/**/}

    interface After extends Operation {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After;
    }
    @Override public void body() {
      System.out.println("write to file");
    }
  }

  static class OpenFile extends Temporal implements WriteToFile.Before {
    interface Before extends Operation {/**/}

    interface After extends Operation {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After;
    }
    @Override public void body() {
      System.out.println("open file");
    }
  }

  static class LogFileOpened extends Temporal implements OpenFile.After/* , WriteToFile.Before */ {
    interface Before extends Operation {/**/}

    interface After extends Operation {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After;
    }
    @Override public void body() {
      System.out.println("logged file opening");
    }
  }

  public static void main(String[] args) {
    Temporal w = new WriteToFile(), o = new OpenFile();
    o.register(new LogFileOpened());
    w.register(o);
    w.go();
    System.out.println("\tShould be the same as...");
    w = new WriteToFile();
    w.register(new OpenFile());
    w.register(new LogFileOpened());
    w.go();
    System.out.println("\tShould be the same as...");
    w = new WriteToFile();
    w.register(new LogFileOpened());
    w.register(new OpenFile());
    w.go();
  }
}
