package il.org.spartan.Leonidas.plugin.GUI.AddTipper;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.tippers.LeonidasTipper;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sharon LK
 */
@State(
        name = "CustomLeonidasTippers",
        storages = {
                @Storage("CustomLeonidasTippers.xml")
        }
)
public class CustomLeonidasTippers implements PersistentStateComponent<CustomLeonidasTippers> {
    protected Map<String, Tipper> tippers = new HashMap<>();

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
    public Map<String, Tipper> getTippers() {
        return tippers;
    }

    /**
     * Returns the tipper that matches the given name.
     *
     * @param name tipper's name
     * @return tipper with the given name, or <code>null</code> if such tipper is not present
     */
    public Tipper getTipper(String name) {
        return tippers.get(name);
    }

    /**
     * Generates a leonidas source file from the given matcher and replacer.
     *
     * @param name        tipper's name
     * @param description tipper's description
     * @param matcher     matcher function source code
     * @param replacer    replacer function source code
     */
    public void generate(String name, String description, String matcher, String replacer) {
        tippers.put(name, new LeonidasTipper(name, getTipperString(name, description, matcher, replacer)));

        Toolbox.getInstance().add(tippers.get(name));
    }

    private String getTipperString(String name, String description, String matcher, String replacer) {
        return String.format("/**\n" +
                "* %s\n" +
                "*/\n" +
                "public class %s implements LeonidasTipperDefinition {\n" +
                "    @Override\n" +
                "    public void matcher() {\n" +
                "        new Template(() -> {\n" +
                "           /* start */\n" +
                "           %s\n" +
                "           /* end */\n" +
                "       });\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void replacer() {\n" +
                "        new Template(() -> {\n" +
                "           /* start */\n" +
                "           %s\n" +
                "           /* end */\n" +
                "       });\n" +
                "    }\n" +
                "}\n", description, name, matcher, replacer);
    }
}
