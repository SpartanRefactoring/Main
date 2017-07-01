package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;

/** TODO Matteo Orru': document class 
 * 
 * @author Matteo Orru'
 * @since 2017-06-30 */
public class BasicStats extends ASTInFilesVisitor{
  
  private static int classNum;
  private static int methodNum;
  //List<String> locations;
  
  public BasicStats(String[] args) {
    super(args);
  }

  private void intialize() {
    classNum = 0;
    methodNum = 0;
  }

  public static void main(String[] args){
    out = system.callingClassUniqueWriter();
    try {
      out.write("project\tClassesNum\tMethodsNum\n");
    } catch (IOException x1) {
      x1.printStackTrace();
    }
    BasicStats a = new BasicStats(args);
//      {
//       private ASTVisitor astVisitor;
//       public void visitAll(final ASTVisitor ¢) {
//         //notify.beginBatch();
//         astVisitor = ¢;
//         locations.forEach(
//             λ -> {
//               setCurrentLocation(λ);
//               visitLocation();
//               try {
//                 out.write(getCurrentLocation() + classNum + methodNum);
//               } catch (IOException x) {
//                 x.printStackTrace();
//               }
//             }
//           );
//         }
//      };
      
      a.visitAll(new ASTTrotter() {
      @Override public boolean visit(final TypeDeclaration ¢) {
        ++classNum;
        System.err.printf(getCurrentLocation() + " Visiting Type: %s\t(%d)\n",¢.getName(),classNum);
//        System.err.println(locations.size());
        return true;
       }
       
      @Override public boolean preVisit2(final ASTNode ¢){
        return true;
       }
       @Override public boolean visit(final MethodDeclaration ¢) {
         ++methodNum;
         System.err.printf(getCurrentLocation() + " Visiting Method: %s\t(%d)\n",¢.getName(),methodNum);
         return true;
        }
       @Override protected void record(final String summary) {
           //System.err.println(classnum);
       }
       @Override public boolean visit(final ReturnStatement ¢) {
         return true;
       }
     });
     try {
      out.close();
    } catch (IOException x) {
      x.printStackTrace();
    }
     System.err.printf("Num Classes: %d\t Num Methods: %d\n", classNum, methodNum);
  }
  
public void visitAll(final ASTVisitor ¢) {
//notify.beginBatch();
astVisitor = ¢;
locations.forEach(
    λ -> {
      setCurrentLocation(λ);
      intialize();
      visitLocation();
      try {
        out.write(getCurrentLocation() + "\t" + classNum + "\t" + methodNum + "\n");
      } catch (IOException x) {
        x.printStackTrace();
      }
    }
  );
}

  
  
//  public static void main(final String[] args) {
//    out = system.callingClassUniqueWriter();
//    new GrandVisitor(args) {/**/}.visitAll(new ASTTrotter() {
//      
//      @Override boolean interesting(final MethodDeclaration ¢) {
//        return !¢.isConstructor();
//      }
//      
////      @Override boolean interesting(final MethodDeclaration ¢) {
////        return !¢.isConstructor() && interesting(statements(body(¢))) && leaking(descendants.streamOf(¢));
////      }
//      
////      @Override protected void record(final String summary) {
////        try {
////          GrandVisitor.out.write(summary);
////        } catch (final IOException ¢) {
////          note.bug(¢);
////        }
////        super.record(summary);
////      }
//    });
//  }
}
