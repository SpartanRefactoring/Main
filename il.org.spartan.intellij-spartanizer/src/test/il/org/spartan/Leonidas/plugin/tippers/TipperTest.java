package il.org.spartan.Leonidas.plugin.tippers;


import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.plugin.Spartanizer;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

/**
 * @author @roey maor
 * @since 27-05-2017.
 */
/*
This class should be used by all tipper tests in order to test the tippers by the examples they provide.
It is correct to demand the creator of the tipper to provide the examples for testing, and it's he's
responsibility to make sure he has included examples that provide adequate coverage for testing.
examples can be in one of two forms:

1) String -> String : The tipper should be applied on the former, and output a PSI Element tree
                      that conforms to the latter
2) String -> null : The tipper should not affect the former.

 */
public class TipperTest{

    private static final String before = "";
    private static final String after = "";
    Tipper tipper = null;
    LeonidasTipperDefinition leonidasTipper = null;
    Boolean leonidasMode; //whether we are testing a leonidas or non-leonidas tipper
    private Boolean setup = false;

    TipperTest(Tipper t){
        this.tipper = t;
        this.leonidasMode = false;
    }

    TipperTest(LeonidasTipperDefinition l){
        this.leonidasTipper = l;
        this.leonidasMode = true;
    }

    protected void setUp(){
        setup = true;
        return;
    }

    public boolean testExample(String input, String output){
        if(!setup) {this.setUp();}
        return false;
    }
}
