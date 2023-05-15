package Persistence;


import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config extends Properties {
    private static final Config instance = new Config();
    protected String configPath;
    protected boolean loaded;
    private Config() {}
    public void setPath(String Path) { // sets config file path
        configPath = Path;
    }

    public void init() { // populates the properties hashmap
        try {
            load(new FileReader(configPath));
        } catch (IOException e) {
            System.out.println("Error Reading Config File : " + configPath);
        }
    }

    public <T> T getProperty(String K, Class<T> c) { // loads a single property from config file
        if (!loaded) {
            init();
            loaded = true;
        }
        String value = getProperty(K);
        try {
            return c.getConstructor(String.class).newInstance(value);
        } catch (Exception ignored) {}
        return null;
    }

    public static Config getInstance() {
        return instance;
    }
}

