package il.org.spartan.spartanizer.ast.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import fluent.ly.note;
import il.org.spartan.spartanizer.ast.navigate.wizard;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum makeAST {
  /** Converts file, string or marker to compilation unit. */
  COMPILATION_UNIT(ASTParser.K_COMPILATION_UNIT) {
    @Override public CompilationUnit from(final File ¢) {
      return from(string(¢));
    }
    @Override public CompilationUnit from(final IFile ¢) {
      return (CompilationUnit) make.COMPILATION_UNIT.parser(¢).createAST(wizard.nullProgressMonitor);
    }
    @Override public CompilationUnit from(final IMarker m, final IProgressMonitor pm) {
      return (CompilationUnit) make.COMPILATION_UNIT.parser(m).createAST(pm);
    }
    @Override public CompilationUnit from(final String ¢) {
      final char[] charArray = ¢.toCharArray();
      final ASTParser $ = wizard.parser(ASTParser.K_COMPILATION_UNIT);
      $.setSource(charArray);
      final ASTNode ret = $.createAST(wizard.nullProgressMonitor);
      return (CompilationUnit) ret;
    }
  },
  /** Converts file, string or marker to expression. */
  EXPRESSION(ASTParser.K_EXPRESSION) {
    @Override public Expression from(final File ¢) {
      return from(string(¢));
    }
    @Override public Expression from(final IFile ¢) {
      return (Expression) make.EXPRESSION.parser(¢).createAST(wizard.nullProgressMonitor);
    }
    @Override public Expression from(final IMarker m, final IProgressMonitor pm) {
      return (Expression) make.EXPRESSION.parser(m).createAST(pm);
    }
    @Override public Expression from(final String ¢) {
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
  public static ICompilationUnit iCompilationUnit(final IMarker ¢) {
    return iCompilationUnit((IFile) ¢.getResource());
  }
  /** Convert file contents into a {@link String}
   * @param f JD
   * @return entire contents of this file, as one string */
  public static String string(final File f) {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
      for (String $ = "", ¢ = r.readLine();; $ += ¢ + System.lineSeparator(), ¢ = r.readLine())
        if (¢ == null)
          return $;
    } catch (final IOException ¢) {
      return note.io(¢, f + "");
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
  public StringBuilder builder(final File f) {
    if (f == null)
      return new StringBuilder();
    try (Scanner $ = new Scanner(f)) {
      return new StringBuilder($.useDelimiter("\\Z").next());
    } catch (final Exception ¢) {
      note.bug(this, ¢);
      return new StringBuilder();
    }
  }
  /** Parses a given {@link Document}.
   * @param d JD
   * @return {@link ASTNode} obtained by parsing */
  public ASTNode from(final IDocument ¢) {
    return from(¢.get());
  }
  /** File -> ASTNode converter
   * @param function File
   * @return ASTNode */
  public ASTNode from(final File ¢) {
    return from(string(¢));
  }
  /** @param function IFile
   * @return ASTNode */
  public ASTNode from(final IFile ¢) {
    return make.from(this).parser(¢).createAST(wizard.nullProgressMonitor);
  }
  /** @param function IFile
   * @return ASTNode */
  public ASTNode fromWithBinding(final IFile ¢) {
    return make.from(this).parserWithBinding(¢).createAST(wizard.nullProgressMonitor);
  }
  /** IMarker, SubProgressMonitor -> ASTNode converter
   * @param m Marker
   * @param pm ProgressMonitor
   * @return ASTNode */
  public ASTNode from(final IMarker m, final IProgressMonitor pm) {
    return make.from(this).parser(m).createAST(pm);
  }
  /** String -> ASTNode converter
   * @param s String
   * @return ASTNode */
  public ASTNode from(final String ¢) {
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
  public ASTParser makeParser(final String text) {
    return makeParser(text.toCharArray());
  }
  /** Creates a binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  public ASTParser makeParserWithBinding(final String text) {
    final ASTParser $ = makeParser(text.toCharArray());
    $.setResolveBindings(true);
    return $;
  }
  public ASTParser makeParserWithBinding(final File ¢) {
    return makeParserWithBinding(string(¢));
  }
}
