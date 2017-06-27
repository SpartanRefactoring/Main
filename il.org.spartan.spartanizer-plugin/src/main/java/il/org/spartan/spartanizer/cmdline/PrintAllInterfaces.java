package il.org.spartan.spartanizer.cmdline;

import java.lang.invoke.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.utils.*;

public class PrintAllInterfaces {
  public static void main(final String[] args) {
    ASTInFilesVisitor.out = system.callingClassUniqueWriter();
    MethodHandles.lookup();
    new ASTInFilesVisitor(args) {/**/}.visitAll(new ASTTrotter() {
      {
        final Rule<TypeDeclaration, Object> r = Rule.on((final TypeDeclaration t) -> t.isInterface()).go(λ -> System.out.println(λ.getName()));
        final Predicate<TypeDeclaration> p = λ -> λ.isInterface(), q = λ -> {
          System.out.println(λ);
          return λ.isInterface();
        };
        final Consumer<TypeDeclaration> c = λ -> System.out.println(λ);
        on(TypeDeclaration.class).hook(r.beforeCheck(c).beforeCheck(q).afterCheck(c).beforeCheck(p).afterCheck(q).afterCheck(p));
      }
    });
  }
}