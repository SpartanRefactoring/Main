package il.org.spartan.spartanizer.cmdline;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;

public class FieldsOnly {
  public static void main(final String[] args) {
    new GrandVisitor(args).visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final FieldDeclaration ¢) {
        System.out.println(¢);
        return true;
      }
    });
  }
}