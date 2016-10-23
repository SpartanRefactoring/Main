package il.org.spartan;

import java.util.*;

import org.jetbrains.annotations.*;

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

  @Nullable @Override public AbstractStringProperties clone() {
    try {
      return (AbstractStringProperties) super.clone();
    } catch (@NotNull final CloneNotSupportedException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Nullable public abstract String get(final String key);

  /** A total inspector
   * @return the header of the CSV line */
  @NotNull public String header() {
    return renderer.allTop() + makeLine(keys()) + renderer.headerEnd();
  }

  public abstract Iterable<String> keys();

  /** A total inspector
   * @return the content of the CSV line as per all recorded values. */
  @NotNull public final String line() {
    return makeLine(values());
  }

  @NotNull public abstract AbstractStringProperties put(final String key, final String value);

  public abstract int size();

  public abstract Iterable<String> values();

  @NotNull protected String makeLine(final Iterable<String> ¢) {
    return renderer.makeLine(¢);
  }

  public static class ListProperties extends AbstractStringProperties {
    private final List<String> keys = new ArrayList<>();
    private final List<String> values = new ArrayList<>();

    /* (non-Javadoc)
     *
     * @see il.org.spartan.csv.AbstractStringProperties#get(java.lang.String) */
    @Nullable @Override public String get(final String key) {
      final int $ = keys.lastIndexOf(key);
      return $ < 0 ? null : values.get($);
    }

    /* (non-Javadoc)
     *
     * @see il.org.spartan.csv.AbstractStringProperties#headers() */
    @NotNull @Override public Iterable<String> keys() {
      return keys;
    }

    /* (non-Javadoc)
     *
     * @see il.org.spartan.csv.AbstractStringProperties#put(java.lang.String,
     * java.lang.String) */
    @NotNull @Override public ListProperties put(final String key, final String value) {
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

    /* (non-Javadoc)
     *
     * @see il.org.spartan.csv.AbstractStringProperties#values() */
    @NotNull @Override public Iterable<String> values() {
      return values;
    }
  }

  public enum Renderer {
    CSV {
      /** Wraps values in a CSV line. Occurrences of this character in field
       * content are escaped by typing it twice. */
      public static final String QUOTE = '"' + "";
      public static final String DELIMETER = ",";

      @NotNull @Override public String headerEnd() {
        return "";
      }

      @NotNull @Override public String makeField(@Nullable final String ¢) {
        return ¢ == null ? "" : !¢.contains(QUOTE) && !¢.contains(delimiter()) ? ¢ : QUOTE + ¢.replaceAll(QUOTE, QUOTE + QUOTE) + QUOTE;
      }

      @NotNull @Override String allBottom() {
        return "";
      }

      @NotNull @Override String allTop() {
        return "";
      }

      @NotNull @Override String delimiter() {
        return DELIMETER;
      }

      @NotNull @Override String lineBegin() {
        return "";
      }

      @NotNull @Override String lineEnd() {
        return "";
      }
    },
    MATRIX {
      public static final String DELIMETER = " ";
      public static final int WIDTH = 3;

      @NotNull @Override String allBottom() {
        return "";
      }

      @NotNull @Override String allTop() {
        return "";
      }

      @NotNull @Override String delimiter() {
        return DELIMETER;
      }

      @NotNull @Override String headerEnd() {
        return "";
      }

      @NotNull @Override String lineBegin() {
        return "";
      }

      @NotNull @Override String lineEnd() {
        return "";
      }

      @Override String makeField(final String ¢) {
        return String.format("%" + WIDTH + "s", ¢);
      }
    },
    LaTeX() {
      @NotNull @Override String allBottom() {
        return "\\bottomrule\n";
      }

      @NotNull @Override String allTop() {
        return "\\toprule\n";
      }

      @Override String delimiter() {
        return " &\t\t";
      }

      @NotNull @Override String headerEnd() {
        return "\n\\midrule";
      }

      @NotNull @Override String lineBegin() {
        return "";
      }

      @NotNull @Override String lineEnd() {
        return "\\\\";
      }

      @NotNull @Override String makeField(@Nullable final String ¢) {
        return ¢ == null ? "" : !¢.contains(delimiter()) ? ¢ : ¢.replaceAll(delimiter(), "\\" + delimiter());
      }
    };
    @NotNull public String makeLine(final Iterable<String> ¢) {
      return lineBegin() + separate(¢) + lineEnd();
    }

    @NotNull public String separate(final Iterable<String> ¢) {
      return separate.these(¢).by(delimiter());
    }

    @NotNull abstract String allBottom();

    @NotNull abstract String allTop();

    @NotNull abstract String delimiter();

    @NotNull abstract String headerEnd();

    @NotNull abstract String lineBegin();

    @NotNull abstract String lineEnd();

    abstract String makeField(String s);
  }
}
