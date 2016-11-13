package il.org.spartan.spartanizer.research.util;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.formatter.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

/** @author Ori Marcovitch
 * @since Nov 13, 2016 */
public class format {
  public static String code(final String code) {
    CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(null);
    TextEdit textEdit = codeFormatter.format(CodeFormatter.K_UNKNOWN, code, 0, code.length(), 0, null);
    IDocument doc = new Document(code);
    try {
      if (textEdit != null)
        textEdit.apply(doc);
    } catch (MalformedTreeException e) {
      e.printStackTrace();
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
    return doc.get();
  }
}
