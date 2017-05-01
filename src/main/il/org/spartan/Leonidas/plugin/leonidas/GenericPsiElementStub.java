package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.*;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.EncapsulatingNode;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes.GenericPsi;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes.GenericPsiBlock;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes.GenericPsiExpression;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes.GenericPsiStatement;

import java.util.Arrays;

/**
 * This class defines methods that will represent generic structures of code
 * such as: statements, conditions, blocks, variable declaration and more.
 *
 * @author Oren Afek
 * @since 06-01-2017
 */
public class GenericPsiElementStub {

    /**
     * method stub representing a boolean expression for leonidas tippers
     *
     * @param id the serial no to distinct between several boolean expressions in the same tipper
     * @return true
     */
    public static boolean booleanExpression(int id) {
        return booleanExpression();
    }

    public static boolean booleanExpression() {
        return true;
    }

    /**
     * method stub representing a statement for leonidas tippers
     *
     * @param id the serial no to distinct between several statements in the same tipper
     */
    public static Object statement(int id) {return new Object();}

    public static Object statement() {return new Object();}

    /**
     * method stub representing an identifier for leonidas tippers
     *
     * @param id the serial no to distinct between several identifiers in the same tipper
     * @return stub object
     */
    public static Object identifier(int id) {
        return identifier();
    }

    public static Object identifier() {
        return new Object();
    }

    /**
     * method stub representing any code block
     * @param id the serial no to distinct between several identifiers in the same tipper
     */
    public static void anyBlock(int id) {
    }

    public static void anyBlock() {/**/}

    /**
     * method stub representing an array identifier for leonidas tippers
     *
     * @param id the serial no to distinct between several array identifiers in the same tipper
     * @return stub array
     */
    public static Object[] arrayIdentifier(int id) {
        return arrayIdentifier();
    }

    public static Object[] arrayIdentifier() {
        return new Object[0];
    }

    /**
     * An enum representing the different coding blocks.
     */
    public enum StubName {
        BOOLEAN_EXPRESSION("booleanExpression"),
        STATEMENT("statement"),
        IDENTIFIER("identifier"),
        ARRAY_IDENTIFIER("arrayIdentifier"),
        ANY_BLOCK("anyBlock");

        private String stubName;

        StubName(String stubName) { this.stubName = stubName; }

        /**
         * @param x method call such as: booleanExpression(3), statement(2)...
         * @return the name of the generic type represented in the method call.
         */
        public static StubName valueOfMethodCall(PsiMethodCallExpression x) {
            if (x == null) return null;
            return Arrays.stream(values())
                    .filter(stub -> x.getMethodExpression().getText().equals(stub.stubName()))
                    .findFirst().orElseGet(null);
        }

        /**
         * @param s string representing generic type: "booleanExpression(3)", "statement(2)"...
         * @return the name of the generic type represented in the method call.
         */
        public static StubName valueOfStringExpression(String s) {
            return Arrays.stream(values())
                    .filter(stub -> s.equals(stub.stubMethodCallExpression()))
                    .findFirst().orElse(null);
        }

        /**
         * @param e psi element
         * @return the StubName of the genericType most qualified to the element.
         */
        public static StubName getGeneralType(PsiElement e) {
            Wrapper<StubName> name = new Wrapper<>(null);
            e.accept(new JavaElementVisitor() {
                @Override
                public void visitCodeBlock(PsiCodeBlock b) {
                    super.visitCodeBlock(b);
                    name.set(ANY_BLOCK);
                }

                @Override
                public void visitExpression(PsiExpression x) {
                    super.visitExpression(x);
                    name.set(BOOLEAN_EXPRESSION);
                }

                @Override
                public void visitStatement(PsiStatement s) {
                    super.visitStatement(s);
                    name.set(STATEMENT);
                }

                @Override
                public void visitIdentifier(PsiIdentifier i) {
                    super.visitIdentifier(i);
                    name.set(IDENTIFIER);
                }

                @Override
                public void visitArrayAccessExpression(PsiArrayAccessExpression x) {
                    super.visitArrayAccessExpression(x);
                    name.set(ARRAY_IDENTIFIER);
                }
            });
            return name.get();
        }

        public String stubName() {
            return stubName;
        }

        /**
         * @return an example of method call expression with the current generic type.
         */
        public String stubMethodCallExpression() {
            return String.format("%s()", stubName);
        }

        /**
         * @return an example of method call statement (followed by ";") with the current generic type.
         */
        public String stubMethodCallExpressionStatement() {
            return String.format("%s();", stubName);
        }

        /**
         * @param inner the psi element that the generic type generalizes (usually a method call)
         * @param id the ID inside the replaced element (if it isn't the direct method call, then we won't be able to retrieve it from "inner").
         * @return
         */
        public GenericPsi getGenericPsiType(PsiElement inner, Integer id) {
            GenericPsi x;
            switch (this) {
                case BOOLEAN_EXPRESSION:
                    x = new GenericPsiExpression(PsiType.BOOLEAN, inner);
                    break;
                case STATEMENT:
                    x = new GenericPsiStatement(inner);
                    break;
                case ANY_BLOCK:
                    x = new GenericPsiBlock(inner);
                    break;
                default:
                    return null;
            }
            x.putUserData(KeyDescriptionParameters.ID, id);
            return x;
        }

        /**
         * @param prv the method call expression representing the generic element.
         * @param next one step upwards in the psi tree of the code.
         * @return the true iff by the type of the generic element there is a need to go one step upwards.
         */
        public boolean goUpwards(EncapsulatingNode prv, EncapsulatingNode next) {
            if (next == null)
                return false;
            switch (this) {
                default:
                    return prv.getText().equals(next.getText());
                case STATEMENT:
                    return prv.getText().equals(next.getText()) || next.getText().equals(prv.getText() + ";");
                case ANY_BLOCK:
                    return !iz.block(prv.getInner());
            }
        }
    }
}
