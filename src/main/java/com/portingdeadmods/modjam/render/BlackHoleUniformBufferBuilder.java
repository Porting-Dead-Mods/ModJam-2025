package com.portingdeadmods.modjam.render;

import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackHoleUniformBufferBuilder {


    private static class BlackHoleDefinition
    {
        private static final int FLOAT_SIZE = 4;
        private static final int INT_SIZE = 4;
        private static final int SIZE_IN_BYTES = FLOAT_SIZE * 5 + INT_SIZE;

        Vector3f position;
        double eventHorizonRadius;
       // double spin;
        float discDensity;
        float diskOpacity;
        float diskTemperature;
        float innerDiskRadius;
        float outerDiskRadius;
        int  discParticles; // i have no idea what this means

    }

    private ArrayList<BlackHoleDefinition> blackholes;
    private Vector3f latestBlackHolePosition = null;

    public BlackHoleUniformBufferBuilder()
    {
        this.blackholes = new ArrayList<BlackHoleDefinition>(8);
    }
    
    public Vector3f getLatestBlackHolePosition() {
        return latestBlackHolePosition;
    }

    public ByteBuffer build()
    {
        //cache buffer?
        ByteBuffer buffer = ByteBuffer.allocateDirect(blackholes.size() * BlackHoleDefinition.SIZE_IN_BYTES);
        for (BlackHoleDefinition hole : blackholes)
        {
            buffer.putFloat(hole.position.x);
            buffer.putFloat(hole.position.y);
            buffer.putFloat(hole.position.z);
            buffer.putFloat(calculateMass(hole.eventHorizonRadius));
            buffer.putFloat(hole.discDensity);
            buffer.putFloat(hole.diskOpacity);
            buffer.putFloat(hole.diskTemperature);
            buffer.putFloat(hole.innerDiskRadius);
            buffer.putFloat(hole.outerDiskRadius);
            buffer.putInt(hole.discParticles);
        }

        return buffer;
    }

    public void blackhole(Vector3f position, float radius, float acreationDiskRadius)
    {
        latestBlackHolePosition = new Vector3f(position);
    }

    public void restart()
    {
        this.blackholes.clear();
        this.latestBlackHolePosition = null;
    }
    
    public void clearBlackHolePosition() {
        this.latestBlackHolePosition = null;
    }

    private float calculateMass(double eventHorizon)
    {
        return 1;
    }


}
