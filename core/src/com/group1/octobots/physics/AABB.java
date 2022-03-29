package com.group1.octobots.physics;

import com.badlogic.gdx.math.MathUtils;
import com.group1.octobots.AI.Direction;
import com.group1.octobots.AI.State;
import com.group1.octobots.Module;
import org.jetbrains.annotations.NotNull;
//import com.sun.istack.internal.NotNull;
import java.util.ArrayList;

import static com.group1.octobots.utility.Constants.Physics.MAP_SIZE;
import static com.group1.octobots.utility.Constants.Physics.MOD_SIDE_AREA;
import static com.group1.octobots.utility.Constants.Physics.OBJECT_SIZE;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Simple AABB for collision detection.
 */
public class AABB {

    private WorldObject object;
    private final V3 minExtent;
    private final V3 maxExtent;

    public AABB() {
        minExtent = new V3();
        maxExtent = new V3();
    }

    public AABB(V3 minExtent, V3 maxExtent) {
        this.minExtent = minExtent;
        this.maxExtent = maxExtent;
    }

    public AABB(AABB other, V3 offset) {
        minExtent = other.minExtent.cpy().add(offset);
        maxExtent = other.maxExtent.cpy().add(offset);
    }

    public AABB(@NotNull WorldObject object) {
        this.object = object;
        minExtent = new V3();
        maxExtent = new V3();
        update();
    }

    public V3 getMin() {
        return minExtent;
    }

    public V3 getMax() {
        return maxExtent;
    }

    public void update() {
        if (object == null) throw new NullPointerException("AABB was not initialized with a target, update manually.");
        minExtent.set(object.pos());
        maxExtent.set(minExtent).add(OBJECT_SIZE);
    }

    public boolean intersects(AABB that) {
        return (this.minExtent.x < that.maxExtent.x && this.maxExtent.x > that.minExtent.x) &&
                (this.minExtent.y < that.maxExtent.y && this.maxExtent.y > that.minExtent.y) &&
                 (this.minExtent.z < that.maxExtent.z && this.maxExtent.z > that.minExtent.z);
    }

    public IntersectData collide(AABB other) {
        return new IntersectData(
                other.getMin().cpy().subtract(maxExtent),
                minExtent.cpy().subtract(other.getMax())
        );
    }

    public void combine(AABB other) {
        minExtent.x = (minExtent.x < other.minExtent.x) ? minExtent.x : other.minExtent.x;
        minExtent.y = (minExtent.y < other.minExtent.y) ? minExtent.y : other.minExtent.y;
        minExtent.z = (minExtent.z < other.minExtent.z) ? minExtent.z : other.minExtent.z;
        maxExtent.x = (maxExtent.x > other.maxExtent.x) ? maxExtent.x : other.maxExtent.x;
        maxExtent.y = (maxExtent.y > other.maxExtent.y) ? maxExtent.y : other.maxExtent.y;
        maxExtent.z = (maxExtent.z > other.maxExtent.z) ? maxExtent.z : other.maxExtent.z;
    }

    public void combine(AABB b1, AABB b2) {
        minExtent.set(min(b1.minExtent.x, b2.minExtent.x),
                      min(b1.minExtent.y, b2.minExtent.y),
                      min(b1.minExtent.z, b2.minExtent.z));
        maxExtent.set(max(b1.minExtent.x, b2.maxExtent.x),
                      max(b1.maxExtent.y, b2.maxExtent.y),
                      max(b1.maxExtent.z, b2.maxExtent.z));
    }




