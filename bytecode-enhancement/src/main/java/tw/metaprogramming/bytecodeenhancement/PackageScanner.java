package tw.metaprogramming.bytecodeenhancement;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageScanner {
    public static Set<String> getAllClassesName(String packageName) throws IOException {
        Set<String> classes = new HashSet<>();
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                classes.addAll(getClassesByFileRecursive(packageName, filePath));
            }else if ("jar".equals(protocol)) {
                JarFile jar = ((JarURLConnection) url.openConnection())
                        .getJarFile();
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.charAt(0) == '/') {
                        name = name.substring(1);
                    }
                    if (name.startsWith(packageDirName)) {
                        int idx = name.lastIndexOf('/');
                        if (idx != -1) {
                            packageName = name.substring(0, idx)
                                    .replace('/', '.');
                        }
                        if (name.endsWith(".class")
                                && !entry.isDirectory()) {
                            String className = name.substring(
                                    packageName.length() + 1, name
                                            .length() - 6);
                            String fullClassName = packageName + '.' + className;
                            classes.add(fullClassName);
                        }
                    }
                }
            }
        }
        return classes;
    }

    private static Set<String> getClassesByFileRecursive(String packageName, String packagePath) {
        Set<String> classes = new HashSet<>();
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return classes;
        }
        File[] filesInDir = dir.listFiles(file -> file.isDirectory() || file.getName().endsWith(".class"));
        if(filesInDir == null){
            return classes;
        }
        for (File file : filesInDir) {
            if (file.isDirectory()) {
                classes.addAll(getClassesByFileRecursive(packageName + "." + file.getName(), file.getAbsolutePath()));
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                String fullClassName = packageName + '.' + className;
                classes.add(fullClassName);
            }
        }
        return classes;
    }
}
