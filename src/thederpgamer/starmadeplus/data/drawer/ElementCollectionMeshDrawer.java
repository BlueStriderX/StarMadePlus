package thederpgamer.starmadeplus.data.drawer;

import api.utils.draw.ModWorldDrawer;
import org.schema.game.client.view.shader.CubeMeshQuadsShader13;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.schine.graphicsengine.core.DrawableScene;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.shader.Shader;
import org.schema.schine.graphicsengine.shader.ShaderLibrary;
import org.schema.schine.graphicsengine.shader.Shaderable;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * ElementCollectionMeshDrawer.java
 * <Description>
 *
 * @author TheDerpGamer
 * @since 03/14/2021
 */
public class ElementCollectionMeshDrawer extends ModWorldDrawer implements Shaderable {

    public static ElementCollectionMeshDrawer instance;

    private HashMap<ElementCollection<?, ?, ?>, MeshDrawData> drawMap;
    private Shader elementCollectionShader;

    public ElementCollectionMeshDrawer() {
        instance = this;
        drawMap = new HashMap<>();
    }

    @Override
    public void onInit() {
        elementCollectionShader = ShaderLibrary.cubeGroupShader;
    }

    @Override
    public void update(Timer timer) {
        for(MeshDrawData drawData : drawMap.values()) {
            if(drawData.getMesh().isVisibleFrustum(drawData.getElementCollection().getSegmentController().getWorldTransformOnClient())) {
                elementCollectionShader.setShaderInterface(this);
                elementCollectionShader.load();
                GlUtil.glEnable(3042);
                GlUtil.glBlendFunc(770, 771);
                GlUtil.glBlendFuncSeparate(770, 771, 1, 771);
                GlUtil.glPushMatrix();
                GlUtil.glMultMatrix(drawData.getMeshTransform());
                GlUtil.scaleModelview(drawData.getMeshScale(), drawData.getMeshScale(), drawData.getMeshScale());
                drawData.getMesh().markDraw();
                drawData.getMesh().draw();
                GlUtil.glPopMatrix();
                GlUtil.glDisable(3042);
                elementCollectionShader.unload();
            }
        }
    }

    @Override
    public void cleanUp() {
        for(MeshDrawData drawData : drawMap.values()) drawData.getMesh().clear();
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public void onExit() {

    }

    @Override
    public void updateShader(DrawableScene drawableScene) {

    }

    @Override
    public void updateShaderParameters(Shader shader) {
        if(shader.recompiled) {
            GlUtil.updateShaderCubeNormalsBiNormalsAndTangentsBoolean(shader);
            FloatBuffer buffer = GlUtil.getDynamicByteBuffer(72, 0).asFloatBuffer();
            buffer.rewind();

            for(int i = 0; i < CubeMeshQuadsShader13.quadPosMark.length; ++ i) {
                buffer.put(CubeMeshQuadsShader13.quadPosMark[i].x);
                buffer.put(CubeMeshQuadsShader13.quadPosMark[i].y);
                buffer.put(CubeMeshQuadsShader13.quadPosMark[i].z);
            }

            buffer.rewind();
            GlUtil.updateShaderFloats3(shader, "quadPosMark", buffer);
            shader.recompiled = false;
        }
    }

    public HashMap<ElementCollection<?, ?, ?>, MeshDrawData> getDrawMap() {
        return drawMap;
    }
}
