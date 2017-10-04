package com.bignerdranch.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.bignerdranch.android.criminalintent.Crime;
import com.bignerdranch.android.criminalintent.Photo;
import java.util.Date;
import java.util.UUID;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.PhotoTable;

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);

        return crime;
    }

    public Photo getPhoto() {
        String uuidString = getString(getColumnIndex(PhotoTable.Cols.UUID));
        String crimeId = getString(getColumnIndex(PhotoTable.Cols.CRIMEID));
        byte[] image = getBlob(getColumnIndex(PhotoTable.Cols.IMAGE));

        Photo photo = new Photo(UUID.fromString(uuidString));
        photo.setCrimeId(crimeId);
        photo.setImage(image);

        return photo;
    }
}
