package il.org.spartan.Leonidas.plugin.GUI.AddTipper;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.tippers.LeonidasTipper;
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
    public Map<String, String> tippers;

    public CustomLeonidasTippers() {
        tippers = new HashMap<>();
    }

    /**
     * @return instance of this class
     */
    public static CustomLeonidasTippers getInstance() {
        return ServiceManager.getService(CustomLeonidasTippers.class);
    }

    @Override
	@Nullable
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
    public Map<String, String> getTippers() {
        return tippers;
    }

    public void setTippers(Map<String, String> tippers) {
        this.tippers = tippers;
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
        tippers.put(name, getTipperString(name, description, matcher, replacer));

        Toolbox.getInstance().add(new LeonidasTipper(name, tippers.get(name)));
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

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return tippers == null ? ((CustomLeonidasTippers) o).tippers == null
				: tippers.equals(((CustomLeonidasTippers) o).tippers);
    }

    @Override
    public int hashCode() {
        return tippers == null ? 0 : tippers.hashCode();
    }
}
