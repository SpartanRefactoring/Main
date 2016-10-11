package il.org.spartan.spartanizer.research;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class TestComments {
  public static void main(final String[] args) throws MalformedTreeException, BadLocationException, CoreException {
    final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("testAddComments");
    final IJavaProject javaProject = JavaCore.create(project);
    final IPackageFragment package1 = javaProject.getPackageFragments()[0];
    // get first compilation unit
    final ICompilationUnit unit = package1.getCompilationUnits()[0];
    // parse compilation unit
    final CompilationUnit astRoot = parse(unit);
    // create a ASTRewrite
    final AST ast = astRoot.getAST();
    final ASTRewrite rewriter = ASTRewrite.create(ast);
    // for getting insertion position
    final TypeDeclaration typeDecl = (TypeDeclaration) astRoot.types().get(0);
    final MethodDeclaration methodDecl = typeDecl.getMethods()[0];
    final Block block = methodDecl.getBody();
    final ListRewrite listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
    final Statement placeHolder = (Statement) rewriter.createStringPlaceholder("//mycomment", ASTNode.EMPTY_STATEMENT);
    listRewrite.insertFirst(placeHolder, null);
    final TextEdit edits = rewriter.rewriteAST();
    // apply the text edits to the compilation unit
    final Document document = new Document(unit.getSource());
    edits.apply(document);
    // this is the code for adding statements
    unit.getBuffer().setContents(document.get());
    System.out.println("done");
  }

  /** @param unit
   * @return */
  private static CompilationUnit parse(final ICompilationUnit u) {
    final ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setSource(u);
    parser.setResolveBindings(true); // we need bindings later on
    return (CompilationUnit) parser.createAST(null);
  }
}
