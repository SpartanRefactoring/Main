//package il.org.spartan.spartanizer.tippers;
//
//import java.util.*;
//
//import org.eclipse.jdt.core.dom.*;
//import org.eclipse.jdt.core.dom.rewrite.*;
//import org.eclipse.text.edits.*;
//
//import il.org.spartan.spartanizer.ast.factory.*;
//import il.org.spartan.spartanizer.ast.navigate.*;
//import il.org.spartan.spartanizer.ast.safety.*;
//import il.org.spartan.spartanizer.dispatch.*;
//import il.org.spartan.spartanizer.engine.*;
//import il.org.spartan.spartanizer.tipping.*;
//
///**
// * 
// * @author Dor Ma'ayan
// * @since 03-12-2016
// */
//public final class WhileNextReturnToWhile extends EagerTipper<WhileStatement> implements TipperCategory.Inlining {
//  @Override public String description(@SuppressWarnings("unused") final WhileStatement ¢) {
//    return "Iniline the return into the while statement";
//  }
//
//  @SuppressWarnings("boxing")
//  @Override public Tip tip(WhileStatement n) {
//    ReturnStatement nextRet = extract.nextReturn(n);
//    int breaks = new Recurser<>(n,0).preVisit((x) -> (iz.breakStatement(az.statement(x.getRoot())) ? (1 + x.getCurrent()) : x.getCurrent()));
//    if(nextRet==null || breaks!=0 ||iz.block(n.getBody()))
//      return null;
//    IfStatement inlineIf = subject.pair(nextRet, null).toNot(n.getExpression());
//    WhileStatement retWhile = duplicate.of(n);
//    List<Statement> lst = extract.statements(retWhile.getBody());
//    lst.add(inlineIf);
//    Block b = subject.ss(lst).toBlock();
//    retWhile.setBody(b);
//    return new Tip(description(n), n, this.getClass()) {
//      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
//        r.replace(n, retWhile, g);
//        r.remove(nextRet, g);
//      }
//    };
//  }
//}
