package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Nov 13, 2016 */
public enum generalize {
  ;
  public static String code(@NotNull final String code) {
    finish();
    return generalizeIdentifiers(code);
  }

  static final Int serial = new Int();

  private static void finish() {
    serial.inner = 0;
  }

  /** @param type of placeHolder, can be X,N,M,B,A,L
   * @return */
  @NotNull static String renderIdentifier(final String type) {
    return "$" + type + serial.inner++;
  }

  public static String generalizeIdentifiers(@NotNull final String s) {
    @NotNull final IDocument d = new Document(ASTutils.wrapCode(s));
    final ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource(d.get().toCharArray());
    @NotNull final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    final AST ast = cu.getAST();
    @Nullable final ASTNode n = ASTutils.extractASTNode(s, cu);
    final ASTRewrite r = ASTRewrite.create(ast);
    @NotNull final Map<String, String> renaming = new HashMap<>();
    // noinspection SameReturnValue,SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final StringLiteral $) {
        final StringLiteral ¢ = ast.newStringLiteral();
        ¢.setLiteralValue(renderIdentifier("L"));
        r.replace($, ¢, null);
        return super.visit($);
      }

      @Override public boolean visit(final ImportDeclaration ¢) {
        ___.unused(¢);
        return false;
      }

      @Override public boolean visit(final PackageDeclaration ¢) {
        ___.unused(¢);
        return false;
      }

      @Override public boolean visit(@NotNull final SimpleName $) {
        final String name = $.getFullyQualifiedName();
        if (!renaming.containsKey(name))
          renaming.put(name, renderIdentifier("N"));
        r.replace($, ast.newSimpleName(renaming.get(name)), null);
        return super.visit($);
      }
    });
    try {
      r.rewriteAST(d, null).apply(d);
    } catch (@NotNull MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      monitor.logEvaluationError(¢);
    }
    return ASTutils.extractCode(s, d);
  }

  public static void main(final String[] args) {
    System.out.println("enter whatever:");
    System.out.println(generalize.code(system.read()));
  }
}
