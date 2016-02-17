package com.tracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class helps to create a SQL database for the app. This is the place
 * where you add any new tables and columns.
 *
 * @author Muhammad Azeem Anwar
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static int DB_VERSION = 4;
    private static String DATABASE_NAME = "driverConnex.db";

    private final Context context;

    // Expenses Module
    // ---------------------------------------------------
    public static final String EXPENSE_TABLE = "expenses";
    public static final String EXPENSE_ID = "expense_id";
    public static final String EXPENSE_DESCRIPTION = "expense_description";
    public static final String EXPENSE_TYPE = "expense_type";
    public static final String EXPENSE_SPEND = "expense_spend";
    public static final String EXPENSE_DATE = "expense_date";
    public static final String EXPENSE_CURRENCY = "expense_currency";
    public static final String EXPENSE_IS_VAT = "expense_is_vat";
    public static final String EXPENSE_IS_CLAIMED = "expense_is_claimed";

    public static final String EXPENSEIS_BUSINESS = "expense_is_business";
    public static final String EXPENSE_PICPATH = "expense_pic_path";
    public static final String EXPENSE_VEHICLE = "expense_vehicle";
    public static final String FUEL_VOLUME = "fuel_volume";
    public static final String FUEL_MILEAGE = "fuel_mileage";
    public static final String FUEL_IS_FULL_TANK = "fuel_is_full_tank";
    public static final String KPMG_HOURS = "kpmg_hours";
    public static final String KPMG_CLAIMED = "kpmg_claimed";
    // dashcam Module
    // ---------------------------------------------------
    public static final String VIDEO_TABLE = "video";
    public static final String VIDEO_ID = "video_id";
    public static final String VIDEO_DESCRIPTION = "video_description";
    public static final String VIDEO_TYPE = "video_type";
    public static final String VIDEO_TIME = "video_time";
    public static final String VIDEO_DATE = "video_date";
    public static final String VIDEO_START_PLACE = "video_start_place";
    public static final String VIDEO_TEMP_PATH = "video_temp_path";
    public static final String VIDEO_SAVED_PATH = "video_saved_path";

    // Journey Module
    // ---------------------------------------------------
    // DCJourney Table
    public static final String JOURNEY_TABLE = "journey";
    public static final String JOURNEY_ID = "journey_id";
    public static final String JOURNEY_DESC = "journey_desc";
    public static final String JOURNEY_CREATE_DATE = "journey_create_date";
    public static final String JOURNEY_START_TIME = "journey_start_time";
    public static final String JOURNEY_END_TIME = "journey_end_time";
    public static final String JOURNEY_DURATION = "journey_duration";
    public static final String JOURNEY_DISTANCE = "journey_distance";
    public static final String JOURNEY_BUSINESS = "journey_purpose";
    public static final String JOURNEY_EXPENSE = "journey_expense";
    public static final String JOURNEY_IS_CLAIMED = "journey_is_claimed";

    public static final String JOURNEY_START_ADDR = "journey_start_address";
    public static final String JOURNEY_START_CITY = "journey_start_city";

    public static final String JOURNEY_END_ADDR = "journey_end_address";
    public static final String JOURNEY_END_CITY = "journey_end_city";
    public static final String JOURNEY_VEHICLE_REG = "journey_vehicle_reg";
    public static final String JOURNEY_AVG_SPEED = "journey_avg_speed";
    public static final String JOURNEY_MAX_SPEED = "journey_max_speed";
    public static final String JOURNEY_EMISSION = "journey_emission";
    public static final String JOURNEY_SCORE = "journey_score";
    public static final String JOURNEY_SCORE_ADDED = "journey_score_added";
    public static final String JOURNEY_VALID_BEHAVIOUR = "journey_valid_behaviour";
    public static final String JOURNEY_BEHAVIOUR_ADDED = "journey_behaviour_added";
    public static final String JOURNEY_CALCULATED_BEHAVIOUR = "journey_calculated_behaviour";

    // DCJourneyPoint table
    public static final String JOURNEY_POINT_TABLE = "journey_point";
    public static final String JOURNEY_POINT_ID = "journey_point_id";
    public static final String JOURNEY_POINT_JOURNEY_ID = "journey_point_journey_id";
    public static final String JOURNEY_POINT_LATITUDE = "journey_point_latitude";
    public static final String JOURNEY_POINT_LONGITUDE = "journey_point_longitude";

    // DCMessage table
    public static final String MESSAGE_TABLE = "message";
    public static final String MESSAGE_ID = "message_id";
    public static final String MESSAGE_SERVER_ID = "message_server_id";
    public static final String MESSAGE_TITLE = "message_title";
    public static final String MESSAGE_BODY = "message_body";
    public static final String MESSAGE_DATE = "message_date";
    public static final String MESSAGE_READ = "message_is_read";

    // DCPhoto table

    public static final String PHOTO_TABLE = "photo";
    public static final String PHOTO_ID = "photo_id";
    public static final String INCIDENT_PHOTO_INCIDENT_ID = "incident_photo_incident_id";
    public static final String PHOTO_BYTE = "photo_byte";

    // DCWitness table
    public static final String WITNESS_TABLE = "witness";
    public static final String WITNESS_ID = "witness_id";
    public static final String INCIDENT_WITNESS_INCIDENT_ID = "incident_witness_incident_id";
    public static final String WITNESS_NAME = "witness_name";
    public static final String WITNESS_PHONE = "witness_phone";
    public static final String WITNESS_EMAIL = "witness_email";
    public static final String WITNESS_STATEMENT = "witness_statement";

    // DCIncident table

    public static final String INCIDENT_TABLE = "incident";
    public static final String INCIDENT_ID = "incident_id";
    public static final String INCIDENT_GDATE = "incident_date";
    public static final String INCIDENT_GTIME = "incident_time";
    public static final String INCIDENT_VEHICLE_REG = "incident_vehicle_reg";
    public static final String INCIDENT_LATITUDE = "incident_latitude";
    public static final String INCIDENT_LONGITUDE = "incident_longitude";
    public static final String INCIDENT_DESC = "incident_description";
    public static final String INCIDENT_VIDEO = "incident_video";

    public static final String INCIDENT_POINT_LATITUDE = "incident_point_latitude";
    public static final String INCIDENT_POINT_LONGITUDE = "incident_point_longitude";
    public static final String INCIDENT_VEHICLE = "incident_vehicle";

    // DCBehaviourPoint table
    public static final String BEHAVIOUR_TABLE = "behaviour";
    public static final String BEHAVIOUR_ID = "behaviour_id";
    public static final String BEHAVIOUR_JOURNEY_ID = "behaviour_journey_id";
    public static final String BEHAVIOUR_ACCELERATION_X = "behaviour_acceleration_x";
    public static final String BEHAVIOUR_ACCELERATION_Y = "behaviour_acceleration_y";
    public static final String BEHAVIOUR_ACCELERATION_Z = "behaviour_acceleration_z";

    public static final String BEHAVIOUR_GRAVITY_X = "behaviour_gravity_x";
    public static final String BEHAVIOUR_GRAVITY_Y = "behaviour_gravity_y";
    public static final String BEHAVIOUR_GRAVITY_Z = "behaviour_gravity_z";


    public static final String BEHAVIOUR_ACTIVITY = "behaviour_activity";
    public static final String BEHAVIOUR_SPEED = "behaviour_speed";
    public static final String BEHAVIOUR_FLAT = "behaviour_flat";
    public static final String BEHAVIOUR_LANDSCAPE = "behaviour_landscape";
    public static final String BEHAVIOUR_PORTRAIT = "behaviour_portrait";
    public static final String BEHAVIOUR_FUNCTION_EXECUTION_TIME = "behaviour_function_execution_time";
    public static final String BEHAVIOUR_INDEX = "behaviour_index";
    public static final String BEHAVIOUR_POINT_TIME_DIFFERENCE = "behaviour_point_time_difference";
    // ---------------------------------------------------

    // Parking Module
    // ---------------------------------------------------
    public static final String PARKING_TABLE = "parking_locations";
    public static final String PARKING_ID = "parking_location_id";
    public static final String PARKIN_GDATE = "parking_date";
    public static final String PARKING_LATITUDE = "parking_latitude";
    public static final String PARKING_LONGITUDE = "parking_longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Expenses table
        db.execSQL("CREATE TABLE " + EXPENSE_TABLE + " (" + EXPENSE_ID
                + " INTEGER PRIMARY KEY, " + EXPENSE_TYPE + " TEXT, "
                + EXPENSE_DESCRIPTION + " TEXT, " + EXPENSE_SPEND + " DOUBLE, "
                + EXPENSE_DATE + " DATETIME, " + EXPENSE_CURRENCY + " TEXT, "
                + EXPENSE_IS_VAT + " BOOLEAN, " + EXPENSE_IS_CLAIMED
                + " BOOLEAN, "

                + EXPENSEIS_BUSINESS + " BOOLEAN, " + EXPENSE_PICPATH
                + " TEXT, " + FUEL_VOLUME + " DOUBLE, " + FUEL_MILEAGE
                + " INTEGER, " + FUEL_IS_FULL_TANK + " BOOLEAN, "
                + EXPENSE_VEHICLE + " TEXT, " + KPMG_HOURS + " TEXT, "
                + KPMG_CLAIMED + " TEXT)");
