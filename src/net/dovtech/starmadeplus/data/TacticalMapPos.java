package net.dovtech.starmadeplus.data;

import com.bulletphysics.linearmath.Transform;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.data.drawer.TacticalMapDrawer;
import org.schema.common.util.linAlg.Vector3fTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gamemap.GameMapDrawer;
import org.schema.game.common.data.world.VoidSystem;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.graphicsengine.core.GLFW;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.Transformable;
import org.schema.schine.input.Keyboard;
import org.schema.schine.network.objects.container.TransformTimed;
import javax.vecmath.Vector3f;

public class TacticalMapPos implements Transformable {

    private TacticalMapDrawer tacticalMapDrawer;
    private final TransformTimed transform;
    private final Vector3f oldPosition = new Vector3f();
    private final Vector3f newPosition = new Vector3f();
    private final Vector3f tmp = new Vector3f();
    private final Vector3i tmpSysPosA = new Vector3i();

    private Vector3f secPos = new Vector3f();
    private Vector3i selectedSector;

    private Vector3f originPos;
    private Vector3i originSector;

    private final int maxViewRange = StarMadePlus.getInstance().tacticalMapMaxViewDistance;

    public TacticalMapPos(GameClientState state, TacticalMapDrawer tacticalMapDrawer) {
        this.tacticalMapDrawer = tacticalMapDrawer;
        this.transform = new TransformTimed();
        this.transform.setIdentity();
        this.selectedSector = state.getPlayer().getCurrentSector();
        Transform tempTransform = new Transform();
        state.getPlayer().getWordTransform(tempTransform);
        this.originPos = tempTransform.origin;
    }

    public void add(float xPos, float yPos, float zPos, float offset, boolean immediate) {
        if (!Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            Vector3i newPos = selectedSector;
            newPos.add(new Vector3i(xPos, yPos, zPos));
            if(Math.abs(Vector3i.getDisatance(newPos, originSector)) <= maxViewRange) {
                selectedSector.add(newPos);
            }
        } else {
            Vector3f newPos = secPos;
            newPos.add(new Vector3f(xPos, yPos, zPos));
            if(newPos.length() <= GameServerState.instance.getSectorSize()) {
                secPos.add(newPos);
            }
        }
        apply(immediate);
    }

    private void apply(boolean immediate) {

        float x = (secPos.x);
        float y = (secPos.y);
        float z = (secPos.z);

        newPosition.set(
                (x - 7.5f) * (GameMapDrawer.size / VoidSystem.SYSTEM_SIZEf),
                (y - 7.5f) * (GameMapDrawer.size / VoidSystem.SYSTEM_SIZEf),
                (z - 7.5f) * (GameMapDrawer.size / VoidSystem.SYSTEM_SIZEf)
        );
        if (immediate) {
            getWorldTransform().origin.set(newPosition);
            oldPosition.set(newPosition);
        }
        //currentSysPos = VoidSystem.getPosFromSector(secPos, tmpSysPosA);
        //gameMapDrawer.checkSystem(secPos);
    }

    public Vector3i get(Vector3f out) {
        out.set(secPos);
        return null;
    }

    public Vector3i getCurrentSysPos() {
        return null;
    }

    @Override
    public TransformTimed getWorldTransform() {
        return transform;
    }

    public boolean isActive(Vector3i sysPosToCheck) {
        return false;
    }

    public void set(int xSec, int ySec, int zSec, boolean immediate) {
        secPos.set(xSec, ySec, zSec);

        apply(immediate);

    }

    public void update(Timer timer) {

        if (!oldPosition.epsilonEquals(newPosition, 0.001f)) {
            tmp.sub(newPosition, oldPosition);
            float len = tmp.length();
            tmp.normalize();
            tmp.scale(timer.getDelta() * 10f);
            if (len > 1) {
                tmp.scale(len);
            }
            if (len < tmp.length()) {
                oldPosition.set(newPosition);
            } else {
                oldPosition.add(tmp);
            }

        } else {
            oldPosition.set(newPosition);
        }

        getWorldTransform().origin.set(oldPosition);
    }
}
