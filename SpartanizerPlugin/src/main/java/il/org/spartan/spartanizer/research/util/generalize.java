package il.org.spartan.spartanizer.research.util;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since Nov 13, 2016 */
public class generalize {
  public static String code(final String code) {
    finish();
    return generalizeIdentifiers(code);
  }

  static Int serial = new Int();

  private static void finish() {
    serial.inner = 0;
  }

  /** @param type of placeHolder, can be X,N,M,B,A,L
   * @return */
  static String renderIdentifier(final String type) {
    return "$" + type + serial.inner++;
  }

  public static String generalizeIdentifiers(final String s) {
    final Map<String, String> renaming = new HashMap<>();
    final Document document = new Document(ASTutils.wrapCode(s));
    final ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource(document.get().toCharArray());
    final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    final AST ast = cu.getAST();
    final ASTNode n = ASTutils.extractASTNode(s, cu);
    final ASTRewrite r = ASTRewrite.create(ast);
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final StringLiteral node) {
        final StringLiteral lit = ast.newStringLiteral();
        lit.setLiteralValue(renderIdentifier("L"));
        r.replace(node, lit, null);
        return super.visit(node);
      }

      @Override public boolean visit(@SuppressWarnings("unused") final ImportDeclaration __) {
        return false;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final PackageDeclaration __) {
        return false;
      }

      @Override public boolean visit(final SimpleName node) {
        final String name = ((Name) node).getFullyQualifiedName();
        if (!renaming.containsKey(name))
          renaming.put(name, renderIdentifier("N"));
        r.replace(node, ast.newSimpleName(renaming.get(name)), null);
        return super.visit(node);
      }
      // @Override public boolean visit(final QualifiedName node) {
      // final String name = ((Name) node).getFullyQualifiedName();
      // if (!renaming.containsKey(name))
      // renaming.put(name, renderIdentifier("N"));
      // r.replace(node, ast.newSimpleName(renaming.get(name)), null);
      // return super.visit(node);
      // }
    });
    try {
      r.rewriteAST(document, null).apply(document);
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      ¢.printStackTrace();
    }
    return ASTutils.extractCode(s, document);
  }
}
