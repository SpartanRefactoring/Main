package il.org.spartan;

import java.util.*;

import org.jetbrains.annotations.*;

import fluent.ly.separate;

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

  @Override @Nullable public AbstractStringProperties clone() {
    try {
      return (AbstractStringProperties) super.clone();
    } catch (@NotNull final CloneNotSupportedException ¢) {
      ¢.printStackTrace();
      return null;
    }
  }

  @Nullable public abstract String get(String key);

  /** A total inspector
   * @return the header of the CSV line */
  @NotNull public String header() {
    return renderer.allTop() + makeLine(keys()) + renderer.headerEnd();
  }

  @NotNull public abstract Iterable<String> keys();

  /** A total inspector
   * @return the content of the CSV line as per all recorded values. */
  @NotNull public final String line() {
    return makeLine(values());
  }

  @NotNull public abstract AbstractStringProperties put(String key, String value);

  public abstract int size();

  @NotNull public abstract Iterable<String> values();

  @NotNull protected String makeLine(final Iterable<String> ¢) {
    return renderer.makeLine(¢);
  }

  public static class ListProperties extends AbstractStringProperties {
    private final List<String> keys = new ArrayList<>();
    private final List<String> values = new ArrayList<>();

    @Override @SuppressWarnings("null") @Nullable public String get(final String key) {
      final int $ = keys.lastIndexOf(key);
      return $ < 0 ? null : values.get($);
    }

    @Override @NotNull public Iterable<String> keys() {
      return keys;
    }

    @Override @NotNull public ListProperties put(final String key, final String value) {
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

    @Override @NotNull public Iterable<String> values() {
      return values;
    }
  }

  public enum Renderer {
    CSV {
      /** Wraps values in a CSV line. Occurrences of this character in field
       * content are escaped by typing it twice. */
      static final String QUOTE = '"' + "";
      static final String DELIMETER = ",";

      @Override @NotNull public String headerEnd() {
        return "";
      }

      @Override @NotNull public String makeField(@Nullable final String ¢) {
        return ¢ == null ? "" : !¢.contains(QUOTE) && !¢.contains(delimiter()) ? ¢ : QUOTE + ¢.replaceAll(QUOTE, QUOTE + QUOTE) + QUOTE;
      }

      @Override @NotNull String allBottom() {
        return "";
      }

      @Override @NotNull String allTop() {
        return "";
      }

      @Override @NotNull String delimiter() {
        return DELIMETER;
      }

      @Override @NotNull String lineBegin() {
        return "";
      }

      @Override @NotNull String lineEnd() {
        return "";
      }
    },
    MATRIX {
      static final String DELIMETER = " ";
      static final int WIDTH = 3;

      @Override @NotNull String allBottom() {
        return "";
      }

      @Override @NotNull String allTop() {
        return "";
      }

      @Override @NotNull String delimiter() {
        return DELIMETER;
      }

      @Override @NotNull String headerEnd() {
        return "";
      }

      @Override @NotNull String lineBegin() {
        return "";
      }

      @Override @NotNull String lineEnd() {
        return "";
      }

      @Override String makeField(final String ¢) {
        return String.format("%" + WIDTH + "s", ¢);
      }
    },
    LaTeX() {
      @Override @NotNull String allBottom() {
        return "\\bottomrule\n";
      }

      @Override @NotNull String allTop() {
        return "\\toprule\n";
      }

      @Override String delimiter() {
        return " &\t\t";
      }

      @Override @NotNull String headerEnd() {
        return "\n\\midrule";
      }

      @Override @NotNull String lineBegin() {
        return "";
      }

      @Override @NotNull String lineEnd() {
        return "\\\\";
      }

      @Override @NotNull String makeField(@Nullable final String ¢) {
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
