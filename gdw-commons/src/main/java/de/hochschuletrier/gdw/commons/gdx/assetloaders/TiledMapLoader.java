package de.hochschuletrier.gdw.commons.gdx.assetloaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdxVBO;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.resourcelocator.IResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import java.util.HashMap;

/**
 *
 * @author Santo Pfingsten
 */
public class TiledMapLoader extends AsynchronousAssetLoader<TiledMap, TiledMapLoader.TiledMapParameter> {
    TiledMap map;
    
    public TiledMapLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle fileHandle, TiledMapParameter parameter) {
		try {
			map = new TiledMap(fileName, parameter.polyMode);
		} catch(Exception e) {
            return null;
		}
        
        Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
        TextureParameter params = new TextureParameter();
        params.format = parameter.format;
        params.genMipMaps = parameter.genMipMaps;
        params.minFilter = parameter.minFilter;
        params.magFilter = parameter.magFilter;
        params.wrapU = parameter.wrapU;
        params.wrapV = parameter.wrapV;
        
        IResourceLocator locator = CurrentResourceLocator.get();
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String path = locator.combinePaths(tileset.getFilename(), img.getSource());
            deps.add(new AssetDescriptor(path, ImageX.class, params));
        }
        return deps;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle fileHandle, TiledMapParameter parameter) {
    }

    @Override
    public TiledMap loadSync(AssetManager manager, String fileName, FileHandle fileHandle, TiledMapParameter parameter) {
        IResourceLocator locator = CurrentResourceLocator.get();
        
        HashMap<TileSet, ImageX> tilesetImages = new HashMap<TileSet, ImageX>(map.getTileSets().size());
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String path = locator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, manager.get(path, ImageX.class));
        }
        
        if(parameter.useVBO)
            map.setRenderer(new TiledMapRendererGdxVBO(map, tilesetImages));
        else
            map.setRenderer(new TiledMapRendererGdx(map, tilesetImages));
        return map;
    }

    /** Parameter to be passed to {@link AssetManager#load(String, Class, AssetLoaderParameters)} if
     * additional configuration is necessary for the {@link TiledMap}. */
    static public class TiledMapParameter extends AssetLoaderParameters<TiledMap> {
        boolean useVBO = false;
        LayerObject.PolyMode polyMode = LayerObject.PolyMode.ABSOLUTE;

        /** the format of the final Texture. Uses the source images format if null * */
        public Pixmap.Format format = null;
        /** whether to generate mipmaps * */
        public boolean genMipMaps = false;
        public TextureFilter minFilter = TextureFilter.Nearest;
        public TextureFilter magFilter = TextureFilter.Nearest;
        public Texture.TextureWrap wrapU = Texture.TextureWrap.ClampToEdge;
        public Texture.TextureWrap wrapV = Texture.TextureWrap.ClampToEdge;
    }
}
