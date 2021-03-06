package edu.missouri.nimh.emotion.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 *
 * @author Andrew Smith
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * DO <b>NOT</b> update DB_VERSION without reason.
     *
     *  Manually incrementing DB VERSION <i><b>will</b></i> cause the app to drop and recreate the
     *  database tables when opening an older database, <b>losing all existing database data in the process</b>.
     */
    private static final int    DB_VERSION  = 2;

    private static DatabaseHelper databaseHelper;

    private static final String DB_LOCATION  = "";
    private static final String LOG_TAG      = "DB";

    public static final String DB_NAME                  = "db.db";
    public static final String LOCATION_DATA_TABLE      = "locationData";
    public static final String HARDWARE_INFO_TABLE      = "hardwareInfo";
    public static final String SURVEY_TABLE             = "survey";
    public static final String QUESTION_TABLE           = "question";
    public static final String QUESTION_ON_SURVEY_TABLE = "questionOnSurvey";
    public static final String SURVEY_SUBMISSION_TABLE  = "surveySubmission";
    public static final String SUBMISSION_ANSWER_TABLE  = "submissionAnswer";
    public static final String EVENT_TABLE              = "event";

    private static final String LOCATION_DATA_SQL =
            "CREATE TABLE " + LOCATION_DATA_TABLE + " (" +
            "    `locationDataId` INTEGER PRIMARY KEY NOT NULL," +
            "    `latitude`  DOUBLE NULL,"                       +
            "    `longitude` DOUBLE NULL,"                       +
            "    `accuracy`  Float NULL,"                        +
            "    `provider`  TEXT NULL,"                         +
            "    `type`      TEXT NULL"                          +
            ");";

    private static final String HARDWARE_INFO_SQL =
            "CREATE TABLE " + HARDWARE_INFO_TABLE + " ("         +
            "    `hardwareInfoID` INTEGER PRIMARY KEY NOT NULL," +
            "    `message` TEXT NOT NULL"                        +
            ");";

    private static final String SURVEY_SQL =
            "CREATE TABLE " + SURVEY_TABLE +" ("   +
            "    `surveyID` VARCHAR(45) NOT NULL," +
            "    `name`     TEXT NOT NULL,"        +
            "    PRIMARY KEY (`surveyID`)"         +
            ");";

    private static final String QUESTION_SQL =
            "CREATE TABLE " + QUESTION_TABLE +" ("  +
            "    `questionID` VARCHAR(45) NULL,"    +
            "    `text`       TEXT        NOT NULL" +
            ");";

    private static final String QUESTION_SURVEY =
            "CREATE TABLE " + QUESTION_ON_SURVEY_TABLE + " ("   +
            "    `surveyID`   VARCHAR(45) NOT NULL,"            +
            "    `questionID` VARCHAR(45) NOT NULL,"            +
            "    PRIMARY KEY (`surveyID`, `questionID`)"        +
            ");";

    private static final String SurveySubmission =
            "CREATE TABLE " + SURVEY_SUBMISSION_TABLE + " ("          +
            "    `surveySubmissionID` CHAR(36) PRIMARY KEY NOT NULL," +
            "    `surveyID` VARCHAR(45) NOT NULL,"                    +
                    "reminderTS1 TEXT NULL,"                          +
                    "reminderTS2 TEXT NULL,"                          +
                    "reminderTS3 TEXT NULL"                           +
             ");";

    private static final String SUBMISSION_ANSWER =
            "CREATE TABLE " + SUBMISSION_ANSWER_TABLE + " ("             +
            "        `submissionAnswerID` INTEGER PRIMARY KEY NOT NULL," +
            "        `surveySubmissionID` CHAR(36),"                     +
            "        `questionID`         VARCHAR(45) NOT NULL,"         +
            "        `answer`             TEXT NOT NULL"                 +
            ");";

   private static final String EVENT_SQL =
            " CREATE TABLE " + EVENT_TABLE + " ("             +
            "    `eventID` INTEGER PRIMARY KEY NOT NULL,"     +
            "    `userID`             VARCHAR(8)  NOT NULL,"  +
            "    `timestamp`          TIMESTAMP   NOT NULL,"  +
            "    `type`               TEXT        NULL,"      +
            "    `studyDay`           DATE        NOT NULL,"  +
            "    `scheduledTS`        TIMESTAMP   NULL,"      +
            "    `startTS`            TIMESTAMP   NULL,"      +
            "    `endTS`              TIMESTAMP   NULL,"      +
            "    `surveySubmissionID` CHAR(36)    NULL,"      +
            "    `locationDataID`     INT         NULL,"      +
            "    `hardwareInfoID`     INT         NULL,"      +
            "    `isSynchronized`     BOOL        NOT NULL"   +
            ");";

    private static final String[] TABLES_SQL = {
            LOCATION_DATA_SQL,
            HARDWARE_INFO_SQL,
            SURVEY_SQL,
            QUESTION_SQL,
            QUESTION_SURVEY,
            SurveySubmission,
            SUBMISSION_ANSWER,
            EVENT_SQL
    };

    private static final String[] TABLE_NAMES = {
            LOCATION_DATA_TABLE,
            HARDWARE_INFO_TABLE,
            SURVEY_TABLE,
            QUESTION_TABLE,
            QUESTION_ON_SURVEY_TABLE,
            SURVEY_SUBMISSION_TABLE,
            SUBMISSION_ANSWER_TABLE,
            EVENT_TABLE
    };

    private DatabaseHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @NonNull
    public static synchronized DatabaseHelper getInstance(@NonNull Context context) {
        if(databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }

        return databaseHelper;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        Log.d(LOG_TAG, "Creating database");

        for (String createStatement : TABLES_SQL) {
            db.execSQL(createStatement);
        }
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(LOG_TAG, "Database is being upgraded");
        Log.d(LOG_TAG, "Deleting tables");

        for(String table : TABLE_NAMES) {
            db.execSQL(String.format("DROP TABLE %s;", table));
        }

        Log.d(LOG_TAG, "Creating tables");

        onCreate(db);
    }
}
