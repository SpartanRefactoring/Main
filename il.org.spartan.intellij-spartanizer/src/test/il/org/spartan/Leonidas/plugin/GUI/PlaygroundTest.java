package il.org.spartan.Leonidas.plugin.GUI;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import il.org.spartan.Leonidas.plugin.GUI.PlaygroundController.Playground;

/**
 * @author Anna Belozovsky
 * @since 06/05/2017
 */
public class PlaygroundTest extends LightPlatformCodeInsightFixtureTestCase {

    private Playground playground;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        playground = new Playground();
    }

    private void preparePlayground(String input) {
        playground.setInput(input);
        playground.doSpartanization();
    }

    public void testLeonidasTipper1() {
        preparePlayground("if(x==0){\n\tx=1;\n}");
        assert playground.getOutput().replaceAll("\\s+","").equals("if(x==0)x=1;");
    }

    public void testLeonidasTipper2() {
        preparePlayground("while(x==0){x=1;}");
        assert playground.getOutput().replaceAll("\\s+","").equals("while(x==0)x=1;");

    }

    public void testLeonidasTipper3() {
        preparePlayground("!(!(x))");
        assert playground.getOutput().equals("x");
    }


    public void testNanoTipper1() {
        preparePlayground("l.get(l.size()-1)");
        assert playground.getOutput().equals("last(l)");
    }

    public void testNanoTipper2() {
        preparePlayground("X == null ? Y : X");
        assert playground.getOutput().equals("defaults(X).to(Y)");
    }

    public void testNanoTipper3() {
        preparePlayground("i->{return i;}");
        assert playground.getOutput().equals("i-> i");
    }

    public void testClear() {
        preparePlayground("x");
        assert playground.getOutput().equals("x");
        playground.doClear();
        assert playground.getOutput().equals("");
        assert playground.getInput().equals("");
    }

    public void testClose() {
        assert playground.isDisplayable();
        playground.doClose();
        assert !playground.isDisplayable();
    }
}
