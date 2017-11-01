package il.org.spartan.java.temporal;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-01 */
public class Main {
  static class WriteToFile extends Temporal {
    protected String filename = "temp/ora.ry";

    public abstract class Nested extends Temporal {
      public String filename() {
        return filename;
      }
    }

    public abstract class NestedBefore extends Nested {/**/}

    public abstract class NestedAfter extends Nested {/**/}

    public interface Before extends Operation {/**/}

    public interface After extends Operation {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before || o instanceof NestedBefore;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After || o instanceof NestedAfter;
    }
    @Override public void body() {
      System.out.println("write to file " + filename);
    }
  }

  static class OpenFile extends WriteToFile.NestedBefore {
    public OpenFile(WriteToFile main) {
      main.super();
    }

    public abstract class Nested extends Temporal {/**/}

    public abstract class NestedBefore extends Nested {/**/}

    public abstract class NestedAfter extends Nested {/**/}

    public interface Before extends Operation {/**/}

    public interface After extends Operation {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before || o instanceof NestedBefore;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After || o instanceof NestedAfter;
    }
    @Override public void body() {
      System.out.println("open file " + filename());
    }
  }

  static class LogFileOpened extends Temporal implements OpenFile.After {
    public abstract class Nested extends Temporal {/**/}

    public abstract class NestedBefore extends Nested {/**/}

    public abstract class NestedAfter extends Nested {/**/}

    public interface Before extends Operation {/**/}

    public interface After extends Operation {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before || o instanceof NestedBefore;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After || o instanceof NestedAfter;
    }
    @Override public void body() {
      System.out.println("logged file opening");
    }
  }

  public static void main(String[] args) {
    WriteToFile w = new WriteToFile();
    OpenFile o = new OpenFile(w);
    o.register(new LogFileOpened());
    w.register(o);
    w.go();
    System.out.println("\tShould be the same as...");
    w = new WriteToFile();
    w.register(new OpenFile(w));
    w.register(new LogFileOpened());
    w.go();
    System.out.println("\tShould be the same as...");
    w = new WriteToFile();
    w.register(new LogFileOpened());
    w.register(new OpenFile(w));
    w.go();
  }
}
