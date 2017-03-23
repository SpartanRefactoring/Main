package il.org.spartan.tables;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.*;

/** TODO: Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016-12-25 */
@FunctionalInterface
public interface TableRenderer {
  enum builtin implements TableRenderer {
    TXT, TEX {
      @Override @NotNull public String afterHeader() {
        return "\\midrule" + NL;
      }

      @Override @NotNull public String afterTable() {
        return "\\bottomrule" + NL;
      }

      // @formatter:off
      @Override
      @NotNull public String arraySeparator() { return ", "; }
      @Override
      @NotNull public String beforeFooter() { return "\\midrule" + NL; }
    // @formatter:on
      @Override @NotNull public String beforeTable() {
        return "\\toprule" + NL;
      }

      @Override @NotNull public String null¢() {
        return "$\\#$";
      }

      @Override @NotNull public String recordEnd() {
        return " \\\\" + NL;
      }

      @Override @NotNull public String recordSeparator() {
        return "\t&\t";
      }

      @Override @NotNull public String render(@NotNull final Statistic ¢) {
        switch (¢) {
          case Σ:
            return "\\hfill$\\Sum$";
          case σ:
            return "\\hfill$\\sigma$";
          case max:
          case min:
            return "\\hfill$\\" + super.render(¢) + "$";
          default:
            return "\\hfill" + super.render(¢);
        }
      }
    },
    TEX2 {
      @Override @NotNull public String afterHeader() {
        return "\\hline" + NL;
      }

      @Override @NotNull public String afterTable() {
        return "\\hline" + NL;
      }

    // @formatter:off
    @Override
    @NotNull public String arraySeparator() { return ", "; }
    @Override
    @NotNull public String beforeFooter() { return "\\hline" + NL; }
  // @formatter:on
      @Override @NotNull public String beforeTable() {
        return "\\hline" + NL;
      }

      @Override @NotNull public String footerEnd() {
        return "\\\\" + NL;
      }

      @Override @NotNull public String recordSeparator() {
        return "\t&\t";
      }
    },
    CSV {
    // @formatter:off
    @Override public String footerEnd() { return NL; }
    @Override
    @NotNull public String recordSeparator() { return ","; }
    // @formatter:on
    },
    MARKDOWN {
      @Override @NotNull public String afterHeader() {
        @NotNull String $ = "| ";
        for (int ¢ = 0; ¢ < lastSize; ++¢)
          $ += "--- |";
        return $ + NL;
      }

      @Override public String afterTable() {
        return NL;
      }

      // @formatter:off
      @Override public String beforeTable() { return NL; }
      @Override
      @NotNull public String recordBegin() { return "|" ; }
      @Override
      @NotNull public String recordEnd() { return " |" + NL; }
      @Override
      @NotNull public String recordSeparator() { return " | "; }
    // @formatter:on
    };
    static int lastSize;

    @Override public void setHeaderCount(final int size) {
      builtin.lastSize = size;
    }
  }

  String NL = System.getProperty("line.separator");

  @NotNull default String cellReal(@NotNull final Double ¢) {
    return ¢.longValue() != ¢.doubleValue() ? ¢ + "" : cellInt(Long.valueOf(¢.longValue()));
  }

  static String empty() {
    return "";
  }

  static String tab() {
    return "\t";
  }

  @NotNull default String afterFooter() {
    return empty();
  }

  @NotNull default String afterHeader() {
    return empty();
  }

  default String afterTable() {
    return empty();
  }

  // @formatter:off
  @NotNull
  default String arraySeparator() { return "; "; }
  @NotNull
  default String beforeFooter() { return empty(); }
  @NotNull
  default String beforeHeader() { return empty(); }
  default String beforeTable() { return empty(); }
  default String cellArray(final Object[] ¢) {
    return separate.these(¢).by(arraySeparator());
  }
  @NotNull
  default String cellInt(final Long ¢) { return ¢ + ""; }
  @NotNull
  default String extension() {
    return toString().toLowerCase();
  }
  @NotNull
  default String footerBegin() { return recordBegin();}
  default String footerEnd() { return recordEnd();}
  @NotNull
  default String footerSeparator() { return recordSeparator(); }
  @NotNull
  default String headerLineBegin() { return recordBegin(); }
  default String headerLineEnd() { return recordEnd(); }
  @NotNull
  default String headerSeparator() { return recordSeparator(); }
  @NotNull
  default String null¢() { return "Nº"; }
  // @formatter:on
  @NotNull default String recordBegin() {
    return empty();
  }

  default String recordEnd() {
    return NL;
  }

  @NotNull default String recordSeparator() {
    return tab();
  }

  @NotNull default String render(final Statistic ¢) {
    return ¢ + "";
  }

  @NotNull default String renderRow(@NotNull final Collection<Object> values) {
    @NotNull final StringBuilder $ = new StringBuilder(recordBegin());
    @NotNull final Separator s = new Separator(recordSeparator());
    values.forEach(λ -> $.append(s)
        .append(λ instanceof Object[] ? cellArray((Object[]) λ)
            : λ instanceof Integer ? cellInt(Long.valueOf(((Integer) λ).intValue())) : λ instanceof Long ? cellInt((Long) λ) //
                : λ instanceof Double ? cellReal((Double) λ) : λ));
    return $ + recordEnd();
  }

  void setHeaderCount(int size);

  default String stringField(final String value) {
    return value;
  }
}
