package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.step;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sharon LK
 * @since 19.5.17
 */
public class Union extends GenericMethodCallBasedBlock {
    public static final String TEMPLATE = "union";

    protected List<GenericEncapsulator> encapsulators = new ArrayList<>();

    public Union(PsiElement e) {
        super(e, TEMPLATE);

        calcUnionParticipants(e);
    }

    public Union(Encapsulator n) {
        super(n, TEMPLATE);

        calcUnionParticipants(n.inner);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected Union() {
        super(TEMPLATE);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return false;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return new Union(e);
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        for (GenericEncapsulator encapsulator : encapsulators) {
            if (encapsulator.generalizes(e)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculates the participants of this union, so the generalizes method can be implemented as
     * expected.
     *
     * @param e PSI element representing the union declaration
     */
    private void calcUnionParticipants(PsiElement e) {
        // The given PSI element is of the form union(1, method(), statement()), we want to extract
        // the arguments and parse them into basic blocks
        List<PsiExpression> arguments = step.arguments(az.methodCallExpression(e));

        // First argument is the index, we want to skip it when parsing the union structure
        arguments.remove(0);

        arguments.forEach(arg -> {
            if (arg.getText().startsWith("method")) {
                encapsulators.add(new Method());
            } else if (arg.getText().startsWith("statement")) {
                encapsulators.add(new Statement());
            } else if (arg.getText().startsWith("anyBlock")) {
                encapsulators.add(new Block());
            } else if (arg.getText().startsWith("booleanExpression")) {
                encapsulators.add(new BooleanExpression());
            } else if (arg.getText().startsWith("booleanLiteral")) {
                encapsulators.add(new BooleanLiteral());
            } else if (arg.getText().startsWith("expression")) {
                encapsulators.add(new Expression());
            }
        });
    }
}
