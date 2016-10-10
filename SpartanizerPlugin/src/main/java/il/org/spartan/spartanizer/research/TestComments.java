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
  public static void main(String[] args) throws MalformedTreeException, BadLocationException, CoreException {
    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("testAddComments");
    IJavaProject javaProject = JavaCore.create(project);
    IPackageFragment package1 = javaProject.getPackageFragments()[0];
    // get first compilation unit
    ICompilationUnit unit = package1.getCompilationUnits()[0];
    // parse compilation unit
    CompilationUnit astRoot = parse(unit);
    // create a ASTRewrite
    AST ast = astRoot.getAST();
    ASTRewrite rewriter = ASTRewrite.create(ast);
    // for getting insertion position
    TypeDeclaration typeDecl = (TypeDeclaration) astRoot.types().get(0);
    MethodDeclaration methodDecl = typeDecl.getMethods()[0];
    Block block = methodDecl.getBody();
    ListRewrite listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
    Statement placeHolder = (Statement) rewriter.createStringPlaceholder("//mycomment", ASTNode.EMPTY_STATEMENT);
    listRewrite.insertFirst(placeHolder, null);
    TextEdit edits = rewriter.rewriteAST();
    // apply the text edits to the compilation unit
    Document document = new Document(unit.getSource());
    edits.apply(document);
    // this is the code for adding statements
    unit.getBuffer().setContents(document.get());
    System.out.println("done");
  }

  /** @param unit
   * @return */
  private static CompilationUnit parse(ICompilationUnit u) {
    final ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setSource(u);
    parser.setResolveBindings(true); // we need bindings later on
    return (CompilationUnit) parser.createAST(null);
  }
}
