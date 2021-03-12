package thederpgamer.starmadeplus.data.camera;

import com.bulletphysics.linearmath.Transform;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.view.camera.ShipOffsetCameraViewable;
import org.schema.game.client.view.camera.UpperFixedViewer;
import org.schema.schine.graphicsengine.camera.Camera;
import org.schema.schine.graphicsengine.camera.look.AxialCameraLook;
import org.schema.schine.graphicsengine.camera.viewer.FixedViewer;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.Transformable;
import org.schema.schine.network.StateInterface;

public class TacticalViewCamera extends Camera {

    private Transform transform;

    public TacticalViewCamera(StateInterface stateInterface, Transformable transformable) {
        super(stateInterface, new UpperFixedViewer(transformable));
        this.setFocus(transformable);
        this.transform = new Transform();
        this.transform.setIdentity();
        setLookAlgorithm(new AxialCameraLook(this));
    }

    public void setFocus(Transformable transformable) {
        ((FixedViewer) getViewable()).setEntity(transformable);
    }

    public void jumpTo(Vector3i pos) {
        ((ShipOffsetCameraViewable) getViewable()).jumpTo(pos);
    }


    @Override
    public void update(Timer timer, boolean server) {
        ((AxialCameraLook) getLookAlgorithm()).getFollowing().set(transform);
        super.update(timer, server);

    }
}
