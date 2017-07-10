package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.UserControlled;
import il.org.spartan.Leonidas.plugin.tippers.LeonidasTipper;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Sharon
 * @since 29-06-2017.
 */
@State(
        name = "TipperDataManager",
        storages = {
                @Storage("TipperDataManager.xml")
        }
)
public class TipperDataManager implements PersistentStateComponent<TipperDataManager> {
    // A mapping from tipper name to (a mapping from basic block id to (a mapping from field name to its value)))
    public Map<String, String> data = new HashMap<>();

    private boolean recalculateData;

    public TipperDataManager() {
        data = new HashMap<>();
    }

    /**
     * @return instance of the data class
     */
    public static TipperDataManager getInstance() {
        return ServiceManager.getService(TipperDataManager.class);
    }

    @Override
	@Nullable
	public TipperDataManager getState() {
		if (!recalculateData)
			return this;
		List<Tipper> tippers = Toolbox.getInstance().getAllTippers();
		data = new HashMap<>();
		tippers.stream().filter(tipper -> tipper instanceof LeonidasTipper).map(tipper -> (LeonidasTipper) tipper)
				.forEach(tipper -> {
					tipper.getMatcher().getAllRoots().stream().map(LeonidasTipper::getGenericElements)
							.flatMap(Collection::stream).forEach(encapsulator -> Stream.of(encapsulator.getClass().getFields())
									.filter(field -> field.isAnnotationPresent(UserControlled.class)).forEach(field -> {
										String key = "matcher_" + tipper.name() + "_" + encapsulator.getId() + "_"
												+ field.getName();
										try {
											data.put(key, DataConverter.convert(field.get(encapsulator)));
										} catch (IllegalAccessException e) {
											e.printStackTrace();
										}
									}));
					tipper.getReplacer().getAllRoots().stream().map(LeonidasTipper::getGenericElements)
							.flatMap(Collection::stream).forEach(encapsulator -> Stream.of(encapsulator.getClass().getFields())
									.filter(field -> field.isAnnotationPresent(UserControlled.class)).forEach(field -> {
										String key = "replacer_" + tipper.name() + "_" + encapsulator.getId() + "_"
												+ field.getName();
										try {
											data.put(key, DataConverter.convert(field.get(encapsulator)));
										} catch (IllegalAccessException e) {
											e.printStackTrace();
										}
									}));
				});
		return this;
	}

    @Override
    public void loadState(TipperDataManager state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    /**
     * Inject tipper specific data from the map to the actual tippers in the plugin.
     */
    public void load() {
        List<Tipper> tippers = Toolbox.getInstance().getAllTippers();

        // Iterate over all leonidas tippers
        tippers.stream()
                .filter(tipper -> tipper instanceof LeonidasTipper)
                .map(tipper -> (LeonidasTipper) tipper)
                .forEach(tipper -> {
                    // Iterate over all generic blocks in the matcher
                    tipper.getMatcher()
                            .getAllRoots()
                            .stream()
                            .map(LeonidasTipper::getGenericElements)
                            .flatMap(Collection::stream)
                            .forEach(encapsulator -> Stream.of(encapsulator.getClass().getFields())
									.filter(field -> field.isAnnotationPresent(UserControlled.class))
									.filter(field -> DataConverter.isSupported(field.getType()))
									.filter(field -> data.containsKey("matcher_" + tipper.name() + "_"
											+ encapsulator.getId() + "_" + field.getName()))
									.forEach(field -> {
										try {
											field.set(encapsulator,
													DataConverter.decode(
															data.get("matcher_" + tipper.name() + "_"
																	+ encapsulator.getId() + "_" + field.getName()),
															field.getType()));
										} catch (IllegalAccessException e) {
											e.printStackTrace();
										}
									}));

                    // Iterate over all generic blocks in the replacer
                    tipper.getReplacer()
                            .getAllRoots()
                            .stream()
                            .map(LeonidasTipper::getGenericElements)
                            .flatMap(Collection::stream)
                            .forEach(encapsulator -> Stream.of(encapsulator.getClass().getFields())
									.filter(field -> field.isAnnotationPresent(UserControlled.class))
									.filter(field -> DataConverter.isSupported(field.getType()))
									.filter(field -> data.containsKey("replacer_" + tipper.name() + "_"
											+ encapsulator.getId() + "_" + field.getName()))
									.forEach(field -> {
										try {
											field.set(encapsulator,
													DataConverter.decode(
															data.get("replacer_" + tipper.name() + "_"
																	+ encapsulator.getId() + "_" + field.getName()),
															field.getType()));
										} catch (IllegalAccessException e) {
											e.printStackTrace();
										}
									}));
                });

        recalculateData = true;
    }

    /**
     * @return mapping from field key to its value
     */
    public Map<String, String> getData() {
        return data;
    }

    /**
     * Sets a new mapping from fields and their values.
     *
     * @param data new data mapping
     */
    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
		return o == this || (o != null && getClass() == o.getClass()
				&& (data == null ? ((TipperDataManager) o).data == null : data.equals(((TipperDataManager) o).data)));
	}

    @Override
    public int hashCode() {
        return data == null ? 0 : data.hashCode();
    }
}
