package com.tracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tracker.data.DCBehaviourPoint;
import com.tracker.data.DCJourney;
import com.tracker.data.DCJourneyPoint;
import com.tracker.data.DatabaseHelper;
import com.tracker.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



/**
 * SQL database class for the journeys.
 *
 * @author Muhammad Azeem Anwar
 */

public class JourneyDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] journeyColumns = { DatabaseHelper.JOURNEY_ID,
			DatabaseHelper.JOURNEY_DESC, DatabaseHelper.JOURNEY_CREATE_DATE,
			DatabaseHelper.JOURNEY_START_TIME, DatabaseHelper.JOURNEY_END_TIME,
			DatabaseHelper.JOURNEY_DURATION, DatabaseHelper.JOURNEY_DISTANCE,
			DatabaseHelper.JOURNEY_BUSINESS, DatabaseHelper.JOURNEY_EXPENSE,
			DatabaseHelper.JOURNEY_IS_CLAIMED,
			DatabaseHelper.JOURNEY_START_ADDR,
			DatabaseHelper.JOURNEY_START_CITY, DatabaseHelper.JOURNEY_END_ADDR,
			DatabaseHelper.JOURNEY_END_CITY,
			DatabaseHelper.JOURNEY_VEHICLE_REG,
			DatabaseHelper.JOURNEY_AVG_SPEED, DatabaseHelper.JOURNEY_MAX_SPEED,
			DatabaseHelper.JOURNEY_SCORE, DatabaseHelper.JOURNEY_SCORE_ADDED,
			DatabaseHelper.JOURNEY_VALID_BEHAVIOUR,

