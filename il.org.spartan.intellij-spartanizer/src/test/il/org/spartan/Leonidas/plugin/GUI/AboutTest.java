package il.org.spartan.Leonidas.plugin.GUI;

import il.org.spartan.Leonidas.plugin.GUI.About.*;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

/**
 * Created by roym on 27/06/17.
 */
public class AboutTest extends LightPlatformCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testAppearence(){
        AboutAction aa = new AboutAction();
        aa.actionPerformed(null);
    }
}
