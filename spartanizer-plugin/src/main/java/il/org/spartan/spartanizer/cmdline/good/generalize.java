package il.org.spartan.spartanizer.cmdline.good;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;

import fluent.ly.forget;
import fluent.ly.note;
import fluent.ly.system;
import il.org.spartan.spartanizer.research.ASTutils;
import il.org.spartan.utils.Int;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Nov 13, 2016 */
public enum generalize {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  public static String code(final String code) {
    finish();
    return generalizeIdentifiers(code);
  }

  static final Int serial = new Int();

  private static void finish() {
    serial.inner = 0;
  }
  /** @param __ of placeHolder, can be X,N,M,B,A,L
   * @return */
  static String renderIdentifier(final String type) {
    return "$" + type + serial.inner++;
  }
  public static String generalizeIdentifiers(final String s) {
    final IDocument ret = new Document(ASTutils.wrapCode(s));
    final ASTParser parser = ASTParser.newParser(AST.JLS14);
    parser.setSource(ret.get().toCharArray());
    final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    final AST ast = cu.getAST();
    final ASTNode n = ASTutils.extractASTNode(s, cu);
    final ASTRewrite r = ASTRewrite.create(ast);
    final Map<String, String> renaming = new HashMap<>();
    // noinspection SameReturnValue,SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final StringLiteral $) {
        final StringLiteral ¢ = ast.newStringLiteral();
        ¢.setLiteralValue(renderIdentifier("L"));
        r.replace($, ¢, null);
        return super.visit($);
      }
      @Override public boolean visit(final ImportDeclaration ¢) {
        forget.em(¢);
        return false;
      }
      @Override public boolean visit(final PackageDeclaration ¢) {
        forget.em(¢);
        return false;
      }
      @Override public boolean visit(final SimpleName $) {
        final String name = $.getFullyQualifiedName();
        if (!renaming.containsKey(name))
          renaming.put(name, renderIdentifier("N"));
        r.replace($, ast.newSimpleName(renaming.get(name)), null);
        return super.visit($);
      }
    });
    try {
      r.rewriteAST(ret, null).apply(ret);
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      note.bug(¢);
    }
    return ASTutils.extractCode(s, ret);
  }
  public static void main(final String[] args) {
    System.out.println("enter whatever:");
    System.out.println(generalize.code(system.read()));
  }
}
