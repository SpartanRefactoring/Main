package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.lang.invoke.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.utils.*;

public class PrintAllInterfaces {
  public static void main(final String[] args) {
    try (BufferedWriter out = system.callingClassUniqueWriter()) {
      MethodHandles.lookup();
      new GrandVisitor(args) {/**/}.visitAll(new ASTTrotter() {
        {
          final Rule<TypeDeclaration, Object> r = Rule.on(TypeDeclaration::isInterface).go(λ -> System.out.println(λ.getName()));
          final Predicate<TypeDeclaration> q = λ -> {
            System.out.println(λ);
            return λ.isInterface();
          };
          on(TypeDeclaration.class).hook(r.beforeCheck(System.out::println).beforeCheck(q).afterCheck(System.out::println)
              .beforeCheck(TypeDeclaration::isInterface).afterCheck(q).afterCheck(TypeDeclaration::isInterface));
        }
      });
    } catch (IOException x) {
      note.io(x);
    }
  }
}