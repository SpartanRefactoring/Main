package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.factory.subject.*;
import il.org.spartan.spartanizer.ast.navigate.*;

@SuppressWarnings("static-method")
public class Issue233 {
  @Ignore public void a() {
    trimmingOf("switch(x) {}").gives(";").stays();
  }

  @Ignore public void b() {
    trimmingOf("switch(x) {} switch(x) {}").gives("").stays();
  }

  // not sure if need to implement the below tipper on this issue
  @Ignore public void c() {
    trimmingOf("switch(x) { default: k=5; break; }").gives("{k=5}").stays();
    
//    wizard.ast("switch(x) {case 1:break; default:break;}").accept(new ASTVisitor() {
////      @Override public void preVisit(ASTNode node) {
////        // TODO Auto-generated method stub
////        super.preVisit(node);
////        System.out.println("xx " + node.toString() + " xx\n");
////      }
//      
//      
//      @Override public boolean visit(SwitchStatement node) {
//        // TODO Auto-generated method stub
//        List<Statement> ll = node.statements();
//        ll.remove(0);
//        SeveralStatements ta = subject.ss(ll);
//        System.out.println("xx " + subject.statement(ta) + " xx\n");
//        return super.visit(node);
//      }
//    });
//    
//    wizard.ast("switch(x) {}").accept(new ASTVisitor() {
//      @Override public void preVisit(ASTNode node) {
//        // TODO Auto-generated method stub
//        super.preVisit(node);
//        System.out.println(node.getClass());
//      }
//    });
  }
}
