package il.org.spartan;

import java.util.*;

import fluent.ly.*;

/** @author Yossi Gil
 * @since Apr 8, 2012 */
public abstract class AbstractStringProperties {
  final Renderer renderer;

  public AbstractStringProperties() {
    this(Renderer.CSV);
  }
  public AbstractStringProperties(final Renderer renderer) {
    this.renderer = renderer;
  }
  @Override public AbstractStringProperties clone() {
    try {
      return (AbstractStringProperties) super.clone();
    } catch (final CloneNotSupportedException ¢) {
      ¢.printStackTrace();
      return null;
    }
  }
  public abstract String get(String key);
  /** A total inspector
   * @return the header of the CSV line */
  public String header() {
    return renderer.allTop() + makeLine(keys()) + renderer.headerEnd();
  }
  public abstract Iterable<String> keys();
  /** A total inspector
   * @return the content of the CSV line as per all recorded values. */
  public final String line() {
    return makeLine(values());
  }
  public abstract AbstractStringProperties put(String key, String value);
  public abstract int size();
  public abstract Iterable<String> values();
  protected String makeLine(final Iterable<String> ¢) {
    return renderer.makeLine(¢);
  }

  public static class ListProperties extends AbstractStringProperties {
    private final List<String> keys = new ArrayList<>();
    private final List<String> values = new ArrayList<>();

    @Override public String get(final String key) {
      final int $ = keys.lastIndexOf(key);
      return $ < 0 ? null : values.get($);
    }
    @Override public Iterable<String> keys() {
      return keys;
    }
    @Override public ListProperties put(final String key, final String value) {
      keys.add(key);
      values.add(value);
      return this;
    }
    /* (non-Javadoc)
     *
     * @see il.org.spartan.csv.AbstractStringProperties#size() */
    @Override public int size() {
      return keys.size();
    }
    @Override public Iterable<String> values() {
      return values;
    }
  }

  public enum Renderer {
    CSV {
      /** Wraps values in a CSV line. Occurrences of this character in field
       * content are escaped by typing it twice. */
      static final String QUOTE = '"' + "";
      static final String DELIMETER = ",";

      @Override public String headerEnd() {
        return "";
      }
      @Override public String makeField(final String ¢) {
        return ¢ == null ? "" : !¢.contains(QUOTE) && !¢.contains(delimiter()) ? ¢ : QUOTE + ¢.replaceAll(QUOTE, QUOTE + QUOTE) + QUOTE;
      }
      @Override String allBottom() {
        return "";
      }
      @Override String allTop() {
        return "";
      }
      @Override String delimiter() {
        return DELIMETER;
      }
      @Override String lineBegin() {
        return "";
      }
      @Override String lineEnd() {
        return "";
      }
    },
    MATRIX {
      static final String DELIMETER = " ";
      static final int WIDTH = 3;

      @Override String allBottom() {
        return "";
      }
      @Override String allTop() {
        return "";
      }
      @Override String delimiter() {
        return DELIMETER;
      }
      @Override String headerEnd() {
        return "";
      }
      @Override String lineBegin() {
        return "";
      }
      @Override String lineEnd() {
        return "";
      }
      @Override String makeField(final String ¢) {
        return String.format("%" + WIDTH + "s", ¢);
      }
    },
    LaTeX() {
      @Override String allBottom() {
        return "\\bottomrule\n";
      }
      @Override String allTop() {
        return "\\toprule\n";
      }
      @Override String delimiter() {
        return " &\t\t";
      }
      @Override String headerEnd() {
        return "\n\\midrule";
      }
      @Override String lineBegin() {
        return "";
      }
      @Override String lineEnd() {
        return "\\\\";
      }
      @Override String makeField(final String ¢) {
        return ¢ == null ? "" : !¢.contains(delimiter()) ? ¢ : ¢.replaceAll(delimiter(), "\\" + delimiter());
      }
    };
    public String makeLine(final Iterable<String> ¢) {
      return lineBegin() + separate(¢) + lineEnd();
    }
    public String separate(final Iterable<String> ¢) {
      return separate.these(¢).by(delimiter());
    }
    abstract String allBottom();
    abstract String allTop();
    abstract String delimiter();
    abstract String headerEnd();
    abstract String lineBegin();
    abstract String lineEnd();
    abstract String makeField(String s);
  }
}
