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
  private String[] source;
  private String content;
  private int endRow;

  public CommentVisitor(CompilationUnit compilationUnit, String[] source) {

    super();
    this.compilationUnit = compilationUnit;
    this.source = source;
  }

  public boolean visit(LineComment node) {

    int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition()) - 1;
    String lineComment = source[startLineNumber].replaceFirst(".*//", "//");

    content = lineComment;
    endRow = startLineNumber;

    return true;
  }

  public boolean visit(BlockComment node) {

    int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition()) - 1;
    int endLineNumber = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength()) - 1;

    StringBuffer blockComment = new StringBuffer();

    for (int lineCount = startLineNumber; lineCount <= endLineNumber; lineCount++) {
      String blockCommentLine = source[lineCount];
      if (lineCount == startLineNumber) {
        blockCommentLine = blockCommentLine.replaceFirst(".*/\\*", "/\\*");
      }
      if (lineCount == endLineNumber) {
        blockCommentLine = new StringBuilder(
            new StringBuilder(blockCommentLine).reverse().toString().replaceFirst(".*/\\*", "/\\*")).reverse()
                .toString();
      }
      blockComment.append(blockCommentLine);
      if (lineCount != endLineNumber) {
        blockComment.append("\n");
      }
    }

    content = blockComment.toString();
    endRow = endLineNumber;

    return true;
  }

  public boolean visit(Javadoc node) {

    int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition()) - 1;
    int endLineNumber = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength()) - 1;

    StringBuffer blockComment = new StringBuffer();

    for (int lineCount = startLineNumber; lineCount <= endLineNumber; lineCount++) {
      String blockCommentLine = source[lineCount];
      if (lineCount == startLineNumber) {
        blockCommentLine = blockCommentLine.replaceFirst(".*/\\*\\*", "/\\*");
      }
      if (lineCount == endLineNumber) {
        blockCommentLine = new StringBuilder(
            new StringBuilder(blockCommentLine).reverse().toString().replaceFirst(".*/\\*", "/\\*")).reverse()
                .toString();
      }
      blockComment.append(blockCommentLine);
      if (lineCount != endLineNumber) {
        blockComment.append("\n");
      }
    }

    content = blockComment.toString();
    endRow = endLineNumber;

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
