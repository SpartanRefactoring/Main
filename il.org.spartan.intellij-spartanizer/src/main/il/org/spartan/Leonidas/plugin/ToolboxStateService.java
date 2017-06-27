package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anna Belozovsky
 * @since 31/05/2017
 */
@State(
        name = "ToolboxStateService",
        storages = {
                @Storage("ToolboxStateService.xml")}
)
public class ToolboxStateService implements PersistentStateComponent<ToolboxStateService> {
    private Map<String, Boolean> tippers = new HashMap<>();

    @Nullable
    public static ToolboxStateService getInstance() {
        return ServiceManager.getService(ToolboxStateService.class);
    }

    Map<String, Boolean> getTippers() {
        return tippers;
    }

    void updateTipper(String tipperName, boolean val) {
        tippers.put(tipperName, val);
    }

    void addTipper(String tipperName) {
        tippers.put(tipperName, true);
    }

    void updateAllTippers(List<String> activeTippers) {
        tippers.keySet().forEach(tipper -> {
            if (activeTippers.contains(tipper))
				tippers.put(tipper, true);
			else
				tippers.put(tipper, false);
        });
    }

    @Override
	@Nullable
	public ToolboxStateService getState() {
		return this;
	}

    @Override
    public void loadState(ToolboxStateService state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
