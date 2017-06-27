package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;

/**
 * Replace if(b); else{s;} with if(!b){s;}
 *
 * @author melanyc
 * @since 30-04-2017
 */
@SuppressWarnings({"ConstantConditions", "StatementWithEmptyBody"})
public class IfEmptyThen implements LeonidasTipperDefinition {

    /**
     * Write here additional constraints on the matcher tree.
     * The constraint are of the form:
     * the(<generic element>(<id>)).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     */
    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            if (!booleanExpression(0))
				statement(1);
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            if (!(booleanExpression(0)))
                statement(1);
            /* end */
        });
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("int x=5;\nObject a,b;\na=new Object();\nb=new Object(); \nif(a!=b)\n\t;\nelse{\n\tx = 8;\n}", "int x=5;\nObject a,b;\na=new Object();\nb=new Object();\n if(!(a!=b)){\n\tx = 8;\n}")
                .map();

    }
}