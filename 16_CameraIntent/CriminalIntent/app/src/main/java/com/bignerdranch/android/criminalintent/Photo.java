package com.bignerdranch.android.criminalintent;

import java.util.UUID;

public class Photo {

    private UUID mId;
    private String mCrimeId;
    private byte[] mImage;

    public Photo() {
        this(UUID.randomUUID());
    }

    public Photo(UUID id) {
        mId = id;
    }
    public UUID getId() {
        return mId;
    }

    public String getCrimeId() {
        return mCrimeId;
    }

    public void setCrimeId(String crimeId) {
        mCrimeId = crimeId;
    }

    public byte[] getImage() {
        return mImage;
    }

    public void setImage(byte[] image) {
        mImage = image;
    }
}
