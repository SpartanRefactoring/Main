package il.org.spartan.Leonidas.plugin.utils.logging;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import il.org.spartan.Leonidas.plugin.PluginDescriptorReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Supplies different utils for editing, probing, and creating our log files at low level. (raw read / writes)
 * Currently supports one single log file.
 *
 * @author RoeiRaz
 * @since 31-3-17
 */
class LogFileUtils {

    private static final String RELATIVE_LOG_PATH = "log/";
    private static final String BASE_LOG_NAME = "log";
    private static final String LOG_EXT = "txt";
    private static final String SLASH = System.getProperty("file.separator");
    private static final String DOT = ".";

    /**
     * When running tests through intellij, the path seems to be:
     * <IDEA_PROJECTS_DIR>/<PROJECT_DIR>/build/resources/...
     * NOTE: there is no need to put logs in tests.
     * When running idea instance through gradle, the path seems to be:
     * <IDEA_PROJECTS_DIR>/<PROJECT_DIR>/build/idea-sandbox/plugins/<PLUGIN_NAME>/...
     *
     * @return The path of the logfile to be used.
     */
    private static String getPath() {
        PluginId ourPluginId = PluginId.getId(PluginDescriptorReader.getPluginId());
        IdeaPluginDescriptor ourPlugin = PluginManager.getPlugin(ourPluginId);
        if (ourPlugin == null)
			throw new RuntimeException("Cannot retrieve plugin descriptor");
        return ourPlugin.getPath().getPath() + SLASH + RELATIVE_LOG_PATH + BASE_LOG_NAME + DOT + LOG_EXT;
    }

    /**
     * Creates the log file and all the containing directories
     * if they are not already exist.
     */
    private static void ensureLogExistance() {
        File f = new File(getPath());
        if (!f.exists())
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

    /**
	 * Appends a string to the log file.
	 * @param s  - The string to append to the logfile
	 */
	static synchronized void appendToLogFile(String s) {
		try {
			ensureLogExistance();
			new FileWriter(getPath(), true).append(s).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
