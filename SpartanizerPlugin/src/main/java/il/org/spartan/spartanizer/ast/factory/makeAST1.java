package il.org.spartan.spartanizer.ast.factory;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum makeAST1 {
  /** Converts file, string or marker to compilation unit. */
  COMPILATION_UNIT(ASTParser.K_COMPILATION_UNIT) {
    @NotNull
    @Override public CompilationUnit from(@NotNull final File ¢) {
      return from(string(¢));
    }

    @NotNull
    @Override public CompilationUnit from(final IFile ¢) {
      return (CompilationUnit) make1.COMPILATION_UNIT.parser(¢).createAST(null);
    }

    @NotNull
    @Override public CompilationUnit from(final IMarker m, final IProgressMonitor pm) {
      return (CompilationUnit) make1.COMPILATION_UNIT.parser(m).createAST(pm);
    }

    @NotNull
    @Override public CompilationUnit from(@NotNull final String ¢) {
      return (CompilationUnit) makeParser(¢).createAST(null);
    }
  },
  /** Converts file, string or marker to expression. */
  EXPRESSION(ASTParser.K_EXPRESSION) {
    @NotNull
    @Override public Expression from(@NotNull final File ¢) {
      return from(string(¢));
    }

    @NotNull
    @Override public Expression from(final IFile ¢) {
      return (Expression) make1.EXPRESSION.parser(¢).createAST(null);
    }

    @NotNull
    @Override public Expression from(final IMarker m, final IProgressMonitor pm) {
      return (Expression) make1.EXPRESSION.parser(m).createAST(pm);
    }

    @NotNull
    @Override public Expression from(@NotNull final String ¢) {
      return (Expression) makeParser(¢).createAST(null);
    }
  },
  /** Constant used in order to get the source as a sequence of sideEffects. */
  STATEMENTS(ASTParser.K_STATEMENTS), //
  /** Constant used in order to get the source as a sequence of class body
   * declarations. */
  CLASS_BODY_DECLARATIONS(ASTParser.K_CLASS_BODY_DECLARATIONS)//
  ;
  /** IFile -> ICompilationUnit converter
   * @param function File
   * @return ICompilationUnit */
  public static ICompilationUnit iCompilationUnit(final IFile ¢) {
    return JavaCore.createCompilationUnitFrom(¢);
  }

  /** IMarker -> ICompilationUnit converter
   * @param marker IMarker
   * @return CompilationUnit */
  public static ICompilationUnit iCompilationUnit(@NotNull final IMarker ¢) {
    return iCompilationUnit((IFile) ¢.getResource());
  }

  /** Convert file contents into a {@link String}
   * @param f JD
   * @return entire contents of this file, as one string */
  public static String string(@NotNull final File f) {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
      for (String $ = "", ¢ = r.readLine();; $ += ¢ + System.lineSeparator(), ¢ = r.readLine())
        if (¢ == null)
          return $;
    } catch (@NotNull final IOException ¢) {
      monitor.infoIOException(¢, f + "");
      return null;
    }
  }

  final int kind;

  makeAST1(final int kind) {
    this.kind = kind;
  }

  /** Creates a {@link StringBuilder} object out of a file object.
   * @param f JD
   * @return {@link StringBuilder} whose content is the same as the contents of
   *         the parameter. */
  @NotNull
  public StringBuilder builder(@NotNull final File f) {
    try (Scanner $ = new Scanner(f)) {
      return new StringBuilder($.useDelimiter("\\Z").next());
    } catch (@NotNull final Exception ¢) {
      monitor.logEvaluationError(this, ¢);
      return new StringBuilder();
    }
  }

  /** Parses a given {@link Document}.
   * @param d JD
   * @return {@link ASTNode} obtained by parsing */
  public ASTNode from(@NotNull final Document ¢) {
    return from(¢.get());
  }

  /** File -> ASTNode converter
   * @param function File
   * @return ASTNode */
  public ASTNode from(@NotNull final File ¢) {
    return from(string(¢));
  }

  /** @param function IFile
   * @return ASTNode */
  public ASTNode from(final IFile ¢) {
    return make1.from(this).parser(¢).createAST(null);
  }

  /** IMarker, SubProgressMonitor -> ASTNode converter
   * @param m Marker
   * @param pm ProgressMonitor
   * @return ASTNode */
  public ASTNode from(final IMarker m, final IProgressMonitor pm) {
    return make1.from(this).parser(m).createAST(pm);
  }

  /** String -> ASTNode converter
   * @param s String
   * @return ASTNode */
  public ASTNode from(@NotNull final String ¢) {
    return makeParser(¢).createAST(null);
  }

  /** Creates a no-binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  public ASTParser makeParser(final char[] text) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(text);
    return $;
  }

  /** Creates a no-binding parser for a given compilation unit
   * @param u what to parse
   * @return a newly created parser for the parameter */
  public ASTParser makeParser(final ICompilationUnit ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢);
    return $;
  }

  /** Creates a no-binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  public ASTParser makeParser(@NotNull final String text) {
    return makeParser(text.toCharArray());
  }

  /** Creates a binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  public ASTParser makeParserWithBinding(@NotNull final String text) {
    final ASTParser $ = makeParser(text.toCharArray());
    $.setResolveBindings(true);
    return $;
  }

  public ASTParser makeParserWithBinding(@NotNull final File ¢) {
    return makeParserWithBinding(string(¢));
  }
}