            DatabaseHelper.JOURNEY_BEHAVIOUR_ADDED,
            DatabaseHelper.JOURNEY_CALCULATED_BEHAVIOUR,
			DatabaseHelper.JOURNEY_EMISSION };

	private String[] journeyPointColumns = { DatabaseHelper.JOURNEY_POINT_ID,
			DatabaseHelper.JOURNEY_POINT_JOURNEY_ID,
			DatabaseHelper.JOURNEY_POINT_LATITUDE,
			DatabaseHelper.JOURNEY_POINT_LONGITUDE };

	private String[] behaviourPointColumns = { DatabaseHelper.BEHAVIOUR_ID,
			DatabaseHelper.BEHAVIOUR_JOURNEY_ID,
			DatabaseHelper.BEHAVIOUR_ACCELERATION_X,
			DatabaseHelper.BEHAVIOUR_ACCELERATION_Y,
			DatabaseHelper.BEHAVIOUR_ACCELERATION_Z,
            DatabaseHelper.BEHAVIOUR_GRAVITY_X,
            DatabaseHelper.BEHAVIOUR_GRAVITY_Y,
            DatabaseHelper.BEHAVIOUR_GRAVITY_Z,
            DatabaseHelper.BEHAVIOUR_FUNCTION_EXECUTION_TIME,
            DatabaseHelper.BEHAVIOUR_INDEX,
			DatabaseHelper.BEHAVIOUR_POINT_TIME_DIFFERENCE,
            DatabaseHelper.BEHAVIOUR_ACTIVITY, DatabaseHelper.BEHAVIOUR_SPEED,
			DatabaseHelper.BEHAVIOUR_FLAT, DatabaseHelper.BEHAVIOUR_LANDSCAPE,
			DatabaseHelper.BEHAVIOUR_PORTRAIT };

	public JourneyDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	// DC Journey
	// -----------------
	/**
	 * Creates DCJourney object in the database
	 * 
	 * @param journey
	 * @param points
	 * @return id of the created journey
	 */
	public long createJourney(DCJourney journey,
			ArrayList<DCJourneyPoint> points) {
		ContentValues values = new ContentValues();
		String date = "";
		if (journey.getCreateDate() == null
				|| journey.getCreateDate().equals("")) {
			date = todaysDate();
		} else {
			date = todaysDate(journey.getStartTime());
		}

		// Create journey
		values.put(DatabaseHelper.JOURNEY_DESC, journey.getDescription());
		values.put(DatabaseHelper.JOURNEY_START_TIME, journey.getStartTime());
		values.put(DatabaseHelper.JOURNEY_END_TIME, journey.getEndTime());
		values.put(DatabaseHelper.JOURNEY_CREATE_DATE, date);
		values.put(DatabaseHelper.JOURNEY_DURATION, journey.getDuration());
		values.put(DatabaseHelper.JOURNEY_DISTANCE, journey.getDistance());
		values.put(DatabaseHelper.JOURNEY_BUSINESS, journey.isBusiness());
		values.put(DatabaseHelper.JOURNEY_EXPENSE, journey.getExpense());
		values.put(DatabaseHelper.JOURNEY_IS_CLAIMED, journey.isClaimedFor());

		values.put(DatabaseHelper.JOURNEY_START_ADDR, journey.getStartAddr());
		values.put(DatabaseHelper.JOURNEY_START_CITY, journey.getStartCity());

		values.put(DatabaseHelper.JOURNEY_END_ADDR, journey.getEndAddr());
		values.put(DatabaseHelper.JOURNEY_END_CITY, journey.getEndCity());

		values.put(DatabaseHelper.JOURNEY_VEHICLE_REG, journey.getVehicle_reg());
		values.put(DatabaseHelper.JOURNEY_AVG_SPEED, journey.getAvgSpeed());
		values.put(DatabaseHelper.JOURNEY_MAX_SPEED, journey.getTopSpeed());
		values.put(DatabaseHelper.JOURNEY_SCORE, journey.getBehaviourScore());
		values.put(DatabaseHelper.JOURNEY_SCORE_ADDED, journey.isScoreAdded());
		values.put(DatabaseHelper.JOURNEY_VALID_BEHAVIOUR,
				journey.isValidBehaviour());

        values.put(DatabaseHelper.JOURNEY_BEHAVIOUR_ADDED, journey.isBehaviourAdded());
        values.put(DatabaseHelper.JOURNEY_CALCULATED_BEHAVIOUR, journey.getBehaviourCalculatedScore());
		values.put(DatabaseHelper.JOURNEY_EMISSION, journey.getEmissions());
		Boolean sd = journey.isClaimedFor();
		long journeyID = database.insert(DatabaseHelper.JOURNEY_TABLE, null,
				values);

		// Create points
		if (points != null) {
			for (int i = 0; i < points.size(); i++) {
				values = new ContentValues();
				values.put(DatabaseHelper.JOURNEY_POINT_JOURNEY_ID, journeyID);
				values.put(DatabaseHelper.JOURNEY_POINT_LATITUDE, points.get(i)
						.getLat());
				values.put(DatabaseHelper.JOURNEY_POINT_LONGITUDE, points
						.get(i).getLng());
				database.insert(DatabaseHelper.JOURNEY_POINT_TABLE, null,
						values);
			}
		}

		Log.d("Journey Datasource","Created new journey with startdate: " + journey.getStartTime());

		return journeyID;
	}

	/**
	 * Updates given journey in the SQL database.
	 * 
	 * @param journey
	 */
	public void updateJourney(DCJourney journey) {
		String journeyCreatedDate = Utilities.convertDateFormat(
				journey.getCreateDate(), "MMM dd, yyyy", "yyyy-MM-dd");

		ContentValues values = new ContentValues();
		Boolean claim = journey.isClaimedFor();
		values.put(DatabaseHelper.JOURNEY_DESC, journey.getDescription());
		values.put(DatabaseHelper.JOURNEY_START_TIME, journey.getStartTime());
		values.put(DatabaseHelper.JOURNEY_END_TIME, journey.getEndTime());
		values.put(DatabaseHelper.JOURNEY_CREATE_DATE, journeyCreatedDate);
		values.put(DatabaseHelper.JOURNEY_DURATION, journey.getDuration());
		values.put(DatabaseHelper.JOURNEY_DISTANCE, journey.getDistance());
		values.put(DatabaseHelper.JOURNEY_BUSINESS, journey.isBusiness());
		values.put(DatabaseHelper.JOURNEY_EXPENSE, journey.getExpense());
		values.put(DatabaseHelper.JOURNEY_IS_CLAIMED, journey.isClaimedFor());
		values.put(DatabaseHelper.JOURNEY_START_ADDR, journey.getStartAddr());
		values.put(DatabaseHelper.JOURNEY_START_CITY, journey.getStartCity());

		values.put(DatabaseHelper.JOURNEY_END_ADDR, journey.getEndAddr());
		values.put(DatabaseHelper.JOURNEY_END_CITY, journey.getEndCity());

		values.put(DatabaseHelper.JOURNEY_VEHICLE_REG, journey.getVehicle_reg());
		values.put(DatabaseHelper.JOURNEY_AVG_SPEED, journey.getAvgSpeed());
		values.put(DatabaseHelper.JOURNEY_MAX_SPEED, journey.getTopSpeed());

        values.put(DatabaseHelper.JOURNEY_BEHAVIOUR_ADDED, journey.isBehaviourAdded());
        values.put(DatabaseHelper.JOURNEY_CALCULATED_BEHAVIOUR, journey.getBehaviourCalculatedScore());

		values.put(DatabaseHelper.JOURNEY_EMISSION, journey.getEmissions());

		database.update(DatabaseHelper.JOURNEY_TABLE, values,
				DatabaseHelper.JOURNEY_ID + "=" + journey.getId(), null);

		Log.d("Journey Datasource", "Updated journey with startdate: " + journey.getStartTime());
	}

	/**
	 * Updates given columns of the given journey in the SQL database.
	 * 
	 * @param journey
	 * @param column
	 */
	public void updateJourney(DCJourney journey, String column[]) {
		ContentValues values = new ContentValues();
		String journeyCreatedDate = Utilities.convertDateFormat(
				journey.getCreateDate(), "MMM dd, yyyy", "yyyy-MM-dd");

		Boolean claim = journey.isClaimedFor();
		for (int i = 0; i < column.length; i++) {
			if (column[i].equals(DatabaseHelper.JOURNEY_DESC))
				values.put(DatabaseHelper.JOURNEY_DESC,
						journey.getDescription());
			else if (column[i].equals(DatabaseHelper.JOURNEY_START_TIME))
				values.put(DatabaseHelper.JOURNEY_START_TIME,
						journey.getStartTime());
			else if (column[i].equals(DatabaseHelper.JOURNEY_END_TIME))
				values.put(DatabaseHelper.JOURNEY_END_TIME,
						journey.getEndTime());
			else if (column[i].equals(DatabaseHelper.JOURNEY_CREATE_DATE))
				values.put(DatabaseHelper.JOURNEY_CREATE_DATE,
						journeyCreatedDate);
			else if (column[i].equals(DatabaseHelper.JOURNEY_DURATION))
				values.put(DatabaseHelper.JOURNEY_DURATION,
						journey.getDuration());
			else if (column[i].equals(DatabaseHelper.JOURNEY_DISTANCE))
				values.put(DatabaseHelper.JOURNEY_DISTANCE,
						journey.getDistance());
			else if (column[i].equals(DatabaseHelper.JOURNEY_BUSINESS))
				values.put(DatabaseHelper.JOURNEY_BUSINESS,
						journey.isBusiness());
			else if (column[i].equals(DatabaseHelper.JOURNEY_EXPENSE))
				values.put(DatabaseHelper.JOURNEY_EXPENSE, journey.getExpense());
			else if (column[i].equals(DatabaseHelper.JOURNEY_IS_CLAIMED))
				values.put(DatabaseHelper.JOURNEY_IS_CLAIMED,
						journey.isClaimedFor());

			else if (column[i].equals(DatabaseHelper.JOURNEY_START_ADDR))
				values.put(DatabaseHelper.JOURNEY_START_ADDR,
						journey.getStartAddr());
			else if (column[i].equals(DatabaseHelper.JOURNEY_START_CITY))
				values.put(DatabaseHelper.JOURNEY_START_CITY,
						journey.getStartCity());
			else if (column[i].equals(DatabaseHelper.JOURNEY_END_ADDR))
				values.put(DatabaseHelper.JOURNEY_END_ADDR,
						journey.getEndAddr());
			else if (column[i].equals(DatabaseHelper.JOURNEY_END_CITY))
				values.put(DatabaseHelper.JOURNEY_END_CITY,
						journey.getEndCity());
			else if (column[i].equals(DatabaseHelper.JOURNEY_VEHICLE_REG))
				values.put(DatabaseHelper.JOURNEY_VEHICLE_REG,
						journey.getVehicle_reg());
			else if (column[i].equals(DatabaseHelper.JOURNEY_AVG_SPEED))
				values.put(DatabaseHelper.JOURNEY_AVG_SPEED,
						journey.getAvgSpeed());
			else if (column[i].equals(DatabaseHelper.JOURNEY_MAX_SPEED))
				values.put(DatabaseHelper.JOURNEY_MAX_SPEED,
						journey.getTopSpeed());
			else if (column[i].equals(DatabaseHelper.JOURNEY_END_TIME))
				values.put(DatabaseHelper.JOURNEY_END_TIME,
						journey.getEmissions());
			else if (column[i].equals(DatabaseHelper.JOURNEY_SCORE))
				values.put(DatabaseHelper.JOURNEY_SCORE,
						journey.getBehaviourScore());
			else if (column[i].equals(DatabaseHelper.JOURNEY_SCORE_ADDED))
				values.put(DatabaseHelper.JOURNEY_SCORE_ADDED,
						journey.isScoreAdded());
			else if (column[i].equals(DatabaseHelper.JOURNEY_VALID_BEHAVIOUR))
				values.put(DatabaseHelper.JOURNEY_SCORE,
						journey.getBehaviourScore());
		}

		database.update(DatabaseHelper.JOURNEY_TABLE, values,
				DatabaseHelper.JOURNEY_ID + "=" + journey.getId(), null);

		Log.d("Journey Datasource", "Updated journey column with startdate: " + journey.getStartTime());
	}

	/**
	 * Deletes given journey from the SQL database.
	 * 
	 * @param journey
	 */
	public void deleteJourney(DCJourney journey) {
		long id = journey.getId();

		database.delete(DatabaseHelper.JOURNEY_TABLE, DatabaseHelper.JOURNEY_ID
				+ " = " + id, null);
	}

	/**
	 * Deletes journey with the given id from SQL database.
	 * 
	 * @param id
	 */
	public void deleteJourney(long id) {
		database.delete(DatabaseHelper.JOURNEY_TABLE, DatabaseHelper.JOURNEY_ID
				+ " = " + id, null);
	}

	/**
	 * Gets specific date journeys from SQL database. i.e for claim
	 * 
	 * @return
	 */
	public ArrayList<DCJourney> getTodaysJourneys(String journeyDate) {
		ArrayList<DCJourney> journeys = new ArrayList<DCJourney>();

		Cursor cursor = database.query(DatabaseHelper.JOURNEY_TABLE,
				journeyColumns, DatabaseHelper.JOURNEY_CREATE_DATE + "=?",
				new String[] { "" + journeyDate }, null, null,
				DatabaseHelper.JOURNEY_CREATE_DATE + " DESC");

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			DCJourney journey = cursorToJourney(cursor);
			journeys.add(journey);
			cursor.moveToNext();
		}

		// make sure to close the cursor
		cursor.close();
		return journeys;
	}

	/**
	 * Gets specific date journey from SQL database. i.e for claim
	 * 
	 * @return
	 */
	public ArrayList<DCJourney> getSelectedJourneys(String startDate,
			String endDate) {
		ArrayList<DCJourney> journeys = new ArrayList<DCJourney>();
		String selection = DatabaseHelper.JOURNEY_CREATE_DATE + " BETWEEN '"
				+ startDate + "' AND '" + endDate + "' ";

		Cursor cursor = database.query(DatabaseHelper.JOURNEY_TABLE,
				journeyColumns, selection, null, null, null,
				DatabaseHelper.JOURNEY_CREATE_DATE + " DESC");

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			DCJourney journey = cursorToJourney(cursor);
			journeys.add(journey);
			cursor.moveToNext();
		}

		// make sure to close the cursor
		cursor.close();
		return journeys;
	}

	/**
	 * Gets all journeys from SQL database.
	 * 
	 * @return
	 */
	public ArrayList<DCJourney> getAllJourneys() {
		ArrayList<DCJourney> journeys = new ArrayList<DCJourney>();

		Cursor cursor = database.query(DatabaseHelper.JOURNEY_TABLE,
				journeyColumns, null, null, null, null,
				DatabaseHelper.JOURNEY_CREATE_DATE + " DESC");

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			DCJourney journey = cursorToJourney(cursor);
			journeys.add(journey);
			cursor.moveToNext();
		}

		// make sure to close the cursor
		cursor.close();
		return journeys;
	}

	private DCJourney cursorToJourney(Cursor cursor) {
		DCJourney journey = new DCJourney();
		journey.setId(cursor.getLong(0));
		journey.setDescription(cursor.getString(1));
		String journeyDate = Utilities.convertDateFormat(cursor.getString(2),
				"yyyy-MM-dd", "MMM dd, yyyy");

		Log.d("Journey Datasource","Found journey on date: " + journeyDate);

		journey.setCreateDate(journeyDate);
		journey.setStartTime(cursor.getString(3));
		journey.setEndTime(cursor.getString(4));
		journey.setDuration(cursor.getLong(5));
		journey.setDistance(cursor.getDouble(6));

		if (cursor.getInt(7) == 1)
			journey.setBusiness(true);
		else
			journey.setBusiness(false);

		journey.setExpense(cursor.getDouble(8));
		int claim = cursor.getInt(9);
		if (cursor.getInt(9) == 1)
			journey.setClaimedFor(true);
		else
			journey.setClaimedFor(false);
		Boolean claimd = journey.isClaimedFor();
		String add = cursor.getString(10);
		journey.setStartAddr(cursor.getString(10));
		journey.setStartCity(cursor.getString(11));

		journey.setEndAddr(cursor.getString(12));
		journey.setEndCity(cursor.getString(13));

		journey.setVehicle_reg(cursor.getString(14));

		journey.setAvgSpeed((float) cursor.getDouble(15));
		journey.setTopSpeed((float) cursor.getDouble(16));
		journey.setBehaviourScore(cursor.getString(17));

		if (cursor.getInt(18) == 1)
			journey.setScoreAdded(true);
		else
			journey.setScoreAdded(false);

		if (cursor.getInt(19) == 1)
			journey.setValidBehaviour(true);
		else
			journey.setValidBehaviour(false);

        if (cursor.getInt(20) == 1)
            journey.setBehaviourAdded(true);
        else
            journey.setBehaviourAdded(false);
        journey.setBehaviourCalculatedScore(cursor.getDouble(21));
		journey.setEmissions((float) cursor.getDouble(22));

		return journey;
	}

	// DC Journey Point
	// -----------------
	/**
	 * Creates given journey point in SQL database in relation with journey of
	 * given id.
	 * 
	 * @param journeyID
	 * @param point
	 * @return
	 */
	public long createJourneyPoint(Long journeyID, DCJourneyPoint point) {
		ContentValues values = new ContentValues();

		values = new ContentValues();
		values.put(DatabaseHelper.JOURNEY_POINT_JOURNEY_ID, journeyID);
		values.put(DatabaseHelper.JOURNEY_POINT_LATITUDE, point.getLat());
		values.put(DatabaseHelper.JOURNEY_POINT_LONGITUDE, point.getLng());
		long pointID = database.insert(DatabaseHelper.JOURNEY_POINT_TABLE,
				null, values);

		Log.d("Journey Datasource", "Created journey point");

		return pointID;
	}

	/**
	 * Deletes journey points associated with the journey of given id.
	 * 
	 * @param journeyID
	 */
	public void deleteJourneyPoints(long journeyID) {
		ArrayList<DCJourneyPoint> journeyPoints = getJourneyPoints(journeyID);

		if (journeyPoints != null) {
			for (int i = 0; i < journeyPoints.size(); i++) {
				database.delete(DatabaseHelper.JOURNEY_POINT_TABLE,
						DatabaseHelper.JOURNEY_POINT_ID + " = "
								+ journeyPoints.get(i).getId(), null);
			}
		}
	}

	/**
	 * Gets journey points associated with journey of the given id.
	 * 
	 * @param journeyID
	 * @return
	 */
	public ArrayList<DCJourneyPoint> getJourneyPoints(long journeyID) {
		ArrayList<DCJourneyPoint> points = new ArrayList<DCJourneyPoint>();

		Cursor cursor = database.query(DatabaseHelper.JOURNEY_POINT_TABLE,
				journeyPointColumns, DatabaseHelper.JOURNEY_POINT_JOURNEY_ID
						+ "=?", new String[] { "" + journeyID }, null, null,
				DatabaseHelper.JOURNEY_POINT_ID);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			DCJourneyPoint point = cursorToJourneyPoint(cursor);
			points.add(point);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return points;
	}

	/**
	 * Gets all journey points from SQL database.
	 * 
	 * @return
	 */
	public ArrayList<DCJourneyPoint> getAllJourneyPoints() {
		ArrayList<DCJourneyPoint> points = new ArrayList<DCJourneyPoint>();

		Cursor cursor = database.query(DatabaseHelper.JOURNEY_POINT_TABLE,
				journeyPointColumns, null, null, null, null,
				DatabaseHelper.JOURNEY_POINT_ID);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			DCJourneyPoint point = cursorToJourneyPoint(cursor);
			points.add(point);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return points;
	}

	private DCJourneyPoint cursorToJourneyPoint(Cursor cursor) {
		DCJourneyPoint point = new DCJourneyPoint();
		point.setId(cursor.getLong(0));
		point.setJourneyID(cursor.getLong(1));
		point.setLat(cursor.getDouble(2));
		point.setLng(cursor.getDouble(3));

		return point;
	}

	// DC Behaviour Point
	// -----------------
	/**
	 * Creates behaviour point in the SQL database in relation with journey of
	 * given id.
	 * 
	 * @param journeyID
	 * @param point
	 * @return
	 */
	public long createBehaviourPoint(Long journeyID, DCBehaviourPoint point) {
		ContentValues values = new ContentValues();

		values = new ContentValues();
		values.put(DatabaseHelper.BEHAVIOUR_JOURNEY_ID, journeyID);
		values.put(DatabaseHelper.BEHAVIOUR_ACCELERATION_X,
				point.getAccelerationX());
		values.put(DatabaseHelper.BEHAVIOUR_ACCELERATION_Y,
				point.getAccelerationY());
		values.put(DatabaseHelper.BEHAVIOUR_ACCELERATION_Z,
				point.getAccelerationZ());

        values.put(DatabaseHelper.BEHAVIOUR_GRAVITY_X,
                point.getGravityX());
        values.put(DatabaseHelper.BEHAVIOUR_GRAVITY_Y,
                point.getGravityY());
        values.put(DatabaseHelper.BEHAVIOUR_GRAVITY_Z,
                point.getGravityZ());
        values.put(DatabaseHelper.BEHAVIOUR_FUNCTION_EXECUTION_TIME,
                point.getExecutionTime());
        values.put(DatabaseHelper.BEHAVIOUR_INDEX,
                point.getBehaviourIndex());
		values.put(DatabaseHelper.BEHAVIOUR_POINT_TIME_DIFFERENCE,
				point.getPointTimeDifferenceFromStart());
		values.put(DatabaseHelper.BEHAVIOUR_ACTIVITY, point.getActivity());
		values.put(DatabaseHelper.BEHAVIOUR_SPEED, point.getSpeed());
		values.put(DatabaseHelper.BEHAVIOUR_FLAT, point.isDeviceFlat());
		values.put(DatabaseHelper.BEHAVIOUR_LANDSCAPE,
				point.isDeviceLandscape());
		values.put(DatabaseHelper.BEHAVIOUR_PORTRAIT, point.isDevicePortrait());
		long pointID = database.insert(DatabaseHelper.BEHAVIOUR_TABLE, null,
				values);

		return pointID;
	}



    /**
     * Updates given Behaviour in the SQL database.
     *
     * @param point
     */
    public void updateBehaviourPoint(DCBehaviourPoint point) {


        ContentValues values = new ContentValues();
        values = new ContentValues();
        values.put(DatabaseHelper.BEHAVIOUR_ACCELERATION_X,
                point.getAccelerationX());
        values.put(DatabaseHelper.BEHAVIOUR_ACCELERATION_Y,
                point.getAccelerationY());
        values.put(DatabaseHelper.BEHAVIOUR_ACCELERATION_Z,
                point.getAccelerationZ());

        values.put(DatabaseHelper.BEHAVIOUR_GRAVITY_X,
                point.getGravityX());
        values.put(DatabaseHelper.BEHAVIOUR_GRAVITY_Y,
                point.getGravityY());
        values.put(DatabaseHelper.BEHAVIOUR_GRAVITY_Z,
                point.getGravityZ());

        values.put(DatabaseHelper.BEHAVIOUR_FUNCTION_EXECUTION_TIME,
                point.getExecutionTime());
        values.put(DatabaseHelper.BEHAVIOUR_INDEX,
                point.getBehaviourIndex());
		values.put(DatabaseHelper.BEHAVIOUR_POINT_TIME_DIFFERENCE,
				point.getPointTimeDifferenceFromStart());
        values.put(DatabaseHelper.BEHAVIOUR_ACTIVITY, point.getActivity());
        values.put(DatabaseHelper.BEHAVIOUR_SPEED, point.getSpeed());
        values.put(DatabaseHelper.BEHAVIOUR_FLAT, point.isDeviceFlat());
        values.put(DatabaseHelper.BEHAVIOUR_LANDSCAPE,
                point.isDeviceLandscape());
        values.put(DatabaseHelper.BEHAVIOUR_PORTRAIT, point.isDevicePortrait());

        database.update(DatabaseHelper.BEHAVIOUR_TABLE, values,
                DatabaseHelper.BEHAVIOUR_ID + "=" + point.getId(), null);
    }

	/**
	 * Deletes behaviour points associated with the journey of given id.
	 * 
	 * @param journeyID
	 */
	public void deleteBehaviourPoints(long journeyID) {
		ArrayList<DCBehaviourPoint> behaviourPoints = getBehaviourPoints(journeyID);

		if (behaviourPoints != null) {
			for (int i = 0; i < behaviourPoints.size(); i++) {
				database.delete(DatabaseHelper.BEHAVIOUR_TABLE,
						DatabaseHelper.BEHAVIOUR_ID + " = "
								+ behaviourPoints.get(i).getId(), null);
			}
		}
	}

	/**
	 * Gets behaviour points associated with the journey of given id.
	 * 
	 * @param journeyID
	 * @return
	 */
	public ArrayList<DCBehaviourPoint> getBehaviourPoints(long journeyID) {
		ArrayList<DCBehaviourPoint> points = new ArrayList<DCBehaviourPoint>();

		Cursor cursor = database.query(DatabaseHelper.BEHAVIOUR_TABLE,
				behaviourPointColumns, DatabaseHelper.BEHAVIOUR_JOURNEY_ID
						+ "=?", new String[] { "" + journeyID }, null, null,
				DatabaseHelper.BEHAVIOUR_ID);

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			DCBehaviourPoint point = cursorToBehaviourPoint(cursor);
			points.add(point);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();

		return points;
	}

	private DCBehaviourPoint cursorToBehaviourPoint(Cursor cursor) {
		DCBehaviourPoint point = new DCBehaviourPoint();

		point.setId(cursor.getLong(0));
		point.setJourneyId(cursor.getLong(1));
		point.setAccelerationX(cursor.getDouble(2));
		point.setAccelerationY(cursor.getDouble(3));
		point.setAccelerationZ(cursor.getDouble(4));

        point.setGravityX(cursor.getDouble(5));
        point.setGravityY(cursor.getDouble(6));
        point.setGravityZ(cursor.getDouble(7));

        point.setExecutionTime(cursor.getLong(8));
        point.setBehaviourIndex(cursor.getDouble(9));
		point.setPointTimeDifferenceFromStart(cursor.getLong(10));


        point.setActivity(cursor.getString(11));
		point.setSpeed(cursor.getFloat(12));

		if (cursor.getInt(13) == 1)
			point.setDeviceFlat(true);
		else
			point.setDeviceFlat(false);

		if (cursor.getInt(14) == 1)
			point.setDeviceLandscape(true);
		else
			point.setDeviceLandscape(false);

		if (cursor.getInt(15) == 1)
			point.setDevicePortrait(true);
		else
			point.setDevicePortrait(false);

		return point;
	}

	private String todaysDate(String date) {

		String strDate = Utilities.convertDateFormat(date,
				"MMM dd, yyyy HH:mm", "yyyy-MM-dd");
		return strDate;
	}

	private String todaysDate() {

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(c.getTime());
		return strDate;
	}
}