    /* JACKY REGION */
    //Returns the distance between two modules, if they intersect, it returns the intersection in negative value.
    public float xDistance(AABB that){
        if (that.minExtent.x > this.maxExtent.x){
            return that.minExtent.x - this.maxExtent.x ;
        } else {
            if(this.minExtent.x > that.maxExtent.x){
                return this.minExtent.x - that.maxExtent.x;
            } else {
                return -xIntersection(that);
            }
        }
    }
    public static float xDistance(AABB thiz , AABB that){
        if (that.minExtent.x > thiz.maxExtent.x){
            return that.minExtent.x - thiz.maxExtent.x ;
        } else {
            if(thiz.minExtent.x > that.maxExtent.x){
                return thiz.minExtent.x - that.maxExtent.x;
            } else {
                return -xIntersection(thiz,that);
            }
        }
    }
    public float yDistance(AABB that){
        if (that.minExtent.y > this.maxExtent.y){
            return that.minExtent.y - this.maxExtent.y ;
        } else {
            if(this.minExtent.y > that.maxExtent.y){
                return this.minExtent.y - that.maxExtent.y;
            } else {
                return -yIntersection(that);
            }
        }
    }
    public static float yDistance(AABB thiz , AABB that){
        if (that.minExtent.y > thiz.maxExtent.y){
            return that.minExtent.y - thiz.maxExtent.y ;
        } else {
            if(thiz.minExtent.y > that.maxExtent.y){
                return thiz.minExtent.y - that.maxExtent.y;
            } else {
                return -yIntersection(thiz,that);
            }
        }
    }
    public float zDistance(AABB that){
        if (that.minExtent.z > this.maxExtent.z){
            return that.minExtent.x - this.maxExtent.x ;
        } else {
            if(this.minExtent.z > that.maxExtent.z){
                return this.minExtent.z - that.maxExtent.z;
            } else {
                return -zIntersection(that);
            }
        }
    }

    boolean tipY;
    boolean tipNEG_Y;

    //Returns the length of intersection on the y axis in percentage
    public float yIntersection(AABB that) {
        // FIXME: 09.05.2017 acmommentdmsak
        if (this.minExtent.y < that.maxExtent.y && this.maxExtent.y > that.minExtent.y){
            if(that.maxExtent.y - this.minExtent.y > this.maxExtent.y - that.minExtent.y) {
                tipY = true;
                return 100 * this.maxExtent.y - that.minExtent.y / OBJECT_SIZE;
            } else {
                tipNEG_Y = true;
                return 100 * that.maxExtent.y - this.minExtent.y / OBJECT_SIZE;
            }
        }
        return -1f;
    }
    public static float yIntersection(AABB thiz,AABB that) {
        // FIXME: 09.05.2017 acmommentdmsak
        if (thiz.minExtent.y < that.maxExtent.y && thiz.maxExtent.y > that.minExtent.y){
            return 100 * min(that.maxExtent.y - thiz.minExtent.y,
                    thiz.maxExtent.y - that.minExtent.y) / OBJECT_SIZE;
        }
        return -1f;
    }
    //Returns the length of intersection on the x axis in percentage
    public float xIntersection(AABB that) {
        // FIXME: 09.05.2017 acmommentdmsak
        if (this.minExtent.x < that.maxExtent.x && this.maxExtent.x > that.minExtent.x){
            return 100 * min(that.maxExtent.x - this.minExtent.x,
                    this.maxExtent.x - that.minExtent.x) / OBJECT_SIZE;
        }
        return -1f;
    }
    public static float xIntersection(AABB thiz,AABB that) {
        // FIXME: 09.05.2017 acmommentdmsak
        if (thiz.minExtent.x < that.maxExtent.x && thiz.maxExtent.x > that.minExtent.x){
            return 100 * min(that.maxExtent.x - thiz.minExtent.x,
                    thiz.maxExtent.x - that.minExtent.x) / OBJECT_SIZE;
        }
        return -1f;
    }
    //Returns the length of intersection on the z axis in percentage
    public float zIntersection(AABB that) {
        // FIXME: 09.05.2017 acmommentdmsak
        if (this.minExtent.z < that.maxExtent.z && this.maxExtent.z > that.minExtent.z){
            return 100 * min(that.maxExtent.z - this.minExtent.z,
                    this.maxExtent.z - that.minExtent.z) / OBJECT_SIZE;
        }
        return -1f;
    }



    //returns the area covered in percentage
    public float sitsOn(AABB that) { //FIXME maybe turn into double if problem arises
        if (xIntersection(that) > 0f && yIntersection(that) > 0f) { //FIXME >= instead of > ???????
            return 100*xIntersection(that)*yIntersection(that)/ MOD_SIDE_AREA;
        } else
            return 0f;
    }

