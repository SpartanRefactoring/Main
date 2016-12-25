package il.org.spartan.spartanizer.cmdline;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-25 */
public interface Renderer {
  static String empty() {
    return "";
  }
  static String nl() {
    return "\n";
  }
  static String tab() {
    return "\t";
  }

  default String extension() {
    return toString().toLowerCase();
  }

// @formatter:off
  default String integerField(String value) { return value; } 
  default String realField(String value) { return value; } 
  default String stringField(String value) { return value; } 
  default String beforeTable() { return empty(); } 
  default String afterTable() { return empty(); } 
  default String beforeHeader() { return empty(); } 
  default String afterHeader() { return empty(); } 
  default String beforeFooter() { return empty(); } 
  default String afterFooter() { return empty(); } 
  default String tableBegin() { return empty(); }
  default String tableEnd() { return empty(); }
  default String recordBegin() { return empty(); }
  default String recordEnd() {return nl(); }
  default String recordSeparator()  { return tab(); }
  default String headerLineBegin() { return recordBegin(); }
  default String headerLineEnd() { return footerEnd(); }
  default String headerLineSepator() { return recordSeparator(); } 
  default String footerBegin() { return recordBegin();}
  default String footerEnd() { return footerEnd();}
  default String footerSeparator() { return recordSeparator(); }
  // @formatter:on

  enum builtin implements Renderer {
    TXT, TEX {
    // @formatter:off
      @Override public String footerEnd() { return "\\\\\n"; } 
      @Override public String recordSeparator() { return "\t&\t"; }
      @Override public String beforeTable() {return "\\toprule"; }
      @Override public String afterTable() {return "\\bottomrule"; }
      @Override public String afterHeader() { return "\\midrule"; } 
      @Override public String beforeFooter() { return "\\midrule"; }
    // @formatter:on
    },
    TEX2 {
  // @formatter:off
    @Override public String footerEnd() { return "\\\\\n"; } 
    @Override public String recordSeparator() { return "\t&\t"; }
    @Override public String beforeTable() {return "\\hline\n"; }
    @Override public String afterTable() {return "\\hline\n"; }
    @Override public String afterHeader() { return "\\hline\n"; } 
    @Override public String beforeFooter() { return "\\hline\n"; }
  // @formatter:on
    },
  CSV {
    // @formatter:off
    @Override public String footerEnd() { return "\n"; } 
    @Override public String recordSeparator() { return ","; }
    // @formatter:on
    }
  }
}
