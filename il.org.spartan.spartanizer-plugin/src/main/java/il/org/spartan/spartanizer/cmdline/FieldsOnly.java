package il.org.spartan.spartanizer.cmdline;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.visitor.*;

public class FieldsOnly {
  public static void main(final String[] args) {
    new MasterVisitor(args).visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final FieldDeclaration ¢) {
        System.out.println(¢);
        return true;
      }
    });
  }
}