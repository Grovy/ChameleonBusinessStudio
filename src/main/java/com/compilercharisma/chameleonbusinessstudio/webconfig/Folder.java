package com.compilercharisma.chameleonbusinessstudio.webconfig;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Folder {
    private final Path path;

    public Folder(Path path){
        this.path = path;
    }

    public boolean doesFileExist(String fileName){
        var f = getFile(fileName);
        return f.isFile() && f.exists();
    }

    public File getFile(String fileName){
        return Paths.get(path.toString(), fileName).toFile();
    }
}
