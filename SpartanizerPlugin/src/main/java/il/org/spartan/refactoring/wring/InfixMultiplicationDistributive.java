package il.org.spartan.refactoring.wring;

import java.util.*;
import java.util.concurrent.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.utils.Utils.*;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

import static il.org.spartan.idiomatic.*;

public final class InfixMultiplicationDistributive extends ReplaceCurrentNode<InfixExpression> implements Kind.DistributiveRefactoring {
  
  @Override String description(InfixExpression e) {
    return "Apply the distributive rule to " + e;
  }

  @Override public String description() {
    return "a*b + a*c => a * (b + c)";
  }
  
  @Override ASTNode replacement(final InfixExpression e) {
    return e.getOperator() != PLUS ? null : (asInfixExpression(e.getLeftOperand()).getOperator() == TIMES) && 
        (asInfixExpression(e.getRightOperand()).getOperator() == TIMES) ? 
            replacement(asInfixExpression(e.getLeftOperand()), 
                asInfixExpression(e.getRightOperand())) : null; //replacement(extract.allOperands(e));
  }

  private ASTNode replacement(InfixExpression e1, InfixExpression e2) {

    List<Expression> common = new ArrayList<Expression>();
    List<Expression> different = new ArrayList<Expression>();
//    System.out.println(common);
//    System.out.println(extract.allOperands(e1));
        
    for(Expression op : extract.allOperands(e1)){
//      System.out.println(op);
//      System.out.println(isIn(op,extract.allOperands(e2)));
//      System.out.println(in(op,extract.allOperands(e2)));
      if(isIn(op,extract.allOperands(e2)))
        common.add(op);
      else
        different.add(op);
    }
    
    for(Expression op : extract.allOperands(e2)){ // [a c]
      if(!isIn(op,common))
        different.add(op);
    }
    
//    different.addAll(extract.allOperands(e2));
    
    System.out.println("different: " + different);
    System.out.println("-----" + different.remove(common));
    
    if(!common.isEmpty()){
      different.remove(common);
    }
    
//    System.out.println("common: " + common);
//    System.out.println("different: " + different);
    
//    Expression p = null;
    
    Expression multiplication = subject.pair(different.get(0),different.get(1)).to(Operator.PLUS);
    Expression addition = subject.pair(common.get(0), multiplication).to(Operator.TIMES);
    System.out.println(multiplication);
    System.out.println(addition);
        
//    if(same(left(e1),left(e2))) {
//      System.out.println("" + left(e1) + " == " + left(e2));
//      p = subject.pair(left(e1), subject.pair(right(e1),right(e2)).to(Operator.PLUS)).to(Operator.TIMES);
//      System.out.println(p);
//    }
//    
//    if(same(right(e1),left(e2))) 
//      System.out.println("" + right(e1) + " == " + left(e2));
//      p = subject.pair(right(e1), subject.pair(left(e1),right(e2)).to(Operator.PLUS)).to(Operator.TIMES);
//      System.out.println(p);
//    
//    if(same(left(e1),right(e2))) 
//      System.out.println("" + left(e1) + " == " + right(e2));
//      p = subject.pair(right(e1), subject.pair(left(e1),left(e2)).to(Operator.PLUS)).to(Operator.TIMES);
//      System.out.println(p);
//    
//    if(same(right(e1),right(e2))) 
//      System.out.println("" + right(e1) + " == " + right(e2));
//      p = subject.pair(right(e1), subject.pair(left(e1),left(e2)).to(Operator.PLUS)).to(Operator.TIMES);
//      System.out.println(p);
    
    return addition;
  }

  private boolean isIn(Expression op, List<Expression> allOperands) {
    for(Expression $ : allOperands)
      if(same(op,$))
        return true; 
    return false;
  }

  private Expression collpse(final InfixExpression e) {
    Expression $;
    return null;
  }

  private ASTNode replacement(List<Expression> es) {
    System.out.println(es);
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : es)
//      same(core(¢).left(¢),core(¢).right)
      System.out.println(¢);
    return null; // (es.size() == 2) ? replacement(left(asInfixExpression(es)),right(asInfixExpression(es))) : null;
  }
  
  @Override boolean scopeIncludes(InfixExpression n) {
    return super.scopeIncludes(n);
  }

