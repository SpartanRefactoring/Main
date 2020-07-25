package il.org.spartan.spartanizer.cmdline;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import fluent.ly.note;
import fluent.ly.system;
import il.org.spartan.utils.Rule;

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
          on(TypeDeclaration.class).hook(r.beforeCheck((Consumer<TypeDeclaration>) System.out::println).beforeCheck(q).afterCheck((Consumer<TypeDeclaration>) System.out::println)
              .beforeCheck(TypeDeclaration::isInterface).afterCheck(q).afterCheck(TypeDeclaration::isInterface));
        }
      });
    } catch (IOException ¢) {
      note.io(¢);
    }
  }
}