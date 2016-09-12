package il.org.spartan.spartanizer.engine;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.spartanizer.ast.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum makeAST {
  /** Converts file, string or marker to compilation unit. */
  COMPILATION_UNIT(ASTParser.K_COMPILATION_UNIT) {
    @Override public CompilationUnit from(final File ¢) {
      return from(string(¢));
    }

    @Override public CompilationUnit from(final IFile ¢) {
      return (CompilationUnit) Make.COMPILATION_UNIT.parser(¢).createAST(null);
    }

    @Override public CompilationUnit from(final IMarker m, final IProgressMonitor pm) {
      return (CompilationUnit) Make.COMPILATION_UNIT.parser(m).createAST(pm);
    }

    @Override public CompilationUnit from(final String s) {
      return (CompilationUnit) makeParser(s).createAST(null);
    }
  },
  /** Converts file, string or marker to expression. */
  EXPRESSION(ASTParser.K_EXPRESSION) {
    @Override public Expression from(final File f) {
      return from(string(f));
    }

    @Override public Expression from(final IFile f) {
      return (Expression) Make.EXPRESSION.parser(f).createAST(null);
    }

    @Override public Expression from(final IMarker m, final IProgressMonitor pm) {
      return (Expression) Make.EXPRESSION.parser(m).createAST(pm);
    }

    @Override public Expression from(final String s) {
      return (Expression) makeParser(s).createAST(null);
    }
  },
  /** Constant used in order to get the source as a sequence of statements. */
  STATEMENTS(ASTParser.K_STATEMENTS), //
  /** Constant used in order to get the source as a sequence of class body
   * declarations. */
  CLASS_BODY_DECLARATIONS(ASTParser.K_CLASS_BODY_DECLARATIONS);
  /** @param n The node from which to return statement.
   * @return null if it is not possible to extract the return statement. */
  public static ReturnStatement asReturn(final ASTNode n) {
    if (n == null)
      return null;
    switch (n.getNodeType()) {
      case ASTNode.BLOCK:
        return asReturn(n);
      case ASTNode.RETURN_STATEMENT:
        return (ReturnStatement) n;
      default:
        return null;
    }
  }

  /** IFile -> ICompilationUnit converter
   * @param f File
   * @return ICompilationUnit */
  public static ICompilationUnit iCompilationUnit(final IFile f) {
    return JavaCore.createCompilationUnitFrom(f);
  }

  /** IMarker -> ICompilationUnit converter
   * @param m IMarker
   * @return CompilationUnit */
  public static ICompilationUnit iCompilationUnit(final IMarker m) {
    return iCompilationUnit((IFile) m.getResource());
  }

  /** Convert file contents into a {@link String}
   * @param f JD
   * @return entire contents of this file, as one string */
  public static String string(final File f) {
    try (final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
      final StringBuilder $ = new StringBuilder();
      for (String line = r.readLine(); line != null; line = r.readLine())
        $.append(line).append(System.lineSeparator());
      return $ + "";
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Creates a {@link StringBuilder} object out of a file object.
   * @param f JD
   * @return {@link StringBuilder} whose content is the same as the contents of
   *         the parameter. */
  public static StringBuilder stringBuilder(final File f) {
    try (final Scanner $ = new Scanner(f)) {
      return new StringBuilder($.useDelimiter("\\Z").next());
    } catch (final Exception e) {
      e.printStackTrace();
      return new StringBuilder("");
    }
  }

  final int kind;

  private makeAST(final int kind) {
    this.kind = kind;
  }

  /** Parses a given {@link Document}.
   * @param d JD
   * @return {@link ASTNode} obtained by parsing */
  public final ASTNode from(final Document ¢) {
    return from(¢.get());
  }

  /** File -> ASTNode converter
   * @param f File
   * @return ASTNode */
  public ASTNode from(final File ¢) {
    return from(string(¢));
  }

  /** @param f IFile
   * @return ASTNode */
  public ASTNode from(final IFile f) {
    return Make.of(this).parser(f).createAST(null);
  }

  /** IMarker, SubProgressMonitor -> ASTNode converter
   * @param m Marker
   * @param pm ProgressMonitor
   * @return ASTNode */
  public ASTNode from(final IMarker m, final IProgressMonitor pm) {
    return Make.of(this).parser(m).createAST(pm);
  }

  /** String -> ASTNode converter
   * @param s String
   * @return ASTNode */
  public ASTNode from(final String s) {
    return makeParser(s).createAST(null);
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
  public ASTParser makeParser(final ICompilationUnit u) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(u);
    return $;
  }

  /** Creates a no-binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  public ASTParser makeParser(final String text) {
    return makeParser(text.toCharArray());
  }
}
