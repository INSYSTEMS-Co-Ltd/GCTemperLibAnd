
package com.greencross.gctemperlib.greencare.charting.data;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;

import com.greencross.gctemperlib.greencare.charting.utils.Utils;


/**
 * Class representing one entry in the chart. Might contain multiple values.
 * Might only contain a single value depending on the used constructor.
 * 
 * @author Philipp Jahoda
 */
public class CEntry extends BaseEntry implements Parcelable {

    /** the x value */
    private float x = 0f;

    public CEntry() {

    }

    /**
     * A CEntry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     */
    public CEntry(float x, float y) {
        super(y);
        this.x = x;
    }

    /**
     * A CEntry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     * @param data Spot for additional data this CEntry represents.
     */
    public CEntry(float x, float y, Object data) {
        super(y, data);
        this.x = x;
    }

    /**
     * A CEntry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     * @param icon icon image
     */
    public CEntry(float x, float y, Drawable icon) {
        super(y, icon);
        this.x = x;
    }

    /**
     * A CEntry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     * @param icon icon image
     * @param data Spot for additional data this CEntry represents.
     */
    public CEntry(float x, float y, Drawable icon, Object data) {
        super(y, icon, data);
        this.x = x;
    }

    /**
     * Returns the x-value of this CEntry object.
     * 
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x-value of this CEntry object.
     * 
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * returns an exact copy of the entry
     * 
     * @return
     */
    public CEntry copy() {
        CEntry e = new CEntry(x, getY(), getData());
        return e;
    }

    /**
     * Compares value, xIndex and data of the entries. Returns true if entries
     * are equal in those points, false if not. Does not check by hash-code like
     * it's done by the "equals" method.
     * 
     * @param e
     * @return
     */
    public boolean equalTo(CEntry e) {

        if (e == null)
            return false;

        if (e.getData() != this.getData())
            return false;

        if (Math.abs(e.x - this.x) > Utils.FLOAT_EPSILON)
            return false;

        if (Math.abs(e.getY() - this.getY()) > Utils.FLOAT_EPSILON)
            return false;

        return true;
    }

    /**
     * returns a string representation of the entry containing x-index and value
     */
    @Override
    public String toString() {
        return "CEntry, x: " + x + " y: " + getY();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.x);
        dest.writeFloat(this.getY());
        if (getData() != null) {
            if (getData() instanceof Parcelable) {
                dest.writeInt(1);
                dest.writeParcelable((Parcelable) this.getData(), flags);
            } else {
                throw new ParcelFormatException("Cannot parcel an CEntry with non-parcelable data");
            }
        } else {
            dest.writeInt(0);
        }
    }

    protected CEntry(Parcel in) {
        this.x = in.readFloat();
        this.setY(in.readFloat());
        if (in.readInt() == 1) {
            this.setData(in.readParcelable(Object.class.getClassLoader()));
        }
    }

    public static final Creator<CEntry> CREATOR = new Creator<CEntry>() {
        public CEntry createFromParcel(Parcel source) {
            return new CEntry(source);
        }

        public CEntry[] newArray(int size) {
            return new CEntry[size];
        }
    };
}
