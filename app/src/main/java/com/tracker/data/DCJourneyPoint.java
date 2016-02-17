package com.tracker.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class for representing a DCJourneyPoint
 * @author Muhammad Azeem Anwar
 *
 */

public class DCJourneyPoint implements Parcelable 
{
	private long id;
	private long journeyID;  // ID of the journey it belongs to
	
	private double lat;
	private double lng;
	private String postalCode = "Getting address...";
	private String locality = "-";
	private boolean hasAddress;
	
	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeLong(id);
		dest.writeDouble(lat);
		dest.writeDouble(lng);
		dest.writeString(locality);
		dest.writeString(postalCode);
		dest.writeByte((byte) (hasAddress ? 0x01 : 0x00));
	}

	public static final Creator<DCJourneyPoint> CREATOR = new Creator<DCJourneyPoint>()
	{
		public DCJourneyPoint createFromParcel(Parcel in) 
		{
			DCJourneyPoint mJourneyP = new DCJourneyPoint();
			mJourneyP.id = in.readLong();
			mJourneyP.lat = in.readDouble();
			mJourneyP.lng = in.readDouble();
			mJourneyP.locality = in.readString();
			mJourneyP.postalCode = in.readString();
			mJourneyP.hasAddress = in.readByte() != 0x00;
			return mJourneyP;
		}

		public DCJourneyPoint[] newArray(int size) {
			return new DCJourneyPoint[size];
		}
	};
	
	
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getJourneyID() {
		return journeyID;
	}

	public void setJourneyID(long journeyID) {
		this.journeyID = journeyID;
	}

	public boolean isHasAddress() {
		return hasAddress;
	}

	public void setHasAddress(boolean hasAddress) {
		this.hasAddress = hasAddress;
	}	
}
