package il.org.spartan.spartanizer.utils;

import java.text.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.formatter.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;

/** Fluet API library for formatting things
 * @author Ori Marcovitch
 * @since Nov 13, 2016 */
public enum format {
  ;
  public static String code(final String code) {
    final TextEdit textEdit = ToolFactory.createCodeFormatter(null).format(CodeFormatter.K_UNKNOWN, code, 0, code.length(), 0, null);
    final IDocument $ = new Document(code);
    try {
      if (textEdit != null)
        textEdit.apply($);
    } catch (final BadLocationException | MalformedTreeException ¢) {
      note.bug(¢);
    }
    return $.get();
  }

  private static final NumberFormat numberFormatter = new DecimalFormat("#0.00");

  public static double decimal(final double ¢) {
    return Double.valueOf(numberFormatter.format(¢)).doubleValue();
  }
  public static double perc(final int a, final int b) {
    return decimal(100 * safe.div(a, b));
  }
}
