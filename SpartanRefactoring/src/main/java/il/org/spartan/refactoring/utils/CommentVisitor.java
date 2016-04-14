package il.org.spartan.refactoring.utils;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;

//TODO changed by Ori Roth
public class CommentVisitor extends ASTVisitor {

  private CompilationUnit compilationUnit;
  private String source;
  private String content;
  private int endRow;

  public CommentVisitor(CompilationUnit compilationUnit, String source) {

    super();
    this.compilationUnit = compilationUnit;
    this.source = source;
  }

  public boolean visit(LineComment node) {

    int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition()) - 1;
    String lineComment = source.split("\n", -1)[startLineNumber].replaceFirst(".*//", "//");

    content = lineComment;
    endRow = startLineNumber;

    return true;
  }

  public boolean visit(BlockComment node) {

    int sp = node.getStartPosition();
    int ep = sp + node.getLength();
    content = new StringBuilder(source).substring(sp, ep).toString();
    endRow = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength()) - 1;;
  
    return true;
  }

  public boolean visit(Javadoc node) {

    int sp = node.getStartPosition();
    int ep = sp + node.getLength();
    content = new StringBuilder(source).substring(sp, ep).toString();
    endRow = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength()) - 1;;
  
    return true;
  }

  public void preVisit(ASTNode node) {

  }

  public String getContent() {
    return content;
  }

  public int getEndRow() {
    return endRow;
  }
}
