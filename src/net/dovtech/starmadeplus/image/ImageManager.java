package net.dovtech.starmadeplus.image;

import org.schema.schine.graphicsengine.forms.Sprite;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ImageManager {

    public final static ConcurrentHashMap<String, Sprite> imgCache = new ConcurrentHashMap<>();
    public final static ConcurrentLinkedQueue<String> downloadingImages = new ConcurrentLinkedQueue<>();
}