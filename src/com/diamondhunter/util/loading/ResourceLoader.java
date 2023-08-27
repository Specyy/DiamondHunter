package com.diamondhunter.util.loading;

import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.util.logging.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResourceLoader {

    private static final Map<String, URL> loadedResources = new LinkedHashMap<>();

    protected ResourceLoader() {
    }

    public static URL loadResource(String path) {
        DiamondHunter diamondHunter = DiamondHunter.getImplementation();
        Logger logger = diamondHunter.getLogger();

        if (DiamondHunter.isDebugMode())
            logger.logLine(Logger.Level.INFO, "Loading resource: \"" + path + "\"");

        URL url = getResourceSilent(path);

        if (url == null) {
            logger.logLine(Logger.Level.WARNING, "Failed to load resource: \"" + path + "\"");
        }

        return url;
    }

    /**
     * Retrieves a resources from the loaded resources, if the given resources has already
     * loaded and loads it if it hasn't.
     *
     * @param path The path to the resource to load
     * @return Returns the loaded resource as a {@code URL}
     */
    public static URL retrieveResource(String path) {
        URL url = loadedResources.get(path);

        if (url == null)
            url = loadResource(path);

        return url;
    }

    private static URL getResourceSilent(String path) {
        if (path == null) return null;

        URL url = getResource(path);

        if (url == null) {
            URL newUrl = ClassLoader.getSystemResource(path);

            if (newUrl == null) {
                return null;
            } else {
                loadedResources.put(path, newUrl);
                return newUrl;
            }
        }

        loadedResources.put(path, url);
        return url;
    }

    private static URL getResource(String path) {
        return ResourceLoader.class.getClassLoader().getResource(path);
    }

    public static URL[] loadResources(String folder) {
        List<URL> urls = new LinkedList<>();

        URL resources;

        if (folder == null)
            resources = getResource("");
        else resources = getResource(folder);

        assert resources != null;
        File file = new File(resources.toExternalForm());
        File[] files = file.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                if (!files[i].isDirectory()) {
                    try {
                        urls.add(files[i].toURI().toURL());
                    } catch (MalformedURLException e) {
                        if (DiamondHunter.isDebugMode()) {
                            DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not convert file: \"" + files[i].getPath() + "\"'s URI to a URL:");
                            e.printStackTrace();
                        } else {
                            DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not load file/folder: \"" + files[i].getPath() + "\"");
                        }
                    }
                }

                if (files[i].listFiles() != null) {
                    urls.addAll(getFolderResources(files[i]));
                }
            }
        } else {
            try {
                urls.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                if (DiamondHunter.isDebugMode()) {
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not convert file: \"" + file.getPath() + "\"'s URI to a URL:");
                    e.printStackTrace();
                } else {
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not load file/folder: \"" + file.getPath() + "\"");
                }
            }
        }

        return urls.toArray(new URL[0]);
    }

    private static List<URL> getFolderResources(File folder) {
        List<URL> urls = new LinkedList<>();

        File[] files = folder.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) {
                    try {
                        urls.add(files[i].toURI().toURL());
                    } catch (MalformedURLException e) {
                        if (DiamondHunter.isDebugMode()) {
                            DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not convert file: \"" + files[i].getPath() + "\"'s URI to a URL:");
                            e.printStackTrace();
                        } else {
                            DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not load file/folder: \"" + files[i].getPath() + "\"");
                        }
                    }
                }

                try {
                    urls.add(files[i].toURI().toURL());
                } catch (MalformedURLException e) {
                    if (DiamondHunter.isDebugMode()) {
                        DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not convert file: \"" + files[i].getPath() + "\"'s URI to a URL:");
                        e.printStackTrace();
                    } else {
                        DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not load file/folder: \"" + files[i].getPath() + "\"");
                    }
                }
            }
        } else {
            try {
                urls.add(folder.toURI().toURL());
            } catch (MalformedURLException e) {
                if (DiamondHunter.isDebugMode()) {
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not convert file: \"" + folder.getPath() + "\"'s URI to a URL:");
                    e.printStackTrace();
                } else {
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not load file/folder: \"" + folder.getPath() + "\"");
                }
            }
        }

        return urls;
    }

    ////
    public static URL[] loadResourcesFolders(String folder) {
        List<URL> urls = new LinkedList<>();

        URL resources;

        if (folder == null)
            resources = getResource("");
        else resources = getResource(folder);

        assert resources != null;
        File file = new File(resources.toExternalForm());
        File[] files = file.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                try {
                    urls.add(files[i].toURI().toURL());
                } catch (MalformedURLException e) {
                    if (DiamondHunter.isDebugMode()) {
                        DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not convert file: \"" + files[i].getPath() + "\"'s URI to a URL:");
                        e.printStackTrace();
                    } else {
                        DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not load file/folder: \"" + files[i].getPath() + "\"");
                    }
                }

                if (files[i].listFiles() != null) {
                    urls.addAll(getFolderResources(files[i]));
                }
            }
        } else {
            try {
                urls.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                if (DiamondHunter.isDebugMode()) {
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not convert file: \"" + file.getPath() + "\"'s URI to a URL:");
                    e.printStackTrace();
                } else {
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not load file/folder: \"" + file.getPath() + "\"");
                }
            }
        }

        return urls.toArray(new URL[0]);
    }

    private static List<URL> getFolderResources0(File folder) {
        List<URL> urls = new LinkedList<>();

        File[] files = folder.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                try {
                    urls.add(files[i].toURI().toURL());
                } catch (MalformedURLException e) {
                    if (DiamondHunter.isDebugMode()) {
                        DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not convert file: \"" + files[i].getPath() + "\"'s URI to a URL:");
                        e.printStackTrace();
                    } else {
                        DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not load file/folder: \"" + files[i].getPath() + "\"");
                    }
                }

                try {
                    urls.add(files[i].toURI().toURL());
                } catch (MalformedURLException e) {
                    if (DiamondHunter.isDebugMode()) {
                        DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not convert file: \"" + files[i].getPath() + "\"'s URI to a URL:");
                        e.printStackTrace();
                    } else {
                        DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not load file/folder: \"" + files[i].getPath() + "\"");
                    }
                }
            }
        } else {
            try {
                urls.add(folder.toURI().toURL());
            } catch (MalformedURLException e) {
                if (DiamondHunter.isDebugMode()) {
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not convert file: \"" + folder.getPath() + "\"'s URI to a URL:");
                    e.printStackTrace();
                } else {
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not load file/folder: \"" + folder.getPath() + "\"");
                }
            }
        }

        return urls;
    }
}
