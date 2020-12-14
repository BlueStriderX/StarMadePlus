package net.dovtech.starmadeplus.data.physics.projectile;

import api.common.GameClient;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.elements.FireingUnit;
import org.schema.game.common.data.physics.*;
import org.schema.game.common.data.world.Sector;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.network.StateInterface;
import javax.vecmath.Vector3f;
import java.util.Arrays;
import java.util.List;

public abstract class WeaponProjectile {

    private StateInterface state;
    public SegmentController owner;
    public FireingUnit unit;
    private int sectorId;
    public CubeRayCastResult rayCast = new CubeRayCastResult(new Vector3f(), new Vector3f(), null);
    private boolean hitMarker = false;
    private List<SegmentController> hitCon = new ObjectArrayList<SegmentController>();
    private Int2ObjectOpenHashMap<SegmentController[]> arrayBuffer = new Int2ObjectOpenHashMap<SegmentController[]>();
    private Vector3f position;

    {
        arrayBuffer.put(0, new SegmentController[0]);
        arrayBuffer.put(1, new SegmentController[1]);
        arrayBuffer.put(2, new SegmentController[2]);
        arrayBuffer.put(3, new SegmentController[3]);
    }

    public WeaponProjectile(FireingUnit unit) {
        this.state = StarLoader.getGameState().getState();
        this.unit = unit;
        this.owner = unit.getSegmentController();
        this.sectorId = owner.getSectorId();
    }

    public PhysicsExt getPhysics() {
        if (state instanceof GameServerState) {
            Sector sector = ((GameServerState) state).getUniverse().getSector(sectorId);
            if (sector == null) {
                return null;
            }
            return (PhysicsExt) sector.getPhysics();
        } else {
            return (PhysicsExt) ((GameClientState) state).getPhysics();
        }
    }

    public boolean checkCollision(Damager owner, Vector3f newPos, boolean ignoreDebris) {
        hitMarker = false;
        hitCon.clear();
        do {
            SegmentController[] filter = arrayBuffer.get(hitCon.size());
            if (filter == null) {
                filter = new SegmentController[hitCon.size()];
                arrayBuffer.put(filter.length, filter);
            }
            for (int i = 0; i < filter.length; i++) {
                filter[i] = hitCon.get(i);
            }
            if (checkCollision(owner, newPos, ignoreDebris, filter)) {
                Arrays.fill(filter, null);
                return true;
            }
            Arrays.fill(filter, null);

        } while (hitMarker);

        hitCon.clear();
        return false;
    }

    public boolean checkCollision(Damager owner, Vector3f newPos, boolean ignoreDebris, SegmentController[] filter) {
        rayCast.closestHitFraction = 1f;
        rayCast.collisionObject = null;
        rayCast.setSegment(null);
        rayCast.rayFromWorld.set(position);
        rayCast.rayToWorld.set(newPos);
        rayCast.filterModeSingleNot = true;
        rayCast.setFilter(filter);
        rayCast.setOwner(owner);
        rayCast.setIgnoereNotPhysical(false);
        rayCast.setIgnoreDebris(ignoreDebris);
        rayCast.setRecordAllBlocks(false);
        rayCast.setZeroHpPhysical(true);
        rayCast.setDamageTest(true);
        rayCast.setCheckStabilizerPaths(true);

        ((ModifiedDynamicsWorld) getPhysics().getDynamicsWorld()).rayTest(position, newPos, rayCast);
        Vector3f dir = new Vector3f();
        dir.sub(position, newPos);
        dir.normalize();
        if (rayCast.hasHit()) {
            if (rayCast.collisionObject instanceof CollisionObjectInterface) {
                final CollisionType type = ((CollisionObjectInterface) rayCast.collisionObject).getType();
                hitMarker = type == CollisionType.CUBE_STRUCTURE && rayCast.getSegment() != null;
                if (hitMarker) {
                    hitCon.add(rayCast.getSegment().getSegmentController());
                }
            } else {
                assert (false) : rayCast.collisionObject;
            }

        } else {
            hitMarker = false;
        }

        return false;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public boolean isShootButtonDown() {
        return GameClient.getClientController().isMouseButtonDown(MouseEvent.ShootButton.PRIMARY_FIRE.getButton());
    }

    public abstract boolean isFired();

    public abstract void handleCollision();
}