//  @Override ASTNode replacement(final InfixExpression e) {
////    System.out.println(parenthesize(e));
//    System.out.println(left(e));
//    System.out.println(right(e));
////    System.out.println(ExpressionComparator.literalCompare(left(e), right(e)));
//    if(asInfixExpression(left(e)).getOperator() == TIMES && asInfixExpression(right(e)).getOperator() == TIMES){
//      List<Expression> comm = new ArrayList<>();
//      List<Expression> all = new ArrayList<>();
//      for (Expression o1 : extract.allOperands(asInfixExpression(right(e)))){
//        all.add(o1);
//        for (Expression o2: extract.allOperands(asInfixExpression(left(e)))) {
//          all.add(o2);
//          System.out.println("" + o1 + " == " + o2 );
//          if(o1.toString().equals(o2.toString())){
//              comm.add(o1);
////          System.out.println(o1.toString().equals(o2.toString()) ? 
////              subject.Several("FOUND!!" : "BAD LUCK!!");
//          }
//        }
//      }
//      System.out.println("all: " + all);
//    }
//      Have.trueLiteral(extract.allOperands(asInfixExpression(right(e))));
//    
//      if(asInfixExpression(left(e)) != null) ;
//    System.out.println(same(left(e),right(e)));
//    System.out.println("core: " + extract.core(e));
//    System.out.println("ancestors: " + extract.ancestors(e));
//    System.out.println("Funcs.asString: " + Funcs.asString(e));
//    System.out.println(Funcs.asStringLiteral(e));
//    
//    if(e.getOperator() == PLUS) {
////      System.out.println(e.getOperator());
//      List<Expression> operands = extract.allOperands(e);
//      if(operandInCommon(operands.get(0), operands.get(1))) // (a * b) and (a * c)
//        System.out.println(findCommonOperands(operands.get(0), operands.get(1)));
////      System.out.println("operands" + operands);
//      for(Expression operand: operands)
//        if(Is.infix(operand))
//          if(asInfixExpression(operand).getOperator() == TIMES)
//            System.out.println(extract.allOperands(asInfixExpression(operand)));
//      return e;
//    }
////    return e;
////    return e.getOperator() != TIMES ? replacement(e.getLeftOperand(), e.getRightOperand()) : e ;
////    final List<Expression> operands = extract.allOperands(e);
////    for(Expression operand : operands)
////      System.out.println(operand);
////    System.out.println(e);
////     System.out.println("all operands: " + extract.allOperands(e));
//     
//     List<Expression> allOperands = null;
//     ArrayList<Expression> common = null;
//    
//     if(e.getOperator() == PLUS){
//       if(((InfixExpression) e.getLeftOperand()).getOperator() == TIMES &&
//           ((InfixExpression) e.getRightOperand()).getOperator() == TIMES){
//       ArrayList<Expression> leftOperands = (ArrayList<Expression>) extract.allOperands((InfixExpression) e.getLeftOperand());
//       ArrayList<Expression> rightOperands = (ArrayList<Expression>) extract.allOperands((InfixExpression) e.getRightOperand());
////       System.out.println(leftOperands);
////       System.out.println(rightOperands);
//       
////       common = findInCommon(leftOperands, rightOperands);
////       common = new ArrayList<Expression>(leftOperands);
////       System.out.println("size: " + common.size());
////       System.out.println("common: " + common);
//       
////       allOperands = extract.allOperands(e);
////       System.out.println(allOperands);
////       common.retainAll(rightOperands);
////       if(rightOperands.get(0).toString().equals(leftOperands.get(0).toString())){
////         System.out.println(leftOperands.toString());
////         System.out.println("OOOOOOOOOOK!");
////       }
////       System.out.println(common.size());
//       }
//         
////      replacement((InfixExpression) e.getLeftOperand());
////      replacement((InfixExpression) e.getRightOperand());
////      InfixExpression leftOperand = (InfixExpression) e.getLeftOperand();
////      InfixExpression rightOperand = (InfixExpression) e.getRightOperand();
////      System.out.println("leftOperand: " + leftOperand);
////      System.out.println("rightOperand: " + rightOperand);
////      if(leftOperand.getOperator() == TIMES && rightOperand.getOperator() == TIMES)
////        System.out.println("OK");
////        ArrayList<Expression> leftOperands = (ArrayList<Expression>) extract.allOperands(leftOperand);
////        ArrayList<Expression> rightOperands = (ArrayList<Expression>) extract.allOperands(rightOperand);
////        ArrayList<Expression> common = findCommonOperands(leftOperands, rightOperands);
////        for(Expression c:common)
////          System.out.println(" ---->> " + c);
////      //    return null;
//////      return e.getRoot();
//       Expression p = subject.pair(e.getRightOperand(), e.getLeftOperand()).to(InfixExpression.Operator.PLUS);
////       return plant(duplicate(asInfixExpression(e.getLeftOperand()))).into(e.getParent());
//       return plant(duplicate(p)).into(e.getParent());
//
//
//     }
////     if(e.getOperator() == TIMES)
//////       if (Funcs.isLiteral(e,))
////    return e; 
////    return null; 
//    return plant(duplicate(subject.pair(right(e), left(e)).to(PLUS))).into(e.getParent());
//  }
//
//
//  private static boolean operandInCommon(final Expression e1, final Expression e2) {
//    return findCommonOperands(e1,e2) != null;
// }
//
//  private static List<Expression> findCommonOperands(final Expression $1, final Expression $2) {
//    ArrayList<Expression> l1 = (ArrayList<Expression>) extract.allOperands(asInfixExpression($1));
//    ArrayList<Expression> l2 = (ArrayList<Expression>) extract.allOperands(asInfixExpression($2));
//    List<Expression> $ = new ArrayList<Expression>();
//    System.out.println($);
//    for(Expression operand : l1){
//      System.out.println(operand);
//      if(l2.contains(operand))
//        $.add(operand);
//    }
//    
//    
//    System.out.println("$: " + $);
////    $.retainAll(l2);
//    System.out.println("$: " + $);
//    return $;
//  }
//  

//  
//  //
////private InfixExpression replacement(Expression leftOperand, Expression rightOperand) {
////  return null;
////}
////
////  private ArrayList<Expression> findInCommon(ArrayList<Expression> leftOperands, ArrayList<Expression> rightOperands) {
//////    System.out.println("leftOperands: " + leftOperands);
//////    System.out.println("rightOperands: " + rightOperands);
////    ArrayList<Expression> common = new ArrayList<Expression>();
////    ArrayList<String> rightOperandsString = new ArrayList<String>();
////    for (Expression el: rightOperands){
////      rightOperandsString.add(el.toString());
////    }
//      
////    for(Expression el:leftOperands){
//////      System.out.println("el: " + el);
//////      System.out.println(rightOperands.contains(el));
////      if(rightOperandsString.contains(el.toString())){
//////        System.out.println(el);
////        common.add((Expression) el);
////      }
////    }
////    
////    return common;
////  }
//
////  private ArrayList<Expression> findCommonOperands(List<Expression> leftOperands, List<Expression> rightOperands) {
////    List<Expression> common = new ArrayList<Expression>(leftOperands);
////    common.retainAll(rightOperands);
////    return (ArrayList<Expression>) common;
////    
////  }

}
