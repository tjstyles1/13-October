package com.rittie.andy.testing;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by James on 4/10/2015.
 * Last Edited on 4/10/2015.
 */
public class HeartRate implements Parcelable {
    private long id;
    private String startTime;
    private long baselineIDFK;
    private double heartRate;

    public HeartRate(String startTime, long baselineIDFK) {
        this.id = 0;
        this.startTime = startTime;
        this.baselineIDFK = baselineIDFK;
        this.heartRate = 0;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public long getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public long getBaselineIDFK() {
        return baselineIDFK;
    }


    @Override
    public String toString() {
        return this.startTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeLong(id);
        pc.writeString(startTime);
        pc.writeLong(baselineIDFK);
        pc.writeDouble(heartRate);
    }

    public static final Parcelable.Creator<HeartRate> CREATOR = new Parcelable.Creator<HeartRate>() {
        public HeartRate createFromParcel (Parcel pc) {
            return new HeartRate(pc);
        }
        public HeartRate[] newArray(int size) {
            return new HeartRate[size];
        }
    };

    public HeartRate(Parcel pc){
        id = pc.readLong();
        startTime = pc.readString();
        baselineIDFK = pc.readLong();
        heartRate = pc.readDouble();
    }
}