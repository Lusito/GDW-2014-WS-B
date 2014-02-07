package de.hochschuletrier.gdw.commons.gdx.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.hochschuletrier.gdw.commons.resourcelocator.IResourceLocator;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 * @author Santo Pfingsten
 */
public class GdxResourceLocator implements IResourceLocator {

    private Files.FileType type;

    public GdxResourceLocator(Files.FileType type) {
        this.type = type;
    }

    @Override
    public InputStream locateResource(String filename) throws FileNotFoundException {
        FileHandle handle = Gdx.files.getFileHandle(filename, type);
        if (handle != null)
            return handle.read();
        return null;
    }

    @Override
    public String combinePaths(String base, String filename) {
        FileHandle parentHandle = Gdx.files.getFileHandle(base, type);
        if (parentHandle != null) {
            FileHandle handle = parentHandle.sibling(filename);
            if (handle != null)
                return handle.path();
        }
        return null;
    }
}
