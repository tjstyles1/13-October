package com.rittie.andy.testing;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by James on 4/10/2015.
 * Last Edited on 4/10/2015.
 */
public class Baseline implements Parcelable {
    private long id;
    private String baselineName;
    private long userIDFK;
    private double avgHR;

    public Baseline(long bID, String baselineName, long userIDFK) {
        this.id = bID;
        this.baselineName = baselineName;
        this.userIDFK = userIDFK;
        this.avgHR = 0;
    }

    public double calcAvg(double[] heartRates) {
        double sum = 0;
        for (int i = 0; i < heartRates.length; i++)
            sum = sum + heartRates[i];

        //calculate average value
        avgHR = sum / heartRates.length;
        return this.avgHR;
    }

    public double getAvgHR() {
        return avgHR;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return baselineName;
    }

    public long getUserIDFK() {
        return userIDFK;
    }


    @Override
    public String toString() {
        return this.baselineName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeLong(id);
        pc.writeString(baselineName);
        pc.writeLong(userIDFK);
        pc.writeDouble(avgHR);
    }

    public static final Parcelable.Creator<Baseline> CREATOR = new Parcelable.Creator<Baseline>() {
        public Baseline createFromParcel (Parcel pc) {
            return new Baseline(pc);
        }
        public Baseline[] newArray(int size) {
            return new Baseline[size];
        }
    };

    public Baseline(Parcel pc){
        id = pc.readLong();
        baselineName = pc.readString();
        userIDFK = pc.readLong();
        avgHR= pc.readDouble();
    }
}