package net.dovtech.starmadeplus.data.drawer;

import api.common.GameClient;
import com.bulletphysics.linearmath.Transform;
import net.dovtech.starmadeplus.data.map.TacticalMapPos;
import net.dovtech.starmadeplus.data.camera.TacticalViewCamera;
import org.schema.game.client.data.GameClientState;
import org.schema.schine.common.InputHandler;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.input.KeyEventInterface;
import org.schema.schine.input.KeyboardMappings;

import javax.vecmath.Vector3f;

public class TacticalMapDrawer implements Drawable, InputHandler {

    private GameClientState state;
    private boolean active;
    private TacticalViewCamera camera;
    private TacticalMapPos pos;

    public TacticalMapDrawer(GameClientState state) {
        this.state = state;
        this.pos = new TacticalMapPos(state, this);
        this.active = false;
    }

    @Override
    public void handleMouseEvent(MouseEvent mouseEvent) {

    }

    @Override
    public void cleanUp() {

    }

    @Override
    public void draw() {

    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public void onInit() {
        camera = new TacticalViewCamera(state, pos);
        camera.setCameraStartOffset(88.487305f);
        camera.setCameraOffset(88.487305f);
        camera.alwaysAllowWheelZoom = true;
    }

    public boolean doDraw(){
        return active;
    }

    private void move(int x, int y, int z) {
        Vector3f dir = new Vector3f();
        int m = 0;
        if (z != 0) {
            m = z;
            z = 0;
            GlUtil.getForwardVector(dir, camera.getWorldTransform());
        }
        if (y != 0) {
            m = y;
            y = 0;
            GlUtil.getUpVector(dir, camera.getWorldTransform());
        }
        if (x != 0) {
            m = x;
            x = 0;
            GlUtil.getRightVector(dir, camera.getWorldTransform());
        }

        if (Math.abs(dir.x) >= Math.abs(dir.y) && Math.abs(dir.x) >= Math.abs(dir.z)) {
            if (dir.x >= 0) {
                x = m;
            } else {
                x = -m;
            }
        } else if (Math.abs(dir.y) >= Math.abs(dir.x) && Math.abs(dir.y) >= Math.abs(dir.z)) {
            if (dir.y >= 0) {
                y = m;
            } else {
                y = -m;
            }

        } else if (Math.abs(dir.z) >= Math.abs(dir.y) && Math.abs(dir.z) >= Math.abs(dir.x)) {
            if (dir.z >= 0) {
                z = m;
            } else {
                z = -m;
            }

        }
        pos.add(x, y, z, camera.getCameraOffset(), false);
    }

    /*
    public void resetToCurrentPos() {
        pos.set(getPlayerPos().x, getPlayerPos().y, getPlayerPos().z, true);
    }
     */

    private Vector3f getPlayerPos() {
        Transform tempTransform = new Transform();
        GameClient.getClientPlayerState().getWordTransform(tempTransform);
        return tempTransform.origin;
    }

    @Override
    public void handleKeyEvent(KeyEventInterface event) {
        if (KeyboardMappings.getEventKeyState(event, this.state)) {

            if (KeyboardMappings.getEventKeySingle(event) == KeyboardMappings.FORWARD.getMapping()) {
                this.move(0, 0, 1);
            }

            if (KeyboardMappings.getEventKeySingle(event) == KeyboardMappings.BACKWARDS.getMapping()) {
                this.move(0, 0, -1);
            }

            if (KeyboardMappings.getEventKeySingle(event) == KeyboardMappings.STRAFE_RIGHT.getMapping()) {
                this.move(-1, 0, 0);
            }

            if (KeyboardMappings.getEventKeySingle(event) == KeyboardMappings.STRAFE_LEFT.getMapping()) {
                this.move(1, 0, 0);
            }

            if (KeyboardMappings.getEventKeySingle(event) == KeyboardMappings.UP.getMapping()) {
                this.move(0, 1, 0);
            }

            if (KeyboardMappings.getEventKeySingle(event) == KeyboardMappings.DOWN.getMapping()) {
                this.move(0, -1, 0);
            }
        }

    }
}
