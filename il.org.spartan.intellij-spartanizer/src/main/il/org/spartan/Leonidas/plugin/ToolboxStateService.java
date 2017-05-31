package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    @Override
    public ToolboxStateService getState() {
        return this;
    }

    @Override
    public void loadState(ToolboxStateService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Nullable
    public static ToolboxStateService getInstance() {
        return ServiceManager.getService(ToolboxStateService.class);
    }
}
