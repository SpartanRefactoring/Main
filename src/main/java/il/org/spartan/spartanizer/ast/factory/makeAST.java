package il.org.spartan.spartanizer.ast.factory;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-16 */
public enum makeAST {
  /** Converts file, string or marker to compilation unit. */
  COMPILATION_UNIT(ASTParser.K_COMPILATION_UNIT) {
    @Override @NotNull public CompilationUnit from(@NotNull final File ¢) {
      return from(string(¢));
    }

    @Override @NotNull public CompilationUnit from(final IFile ¢) {
      return (CompilationUnit) make.COMPILATION_UNIT.parser(¢).createAST(wizard.nullProgressMonitor);
    }

    @Override @NotNull public CompilationUnit from(@NotNull final IMarker m, final IProgressMonitor pm) {
      return (CompilationUnit) make.COMPILATION_UNIT.parser(m).createAST(pm);
    }

    @Override @NotNull public CompilationUnit from(@NotNull final String ¢) {
      @NotNull final char[] charArray = ¢.toCharArray();
      final ASTParser $ = wizard.parser(ASTParser.K_COMPILATION_UNIT);
      $.setSource(charArray);
      final ASTNode createAST = $.createAST(wizard.nullProgressMonitor);
      return (CompilationUnit) createAST;
    }
  },
  /** Converts file, string or marker to expression. */
  EXPRESSION(ASTParser.K_EXPRESSION) {
    @Override @NotNull public Expression from(@NotNull final File ¢) {
      return from(string(¢));
    }

    @Override @NotNull public Expression from(final IFile ¢) {
      return (Expression) make.EXPRESSION.parser(¢).createAST(wizard.nullProgressMonitor);
    }

    @Override @NotNull public Expression from(@NotNull final IMarker m, final IProgressMonitor pm) {
      return (Expression) make.EXPRESSION.parser(m).createAST(pm);
    }

    @Override @NotNull public Expression from(@NotNull final String ¢) {
      return (Expression) makeParser(¢).createAST(wizard.nullProgressMonitor);
    }
  },
  /** Constant used in order to get the source as a sequence of sideEffects. */
  STATEMENTS(ASTParser.K_STATEMENTS), //
  /** Constant used in order to get the source as a sequence of class body
   * declarations. */
  CLASS_BODY_DECLARATIONS(ASTParser.K_CLASS_BODY_DECLARATIONS);
  static final IProgressMonitor npm = new NullProgressMonitor();

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
    try (@NotNull BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
      for (@NotNull String $ = "", ¢ = r.readLine();; $ += ¢ + System.lineSeparator(), ¢ = r.readLine())
        if (¢ == null)
          return $;
    } catch (@NotNull final IOException ¢) {
      monitor.infoIOException(¢, f + "");
      return null;
    }
  }

  final int kind;

  makeAST(final int kind) {
    this.kind = kind;
  }

  /** Creates a {@link StringBuilder} object out of a file object.
   * @param f JD
   * @return {@link StringBuilder} whose content is the same as the contents of
   *         the parameter. */
  @NotNull public StringBuilder builder(@NotNull final File f) {
    try (@NotNull Scanner $ = new Scanner(f)) {
      return new StringBuilder($.useDelimiter("\\Z").next());
    } catch (@NotNull final Exception ¢) {
      monitor.logEvaluationError(this, ¢);
      return new StringBuilder();
    }
  }

  /** Parses a given {@link Document}.
   * @param d JD
   * @return {@link ASTNode} obtained by parsing */
  public ASTNode from(@NotNull final IDocument ¢) {
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
    return make.from(this).parser(¢).createAST(wizard.nullProgressMonitor);
  }

  /** IMarker, SubProgressMonitor -> ASTNode converter
   * @param m Marker
   * @param pm ProgressMonitor
   * @return ASTNode */
  public ASTNode from(@NotNull final IMarker m, final IProgressMonitor pm) {
    return make.from(this).parser(m).createAST(pm);
  }

  /** String -> ASTNode converter
   * @param s String
   * @return ASTNode */
  public ASTNode from(@NotNull final String ¢) {
    return makeParser(¢).createAST(wizard.nullProgressMonitor);
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
