package cn.thinker.wechatmomentsdemo.common;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelablePoolObject implements Parcelable, IPoolObject{
    private Bundle mInternalParcel = new Bundle();

    @Override
    public void initializePoolObject() {
    }

    @Override
    public void finalizePoolObject() {
        mInternalParcel.clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(mInternalParcel);
    }

    public static final Creator<ParcelablePoolObject> CREATOR = new Creator<ParcelablePoolObject>() {

        @Override
        public ParcelablePoolObject createFromParcel(Parcel source) {
            ParcelablePoolObject ob = new ParcelablePoolObject();
            ob.mInternalParcel = source.readBundle();
            return ob;
        }

        @Override
        public ParcelablePoolObject[] newArray(int size) {
            return new ParcelablePoolObject[size];
        }
    };

    public final Bundle getData() {
        return mInternalParcel;
    }
}