    //ONLY USE FOR mods WHICH ARE EITHER ALIGNED WITH module IN THE X- OR Y-AXIS
    public Module nearestMod(Module module , ArrayList<Module> mods){
        Module nearMod = null;
        float distance = OBJECT_SIZE;
        float minD;
        for (Module module1 : mods) {
            minD = max(xDistance(module.body().aabb(),module1.body().aabb()),yDistance(module.body().aabb(),module1.body().aabb())); //If intersection in X-Axis, we calculate the nearest module in the Y-Axis and vice versa.
            if(minD < distance){
                distance = minD;
                nearMod = module1;
            }
        }
        return nearMod;
    }

    public boolean intersectsStrict(AABB that) {
        return (this.minExtent.x < that.maxExtent.x && this.maxExtent.x > that.minExtent.x) &&
                (this.minExtent.y < that.maxExtent.y && this.maxExtent.y > that.minExtent.y) &&
                 (this.minExtent.z < that.maxExtent.z && this.maxExtent.z > that.minExtent.z);
    }

    //Returns true if at least one module in mod1 is touching another module in mod2
    /*public boolean isTouching(List<Module> mod1 , List<Module> mod2 , V3 direction){
        if (direction == V3.X || direction == V3.NEG_X){
            for (Module module1 : mod1 ) {
                for ( Module module2: mod2 ) {
                    if (MathUtils.isZero(module1.body().aabb().collide(module2.body().aabb()).xDist())){
                        return true;
                    }
                }
            }
        }

        if (direction == V3.Y || direction == V3.NEG_Y){
            for (Module module1 : mod1 ) {
                for ( Module module2: mod2 ) {
                    if (MathUtils.isZero(module1.body().aabb().collide(module2.body().aabb()).yDist())){
                        return true;
                    }
                }
            }

        }
        return false;
    }*/

    /* JACKY REGION END */


    public static class IntersectData {
        V3 thatMinMinusThisMax;
        V3 thisMinMinusThatMax;

        private IntersectData(V3 thatMinMinusThisMax, V3 thisMinMinusThatMax) {
            this.thatMinMinusThisMax = thatMinMinusThisMax;
            this.thisMinMinusThatMax = thisMinMinusThatMax;
        }

        public float distOn(Direction d) {
            switch (d) {
                case Front:
                case Back:
                    return xDist();
                case Left:
                case Right:
                    return yDist();
            }
            return Float.NaN;
        }

        public float xDist() {
            return max(thatMinMinusThisMax.x, thisMinMinusThatMax.x);
        }
        public float yDist() {
            return max(thatMinMinusThisMax.y, thisMinMinusThatMax.y);
        }
        public float zDist() {
            return max(thatMinMinusThisMax.z, thisMinMinusThatMax.z);
        }

        public float alignedOnZand(Direction d) {
            return alignedZ() * alignedOn(d) / 100f;
        }

        public float alignedOn(Direction d) {
            switch (d) {
                case Front:
                case Back:
                    return alignedX();
                case Right:
                case Left:
                    return alignedY();
            }
            return Float.NaN;
        }

        public float volumeOfOverlap() {
            return alignedX() * alignedY() * alignedZ() / -1000f;
        }

        public float alignedX() {
            float f = xDist();
            return (f < 0) ? (-100 * f): 0f;
        }

        public float alignedY() {
            float f = yDist();
            return (f < 0) ? (-100 * f) : 0f;
        }

        public float alignedZ() {
            float f = zDist();
            return (f < 0) ? (-100 * f) : 0f;
        }

        public float alignedXY() {
            return alignedX() * alignedY() / 100f;
        }

        public float alignedXZ() {
            return alignedX() * alignedZ() / 100f;
        }

        public float alignedYZ() {
            return alignedY() * alignedZ() / 100f;
        }

        public float maxAxisDist() {
            return thatMinMinusThisMax.max();
        }

        public float euclidianDistance() {
            float dx = xDist();
            float dy = yDist();
            float dz = zDist();
            return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
        }
    }
}
