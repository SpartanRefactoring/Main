package il.org.spartan.Leonidas.plugin.GUI.AddTipper;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sharon LK
 */
public class CustomLeonidasTippers implements PersistentStateComponent<CustomLeonidasTippers> {
    protected List<String> tippers = new ArrayList<>();

    /**
     * @return instance of this class
     */
    public static CustomLeonidasTippers getInstance() {
        return ServiceManager.getService(CustomLeonidasTippers.class);
    }

    @Nullable
    @Override
    public CustomLeonidasTippers getState() {
        return this;
    }

    @Override
    public void loadState(CustomLeonidasTippers state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    /**
     * @return list of all currently loaded custom tippers
     */
    public List<String> getTippers() {
        return tippers;
    }

    /**
     * Generates a leonidas source file from the given matcher and replacer.
     *
     * @param name     tipper's name
     * @param matcher  matcher function source code
     * @param replacer replacer function source code
     */
    public void generate(String name, String matcher, String replacer) {
        tippers.add(getTipperString(name, matcher, replacer));
    }

    private String getTipperString(String name, String matcher, String replacer) {
        return String.format("public class %s implements LeonidasTipperDefinition {" +
                "   @Override\n" +
                "    public void matcher() {\n" +
                "        new Template(() -> {" +
                "           %s" +
                "       });\n" +
                "    }" +
                "" +
                "   @Override\n" +
                "    public void replacer() {\n" +
                "        new Template(() -> {" +
                "           %s" +
                "       });\n" +
                "    }" +
                "" +
                "}", name, matcher, replacer);
    }
}
