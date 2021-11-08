package org.nic.fruits.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;
import android.util.Log;

import org.nic.fruits.database.dao.CropFertilizerMasterDAO;
import org.nic.fruits.database.dao.CropMasterDao;
import org.nic.fruits.database.dao.CropMasterType;
import org.nic.fruits.database.dao.CropMultipickingDataDAO;
import org.nic.fruits.database.dao.CropRegistrationDao;
import org.nic.fruits.database.dao.CropWeeklyDataDAO;
import org.nic.fruits.database.dao.FarmFertilizerDAO;
import org.nic.fruits.database.dao.FarmerOwnerDetails;

import org.nic.fruits.database.dao.FertilizerCropMasterDAO;
import org.nic.fruits.database.dao.FertilizerNameMasterDAO;
import org.nic.fruits.database.dao.InterCropRegistrationDao;
import org.nic.fruits.database.dao.IrrigationTypeDao;
import org.nic.fruits.database.dao.MixedCropRegistrationDao;
import org.nic.fruits.database.dao.PlantAgeMasterDAO;
import org.nic.fruits.pojo.ModelCropFertilizerMasternpk;
import org.nic.fruits.pojo.ModelCropMaster;
import org.nic.fruits.pojo.ModelCropMasterType;
import org.nic.fruits.pojo.ModelCropMultipickingData;
import org.nic.fruits.pojo.ModelCropWeeklyData;
import org.nic.fruits.pojo.ModelCropRegistration;
import org.nic.fruits.pojo.ModelFarmFertilizer;
import org.nic.fruits.pojo.ModelFertilizerCropMaster;
import org.nic.fruits.pojo.ModelFertilizerNameMaster;
import org.nic.fruits.pojo.ModelInterCropRegistration;
import org.nic.fruits.pojo.ModelIrrigationType;
import org.nic.fruits.pojo.ModelMixedCropRegistration;
import org.nic.fruits.pojo.ModelOwnerDetails;
import org.nic.fruits.pojo.ModelPlantAgeMaster;
import org.nic.fruits.pojo.ModelweatherDetails;
import org.nic.fruits.database.dao.CropDetailsDAO;
import org.nic.fruits.database.dao.FarmerDetailsDAO;
import org.nic.fruits.database.dao.ForecastDetailsDAO;
import org.nic.fruits.database.dao.LandDetailsDao;
import org.nic.fruits.database.dao.PaymentDetailsDAO;
import org.nic.fruits.database.dao.WeatherDetailsDAO;
import org.nic.fruits.pojo.ModelCropSurveyDetails;
import org.nic.fruits.pojo.ModelFarmerDetails;
import org.nic.fruits.pojo.ModelFarmerLandDeatails;
import org.nic.fruits.pojo.ModelForeCastDetails;
import org.nic.fruits.pojo.ModelPaymentDetails;


//Programming by Harsha  for version 1.0 release
@Database(entities = {ModelFarmerLandDeatails.class, ModelCropSurveyDetails.class, ModelFarmerDetails.class, ModelPaymentDetails.class, ModelweatherDetails.class, ModelForeCastDetails.class, ModelOwnerDetails.class, ModelCropRegistration.class, ModelCropWeeklyData.class, ModelCropMultipickingData.class, ModelMixedCropRegistration.class, ModelCropMasterType.class, ModelCropMaster.class, ModelInterCropRegistration.class, ModelFertilizerCropMaster.class, ModelPlantAgeMaster.class, ModelFertilizerNameMaster.class, ModelCropFertilizerMasternpk.class, ModelIrrigationType.class}, version = 2, exportSchema = false) //, ModelFarmFertilizer.class

