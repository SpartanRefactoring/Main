package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.refactoring.utils.*;

/**
 * An empty <code><b>enum</b></code> for fluent programming. The name should say
 * it all: The name, followed by a dot, followed by a method name, should read
 * like a sentence phrase.
 *
 * @author Yossi Gil
 * @since 2015-07-16
 */
public enum Wrap {
  /** Algorithm for wrapping/unwrapping a method */
  Method("" + //
      "package p;\n" + //
      "public class SpongeBob {\n" + //
      "", "" + //
          "} // END OF PACKAGE\n" + //
          ""), //
  /** Algorithm for wrapping/unwrapping a statement */
  Statement("" + Method.before + //
      "public void squarePants(){\n" + //
      "", "" + //
          "} // END OF METHOD \n" + //
          "" + Method.after + //
          ""), //
  /** Algorithm for wrapping/unwrapping an expression */
  Expression("" + Statement.before + //
      "   if (", //
      "" + //
          ") patrick();\n" + //
          Statement.after + //
          ""), //
  ComplilationUnit("", ""), //
  //
  ;
  /**
   * Finds the most appropriate Wrap for a given code fragment
   *
   * @param codeFragment JD
   * @return the most appropriate Wrap, or null, if the parameter could not be
   *         parsed appropriately.
   */
  public static Wrap find(final String codeFragment) {
    for (final Wrap $ : new Wrap[] { Statement, Expression, Method })
      if ($.contains($.intoCompilationUnit(codeFragment).toString(), codeFragment))
        return $;
    return null;
  }
  static String essence(final String codeFragment) {
    return compressSpaces(removeComments(codeFragment));
  }
  static String removeComments(final String codeFragment) {
    return codeFragment//
        .replaceAll("//.*?\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "");
  }

  private final String before;
  private final String after;

  Wrap(final String before, final String after) {
    this.before = before;
    this.after = after;
  }
  /**
   * Wrap a given code fragment, and then parse it, converting it into a
   * {@link CompilationUnit}.
   *
   * @param codeFragment JD
   * @return a newly created {@link CompilationUnit} representing the parsed AST
   *         of the wrapped parameter.
   */
  public CompilationUnit intoCompilationUnit(final String codeFragment) {
    return (CompilationUnit) ast.COMPILIATION_UNIT.from(on(codeFragment));
  }
  /**
   * Wrap a given code fragment, and converts it into a {@link Document}
   *
   * @param codeFragment JD
   * @return a newly created {@link CompilationUnit} representing the parsed AST
   *         of the wrapped parameter.
   */
  public Document intoDocument(final String codeFragment) {
    return new Document(on(codeFragment));
  }
  /**
   * Remove a wrap from around a phrase
   *
   * @param codeFragment a wrapped program phrase
   * @return the unwrapped phrase
   */
  public final String off(final String codeFragment) {
    return before.isEmpty() || after.isEmpty() ? codeFragment : removeSuffix(removePrefix(codeFragment, before), after);
  }
  /**
   * Place a wrap around a phrase
   *
   * @param codeFragment some program phrase
   * @return the wrapped phrase
   */
  public final String on(final String codeFragment) {
    return before + codeFragment + after;
  }
  private boolean contains(final String wrap, final String inner) {
    return essence(off(wrap)).contains(essence(inner));
  }
}