// create video table
        db.execSQL("CREATE TABLE " + VIDEO_TABLE + " (" + VIDEO_ID
                + " INTEGER PRIMARY KEY, " + VIDEO_DESCRIPTION + " TEXT, "
                + VIDEO_TYPE + " TEXT, " + VIDEO_TIME + " TEXT, "
                + VIDEO_DATE + " DATETIME, " + VIDEO_START_PLACE + " TEXT, "
                + VIDEO_TEMP_PATH + " TEXT, " + VIDEO_SAVED_PATH + " TEXT)");

        // Create DCJourney table
        db.execSQL("CREATE TABLE " + JOURNEY_TABLE + " (" + JOURNEY_ID
                + " INTEGER PRIMARY KEY, " + JOURNEY_DESC + " TEXT, "
                + JOURNEY_START_TIME + " DATETIME, " + JOURNEY_END_TIME
                + " DATETIME, " + JOURNEY_DURATION + " INTEGER, "
                + JOURNEY_DISTANCE + " DOUBLE, " + JOURNEY_BUSINESS
                + " BOOLEAN, " + JOURNEY_EXPENSE + " DOUBLE, "
                + JOURNEY_IS_CLAIMED + " BOOLEAN, "

                + JOURNEY_START_ADDR + " TEXT, " + JOURNEY_START_CITY
                + " TEXT, " + JOURNEY_END_ADDR + " TEXT, " + JOURNEY_END_CITY
                + " TEXT, " + JOURNEY_VEHICLE_REG + " TEXT, "
                + JOURNEY_AVG_SPEED + " DOUBLE, " + JOURNEY_MAX_SPEED
                + " DOUBLE, " + JOURNEY_CREATE_DATE + " DATETIME, "
                + JOURNEY_SCORE + " TEXT, " + JOURNEY_SCORE_ADDED
                + " BOOLEAN, " + JOURNEY_VALID_BEHAVIOUR + " BOOLEAN, "
                + JOURNEY_BEHAVIOUR_ADDED + " BOOLEAN, " + JOURNEY_CALCULATED_BEHAVIOUR + " TEXT, "
                + JOURNEY_EMISSION + " DOUBLE)");

        // Create DCJourneyPoint table
        db.execSQL("CREATE TABLE " + JOURNEY_POINT_TABLE + " ("
                + JOURNEY_POINT_ID + " INTEGER PRIMARY KEY, "
                + JOURNEY_POINT_JOURNEY_ID + " INTEGER, "
                + JOURNEY_POINT_LATITUDE + " DOUBLE, "
                + JOURNEY_POINT_LONGITUDE + " DOUBLE, " + "FOREIGN KEY ("
                + JOURNEY_POINT_JOURNEY_ID + ") REFERENCES " + JOURNEY_TABLE
                + " (" + JOURNEY_ID + "));");
        // Create DCPhote table
        db.execSQL("CREATE TABLE " + PHOTO_TABLE + " (" + PHOTO_ID
                + " INTEGER PRIMARY KEY, " + INCIDENT_PHOTO_INCIDENT_ID
                + " INTEGER, " + PHOTO_BYTE + " BLOB , " + "FOREIGN KEY ("
                + INCIDENT_PHOTO_INCIDENT_ID + ") REFERENCES " + INCIDENT_TABLE
                + " (" + INCIDENT_ID + "));");

        // Create DCwitness table
        db.execSQL("CREATE TABLE " + WITNESS_TABLE + " (" + WITNESS_ID
                + " INTEGER PRIMARY KEY, " + INCIDENT_WITNESS_INCIDENT_ID
                + " INTEGER, " + WITNESS_NAME + " TEXT , " + WITNESS_PHONE
                + " TEXT , " + WITNESS_EMAIL + " TEXT , " + WITNESS_STATEMENT
                + " TEXT , " + "FOREIGN KEY (" + INCIDENT_WITNESS_INCIDENT_ID
                + ") REFERENCES " + WITNESS_TABLE + " (" + INCIDENT_ID + "));");

        // Create DCIncident table
        db.execSQL("CREATE TABLE " + INCIDENT_TABLE + " (" + INCIDENT_ID
                + " INTEGER PRIMARY KEY, " + INCIDENT_GDATE + " TEXT, "
                + INCIDENT_VEHICLE_REG + " TEXT, " + INCIDENT_DESC + " TEXT, "
                + INCIDENT_LATITUDE + " DOUBLE, " + INCIDENT_LONGITUDE
                + " DOUBLE, " + INCIDENT_VIDEO + " TEXT )");

        // Create DCBehaviour table
        db.execSQL("CREATE TABLE " + BEHAVIOUR_TABLE + " (" + BEHAVIOUR_ID
                + " INTEGER PRIMARY KEY, " + BEHAVIOUR_JOURNEY_ID
                + " INTEGER, " + BEHAVIOUR_ACCELERATION_X + " DOUBLE, "
                + BEHAVIOUR_ACCELERATION_Y + " DOUBLE, "
                + BEHAVIOUR_ACCELERATION_Z + " DOUBLE, " + BEHAVIOUR_GRAVITY_X + " DOUBLE, " + BEHAVIOUR_GRAVITY_Y + " DOUBLE, " + BEHAVIOUR_GRAVITY_Z + " DOUBLE, " + BEHAVIOUR_FUNCTION_EXECUTION_TIME + " INTEGER, " + BEHAVIOUR_INDEX + " DOUBLE, " + BEHAVIOUR_POINT_TIME_DIFFERENCE + " INTEGER, " + BEHAVIOUR_ACTIVITY
                + " TEXT, " + BEHAVIOUR_SPEED + " DOUBLE, " + BEHAVIOUR_FLAT
                + " BOOLEAN, " + BEHAVIOUR_LANDSCAPE + " BOOLEAN, "
                + BEHAVIOUR_PORTRAIT + " BOOLEAN, " + "FOREIGN KEY ("
                + BEHAVIOUR_JOURNEY_ID + ") REFERENCES " + JOURNEY_TABLE + " ("
                + JOURNEY_ID + "));");

        // Create Parking Locations table
        db.execSQL("CREATE TABLE " + PARKING_TABLE + " (" + PARKING_ID
                + " INTEGER PRIMARY KEY, " + PARKIN_GDATE + " DATETIME, "
                + PARKING_LATITUDE + " DOUBLE, " + PARKING_LONGITUDE
                + " DOUBLE)");

        // Create DCMessage table
        db.execSQL("CREATE TABLE " + MESSAGE_TABLE + " (" + MESSAGE_ID
                + " INTEGER PRIMARY KEY, " + MESSAGE_SERVER_ID + " TEXT , " + MESSAGE_TITLE
                + " TEXT , " + MESSAGE_BODY + " TEXT , " + MESSAGE_READ + " BOOLEAN, "+ MESSAGE_DATE
                + " TEXT )");
    }

    /**
     * Updates tables in database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + EXPENSE_TABLE);
        db.execSQL("drop table if exists " + JOURNEY_TABLE);
        db.execSQL("drop table if exists " + JOURNEY_POINT_TABLE);
        db.execSQL("drop table if exists " + PHOTO_TABLE);
        db.execSQL("drop table if exists " + WITNESS_TABLE);
        db.execSQL("drop table if exists " + INCIDENT_TABLE);
        db.execSQL("drop table if exists " + BEHAVIOUR_TABLE);
        db.execSQL("drop table if exists " + PARKING_TABLE);
        db.execSQL("drop table if exists " + MESSAGE_TABLE);
        onCreate(db);
    }

    public static void setDatabaseName(String username) {
        DATABASE_NAME = "driverConnex_" + username + ".db";
    }
}