public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "fruites";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        // TODO (2) call allowMainThreadQueries before building the instance
//                        .allowMainThreadQueries()
                    //    .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }
   static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Your migration strategy here

            /*database.execSQL("CREATE TABLE cropmaster(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cropnameen TEXT," +
                    "cropnamekn TEXT, "+
                    "cropcode TEXT, "+
                    "ismultipicking TEXT)");*/

            database.execSQL("CREATE TABLE farmer_owner_details(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "farmerid TEXT," +
                    "ownername TEXT, "+
                    "ownernumber TEXT, "+
                    "mainownernumber TEXT, "+
                    "relativename TEXT, "+
                    "villagecode TEXT, "+
                    "villagelgcode TEXT,"+
                    "area TEXT)");

            database.execSQL("CREATE TABLE cropregistration(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "farmerid TEXT," +
                    "crid TEXT, "+
                    "year TEXT, "+
                    "yearcode TEXT, "+
                    "season TEXT, "+
                    "seasoncode TEXT, "+
                    "ownerid TEXT, "+
                    "ownername TEXT, "+
                    "ownerarea TEXT, "+
                    "surveynumber TEXT, "+
                    "district TEXT, "+
                    "taluk TEXT, "+
                    "village TEXT, "+
                    "cropname TEXT, "+
                    "croptype TEXT, "+
                    "totalcrops TEXT, "+
                    "cropcode TEXT, "+
                    "cropvariety TEXT, "+
                    "cropextent TEXT, "+
                    "irrigation_type_id TEXT, "+
                    "irrigation_type TEXT, "+
                    "irrigation_source_id TEXT, "+
                    "irrigation_source TEXT, "+
                    "farming TEXT, "+
                    "presowing TEXT, "+
                    "sowingdate TEXT, "+
                    "gpscoordinates TEXT, "+
                    "cropimagepath TEXT, "+
                    "cropimage TEXT, "+
                    "isuploaded TEXT)");

            database.execSQL("CREATE TABLE mixedcropsregistration(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "farmerid TEXT," +
                    "crid TEXT, "+
                    "year TEXT, "+
                    "yearcode TEXT, "+
                    "season TEXT, "+
                    "seasoncode TEXT, "+
                    "ownerid TEXT, "+
                    "ownername TEXT, "+
                    "ownerarea TEXT, "+
                    "surveynumber TEXT, "+
                    "district TEXT, "+
                    "taluk TEXT, "+
                    "village TEXT, "+
                    "croptype TEXT, "+
                    "mixedcropname TEXT, "+
                    "cropcode TEXT, "+
                    "mixedcropnumber TEXT, "+
                    "mixedcropvariety TEXT, "+
                    "totalcrops TEXT, "+
                    "mixedcropname TEXT, "+
                    "mixedcropvariety TEXT, "+
                    "mixdecropextent TEXT, "+
                    "totalcropextent TEXT, "+
                    "farming TEXT, "+
                    "presowing TEXT, "+
                    "sowingdate TEXT, "+
                    "gpscoordinates TEXT, "+
                    "mixedcropimagepath TEXT, "+
                    "mixedcropimage TEXT, "+
                    "isuploaded TEXT)");

            database.execSQL("CREATE TABLE intercropsregistration(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "farmerid TEXT," +
                    "crid TEXT, "+
                    "year TEXT, "+
                    "yearcode TEXT, "+
                    "season TEXT, "+
                    "seasoncode TEXT, "+
                    "ownerid TEXT, "+
                    "ownername TEXT, "+
                    "ownerarea TEXT, "+
                    "surveynumber TEXT, "+
                    "district TEXT, "+
                    "taluk TEXT, "+
                    "village TEXT, "+
                    "croptype TEXT, "+
                    "totalcrops TEXT, "+
                    "intercropnumber, TEXT"+
                    "intercropname TEXT, "+
                    "intercropvariety TEXT, "+
                    "intercropextent TEXT, "+
                    "totalcropextent TEXT, "+
                    "farming TEXT, "+
                    "presowing TEXT, "+
                    "sowingdate TEXT, "+
                    "gpscoordinates TEXT, "+
                    "intercropimagepath TEXT, "+
                    "intercropimage TEXT, "+
                    "isuploaded TEXT)");

            database.execSQL("CREATE TABLE cropweeklydata(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "farmerid TEXT," +
                    "crid TEXT, "+
                    "year TEXT, "+
                    "yearcode TEXT, "+
                    "season TEXT, "+
                    "seasoncode TEXT, "+
                    "ownerid TEXT, "+
                    "ownername TEXT, "+
                    "district TEXT, "+
                    "taluk TEXT, "+
                    "village TEXT, "+
                    "ownerarea TEXT, "+
                    "surveynumber TEXT, "+
                    "cropextent TEXT, "+
                    "cropname TEXT, "+
                    "weeknumber TEXT, "+
                    "farming TEXT, "+
                    "presowingtype TEXT, "+
                    "presowingdetails TEXT, "+
                    "pesticidetype TEXT, "+
                    "pesticidedetails TEXT, "+
                    "cropdisease TEXT, "+
                    "cropdiseasetoexperts TEXT, "+
                    "croppick TEXT, "+
                    "croppickyield TEXT, "+
                    "cropimagepath TEXT, "+
                    "diseaseimagepath TEXT,"+
                    "cropsoweddate TEXT,"+
                    "pickingdate TEXT,"+
                    "isuploaded TEXT)");

            database.execSQL("CREATE TABLE cropmultipickdata(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "farmerid TEXT," +
                    "crid TEXT, "+
                    "year TEXT, "+
                    "yearcode TEXT, "+
                    "season TEXT, "+
                    "seasoncode TEXT, "+
                    "ownerid TEXT, "+
                    "owner TEXT, "+
                    "area TEXT, "+
                    "surveynumber TEXT, "+
                    "cropname TEXT, "+
                    "cropextent TEXT, "+
                    "sowingdate TEXT, "+
                    "pickingdate TEXT, "+
                    "week TEXT, "+
                    "croppick TEXT, "+
                    "yield TEXT, "+
                    "totalpicks TEXT)");

            database.execSQL("CREATE TABLE cropmastertype(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "croptype TEXT," +
                    "croptype_eng TEXT, "+
                    "croptype_kn TEXT )");

            database.execSQL("CREATE TABLE cropmaster(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cropcode TEXT," +
                    "cropname_eng TEXT, "+
                    "cropname_kn TEXT,"+
                    "croptype TEXT, "+
                    "intercroptype TEXT, "+
                    "cropcategory TEXT, "+
                    "cropgroup TEXT, "+
                    "crop_link_code TEXT)");

            database.execSQL("CREATE TABLE fertilizercropmaster(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cropcode TEXT," +
                    "cropname_eng TEXT, "+
                    "cropname_kn TEXT, "+
                    "croptype TEXT)");

            database.execSQL("CREATE TABLE plantagemaster(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "plantid TEXT," +
                    "plantname TEXT)");

            database.execSQL("CREATE TABLE fertilizernamemaster(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "feid TEXT," +
                    "fertilizername TEXT, "+
                    "fertilizerknname TEXT, "+
                    "fertilizertype TEXT, "+
                    "fertilizernitrogen TEXT," +
                    "fertilizerphosphorous TEXT, "+
                    "fertilizerpotash TEXT, "+
                    "fertilizernutrient TEXT)");

            database.execSQL("CREATE TABLE cropfertilizermaster(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cf_id TEXT," +
                    "cf_crop_code TEXT, "+
                    "cf_irrigation_type TEXT, "+
                    "cf_plant_age TEXT," +
                    "cf_nitrogen TEXT, "+
                    "cf_phosphorous TEXT, "+
                    "cf_potash TEXT)");

            database.execSQL("CREATE TABLE cropmastertype(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "croptype TEXT," +
                    "croptype_eng TEXT, "+
                    "croptype_kn TEXT)");

            database.execSQL("CREATE TABLE irrigationtype(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "irrigationtype TEXT," +
                    "irrigationtypeeng TEXT, "+
                    "irrigationtypekn TEXT)");

          /*  database.execSQL("CREATE TABLE farmfertilizer(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "farmerid TEXT," +
                    "farmerid TEXT, "+
                    "year TEXT, "+
                    "season TEXT, "+
                    "cropname TEXT, "+
                    "cropcode TEXT, "+
                    "cropextent TEXT, "+
                    "croptype TEXT, "+
                    "district TEXT, "+
                    "taluk TEXT, "+
                    "hobli TEXT, "+
                    "village TEXT, "+
                    "survey TEXT, "+
                    "recommendedNPK TEXT, "+
                    "cropextentNPK TEXT, "+
                    "totalNPK TEXT, "+
                    "fertilizerNames TEXT, "+
                    "fertilizerKGs TEXT, "+
                    "fertilizerBags TEXT)");*/

        }
    };

   /* static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        public void migrate(SupportSQLiteDatabase database) {
            // Your migration strategy here

            database.execSQL("ALTER TABLE farmer_land_details ADD COLUMN ownernumber TEXT");
            database.execSQL("ALTER TABLE farmer_land_details ADD COLUMN mainownerNumber TEXT");
            database.execSQL("ALTER TABLE farmer_land_details ADD COLUMN relativename TEXT");
         *//*   database.execSQL("CREATE TABLE 'cropvarietymaster' ('id' INTEGER,  "
                    + " 'cropcode' INTEGER, 'cropvariety' String, 'cropsowingduration' String, 'periodicitypicking' String,'totalpicks' INTEGER,'numberofdayfirstpicking' INTEGER,'expyieldinfirstpick' INTEGER, PRIMARY KEY('id'))");
*//*
        }
    };*/

    public abstract LandDetailsDao landDetailsDAO();
    public abstract CropDetailsDAO cropDetailsDAO();
    public abstract FarmerDetailsDAO farmerDetailsDAO();
    public abstract PaymentDetailsDAO paymentDetailsDAO();
    public abstract WeatherDetailsDAO weatherDetailsDAO();
    public abstract ForecastDetailsDAO forecastDetailsDAO();
   // public abstract CropMasterDao cropMasterDao();
    public abstract FarmerOwnerDetails ownerDetailsDAO();
  //  public abstract CropVarietyMasterDao cropVarietyMasterDao();
    public abstract CropRegistrationDao cropRegistrationDao();
    public abstract CropWeeklyDataDAO cropWeeklyDataDAO();
    public abstract MixedCropRegistrationDao mixedCropRegistrationDao();
    public abstract InterCropRegistrationDao interCropRegistrationDao();
    public abstract CropMultipickingDataDAO cropmultipickDataDAO();
    public abstract CropMasterType cropMasterTypeDao();
    public abstract CropMasterDao cropMasterDao();
    public abstract FertilizerCropMasterDAO fertilizerCropMasterDAO();
    public abstract PlantAgeMasterDAO plantAgeMasterDAO();
    public abstract FertilizerNameMasterDAO fertilizerNameMasterDAO();
    public abstract CropFertilizerMasterDAO cropFertilizerMasterDAO();
    public abstract IrrigationTypeDao irrigationTypeDao();
 //   public abstract FarmFertilizerDAO farmFertilizerDAO();
}

