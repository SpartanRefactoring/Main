package il.org.spartan.refactoring.utils;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

/**
 * An empty <code><b>enum</b></code> for fluent programming. The name should say
 * it all: The name, followed by a dot, followed by a method name, should read
 * like a sentence phrase.
 *
 * @author Yossi Gil
 * @since 2015-07-16
 */
public enum ast {
  /**
   * Converts file, string or marker to compilation unit.
   */
  COMPILIATION_UNIT(ASTParser.K_COMPILATION_UNIT) {
    @Override public CompilationUnit from(final File f) {
      return from(string(f));
    }
    @Override public CompilationUnit from(final IFile f) {
      return (CompilationUnit) Make.COMPILIATION_UNIT.parser(f).createAST(null);
    }
    @Override public CompilationUnit from(final IMarker m, final SubProgressMonitor pm) {
      return (CompilationUnit) Make.COMPILIATION_UNIT.parser(m).createAST(pm);
    }
    @Override public CompilationUnit from(final String s) {
      return (CompilationUnit) makeParser(s).createAST(null);
    }
  },
  /**
   * Converts file, string or marker to expression.
   */
  EXPRESSION(ASTParser.K_EXPRESSION) {
    @Override public Expression from(final File f) {
      return from(string(f));
    }
    @Override public Expression from(final IFile f) {
      return (Expression) Make.EXPRESSION.parser(f).createAST(null);
    }
    @Override public Expression from(final IMarker m, final SubProgressMonitor pm) {
      return (Expression) Make.EXPRESSION.parser(m).createAST(pm);
    }
    @Override public Expression from(final String s) {
      return (Expression) makeParser(s).createAST(null);
    }
  },
  /**
   * Constant used in order to get the source as a sequence of statements.
   */
  STATEMENTS(ASTParser.K_STATEMENTS), //
  /**
   * Constant used in order to get the source as a sequence of class body
   * declarations.
   */
  CLASS_BODY_DECLARATIONS(ASTParser.K_CLASS_BODY_DECLARATIONS);
  /**
   * @param n The node from which to return statement.
   * @return null if it is not possible to extract the return statement.
   */
  public static ReturnStatement asReturn(final ASTNode n) {
    if (n == null)
      return null;
    switch (n.getNodeType()) {
      case ASTNode.BLOCK:
        return asReturn((Block) n);
      case ASTNode.RETURN_STATEMENT:
        return (ReturnStatement) n;
      default:
        return null;
    }
  }
  private static ReturnStatement asReturn(final Block b) {
    return b.statements().size() != 1 ? null : asReturn((Statement) b.statements().get(0));
  }
  /**
   * Converts a boolean into a bit value
   *
   * @param $ JD
   * @return 1 if the parameter is <code><b>true</b></code>, 0 if it is
   *         <code><b>false</b></code>
   */
  public static int bit(final boolean $) {
    return $ ? 1 : 0;
  }
  /**
   * IFile -> ICompilationUnit converter
   *
   * @param f File
   * @return ICompilationUnit
   */
  public static ICompilationUnit iCompilationUnit(final IFile f) {
    return JavaCore.createCompilationUnitFrom(f);
  }
  /**
   * IMarker -> ICompilationUnit converter
   *
   * @param m IMarker
   * @return CompilationUnit
   */
  public static ICompilationUnit iCompilationUnit(final IMarker m) {
    return iCompilationUnit((IFile) m.getResource());
  }
  /**
   * Convert file contents into a {@link String}
   *
   * @param f JD
   * @return the entire contents of this file, as one string
   */
  public static String string(final File f) {
    try (final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
      final StringBuilder $ = new StringBuilder();
      for (String line = r.readLine(); line != null; line = r.readLine())
        $.append(line).append(System.lineSeparator());
      return $.toString();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
  /**
   * Creates a {@link StringBuilder} object out of a file object.
   *
   * @param f JD
   * @return {@link StringBuilder} whose content is the same as the contents of
   *         the parameter.
   */
  public static StringBuilder stringBuilder(final File f) {
    try (final Scanner $ = new Scanner(f)) {
      return new StringBuilder($.useDelimiter("\\Z").next());
    } catch (final Exception e) {
      return new StringBuilder("");
    }
  }

  final int kind;

  private ast(final int kind) {
    this.kind = kind;
  }
  /**
   * Parses a given {@link Document}.
   *
   * @param d JD
   * @return the {@link ASTNode} obtained by parsing
   */
  public final ASTNode from(final Document d) {
    return from(d.get());
  }
  /**
   * File -> ASTNode converter
   *
   * @param f File
   * @return ASTNode
   */
  public ASTNode from(final File f) {
    return from(string(f));
  }
  /**
   * @param f IFile
   * @return ASTNode
   */
  public ASTNode from(final IFile f) {
    return Make.of(this).parser(f).createAST(null);
  }
  /**
   * IMarker, SubProgressMonitor -> ASTNode converter
   *
   * @param m Marker
   * @param pm ProgressMonitor
   * @return ASTNode
   */
  public ASTNode from(final IMarker m, final SubProgressMonitor pm) {
    return Make.of(this).parser(m).createAST(pm);
  }
  /**
   * String -> ASTNode converter
   *
   * @param s String
   * @return ASTNode
   */
  public ASTNode from(final String s) {
    return makeParser(s).createAST(null);
  }
  private ASTParser makeParser() {
    final ASTParser $ = ASTParser.newParser(AST.JLS8);
    $.setKind(kind);
    $.setResolveBindings(false);
    return $;
  }
  /**
   * Creates a no-binding parser for a given text
   *
   * @param text what to parse
   * @return a newly created parser for the parameter
   */
  public ASTParser makeParser(final char[] text) {
    final ASTParser $ = makeParser();
    $.setSource(text);
    return $;
  }
  /**
   * Creates a no-binding parser for a given compilation unit
   *
   * @param u what to parse
   * @return a newly created parser for the parameter
   */
  public ASTParser makeParser(final ICompilationUnit u) {
    final ASTParser $ = makeParser();
    $.setSource(u);
    return $;
  }
  /**
   * Creates a no-binding parser for a given text
   *
   * @param text what to parse
   * @return a newly created parser for the parameter
   */
  public ASTParser makeParser(final String text) {
    return makeParser(text.toCharArray());
  }
  /**
   * Downcast <code>List<?></code> into <code>List<Expression></code>
   *
   * @param es JD
   * @return the parameter, properly downcasted
   *
   */
  @SuppressWarnings("unchecked") public static List<Expression> expressions(final List<?> es) {
    return (List<Expression>) es;
  }
  /**
   * Downcast <code>List<?></code> into
   * <code>List<VariableDeclarationFragment></code>
   *
   * @param fs JD
   * @return the parameter, properly downcasted
   *
   */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationFragment> fragments(final List<?> fs) {
    return (List<VariableDeclarationFragment>) fs;
  }
  /**
   * Downcast <code>List<?></code> into <code>List<Statement></code>
   *
   * @param ss JD
   * @return the parameter, properly downcasted
   *
   */
  @SuppressWarnings("unchecked") public static List<Statement> statements(final List<?> ss) {
    return (List<Statement>) ss;
  }
}
