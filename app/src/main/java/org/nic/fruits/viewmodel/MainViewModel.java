package org.nic.fruits.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.nic.fruits.pojo.ModelCropFertilizerMasternpk;
import org.nic.fruits.pojo.ModelCropMaster;
import org.nic.fruits.pojo.ModelCropMasterType;
import org.nic.fruits.pojo.ModelCropWeeklyData;
import org.nic.fruits.pojo.ModelCropRegistration;
import org.nic.fruits.pojo.ModelCropVarietyMaster;
import org.nic.fruits.pojo.ModelFarmFertilizer;
import org.nic.fruits.pojo.ModelFertilizerCropMaster;
import org.nic.fruits.pojo.ModelFertilizerNameMaster;
import org.nic.fruits.pojo.ModelInterCropRegistration;
import org.nic.fruits.pojo.ModelIrrigationType;
import org.nic.fruits.pojo.ModelMixedCropRegistration;
import org.nic.fruits.pojo.ModelOwnerDetails;
import org.nic.fruits.pojo.ModelPlantAgeMaster;
import org.nic.fruits.pojo.ModelweatherDetails;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.pojo.ModelCropSurveyDetails;
import org.nic.fruits.pojo.ModelFarmerDetails;
import org.nic.fruits.pojo.ModelFarmerLandDeatails;
import org.nic.fruits.pojo.ModelForeCastDetails;
import org.nic.fruits.pojo.ModelPaymentDetails;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<ModelFarmerLandDeatails>> lands;
    private LiveData<List<ModelCropSurveyDetails>> cropSurveys;
    private LiveData<List<ModelFarmerDetails>> farmerDetails;
    private LiveData<List<ModelPaymentDetails>> payments;
    private LiveData<List<ModelweatherDetails>> weather;
    private LiveData<List<ModelForeCastDetails>> forecast;
    //   private LiveData<List<ModelCropMaster>> cropmaster;
    private LiveData<List<ModelOwnerDetails>> ownerdetails;
    private LiveData<List<ModelCropVarietyMaster>> cropvariety;
    private LiveData<List<ModelCropRegistration>> cropregistration;
    private LiveData<List<ModelCropRegistration>> cropregcriddata;
    private LiveData<String> cropsavedsurveynum;
    private LiveData<List<ModelCropWeeklyData>> cropweeklydata;
    private LiveData<List<ModelCropWeeklyData>> getweek;
    private LiveData<List<ModelMixedCropRegistration>> mixedCropRegistration;
    private LiveData<List<ModelInterCropRegistration>> interCropRegistration;
    private LiveData<List<ModelCropMasterType>> cropmastertype;
    private LiveData<List<ModelCropMaster>> cropmaster;
    private LiveData<List<ModelFertilizerCropMaster>> fertilizercropmaster;
    private LiveData<List<ModelPlantAgeMaster>> plantagemaster;
    private LiveData<List<ModelFertilizerNameMaster>> fertilizernamemaster;
    private LiveData<List<ModelCropFertilizerMasternpk>> cropfertilizermaster;
    private LiveData<List<ModelIrrigationType>> irrigationtypemaster;
//    private LiveData<List<ModelFarmFertilizer>> farmfertilizermaster;
    private AppDatabase database;

    public MainViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
      /*  lands = database.landDetailsDAO().loadAllLandDetails();
        cropSurveys = database.cropDetailsDAO().loadAllCropDetails();
        farmerDetails = database.farmerDetailsDAO().loadAllFarmerData();
        payments = database.paymentDetailsDAO().loadAllPaymentDetails();
        weather = database.weatherDetailsDAO().loadAllWeatherDetails();
        forecast = database.forecastDetailsDAO().loadAllForeCastDetails();*/
    }

   /* public LiveData<List<ModelFarmerLandDeatails>> getLandDetails() {
        return lands;
    }
    public LiveData<List<ModelCropSurveyDetails>> getCropSurveyDetails() {
        return cropSurveys;
    }
    public LiveData<List<ModelFarmerDetails>> getFarmerDetails() {
        return farmerDetails;
    }
    public LiveData<List<ModelPaymentDetails>> getPaymentDetails() {
        return payments;
    }
    public LiveData<List<ModelweatherDetails>> getWeatherDetails() {
        return weather;
    }
    public LiveData<List<ModelForeCastDetails>> getForeCastDetails() {
        return forecast;
    }*/

    public LiveData<List<ModelFarmerLandDeatails>>  getLandBasedOnFid(String farmerID)
    {
        lands = database.landDetailsDAO().getAllFarmerLandBasedOnFid(farmerID);
        return lands;
    }

    public LiveData<List<ModelFarmerDetails>>  getFarmerDetailsBasedOnFid(String farmerID)
    {
        farmerDetails = database.farmerDetailsDAO().getAllFarmerDetailsBasedOnFid(farmerID);
        return farmerDetails;
    }

    public  LiveData<List<ModelCropSurveyDetails>> getCropSurveBasedOnFid(String farmerID)
    {
        cropSurveys = database.cropDetailsDAO().getAllFarmerCropBasedOnFid(farmerID);
        return cropSurveys;
    }


    public LiveData<List<ModelPaymentDetails>>  getgetPaymentDetailsBasedOnFid(String farmerID)
    {
        payments = database.paymentDetailsDAO().getAllPaymentDetailsBasedOnFid(farmerID);
        return payments;
    }

    public LiveData<List<ModelweatherDetails>> getWeatherDetailsBasedOnFid(String farmerID)
    {
        weather = database.weatherDetailsDAO().getAllWeatherDetailsBasedOnFid(farmerID);
        return weather;
    }

    public LiveData<List<ModelForeCastDetails>> getgetForeCastDetailsBasedOnFid(String farmerID)
    {
        forecast = database.forecastDetailsDAO().getAllForeCastDetailsBasedOnFid(farmerID);
        return forecast;
    }

    public LiveData<List<ModelForeCastDetails>> getForeCastDetailsBasedOnVillageName(String villageName)
    {
        forecast = database.forecastDetailsDAO().getAllForeCastDetailsBasedOnVillageName(villageName);
        return forecast;
    }

    public LiveData<List<ModelweatherDetails>> getWeatherDetailsBasedOnVillageName(String villageName)
    {
        weather = database.weatherDetailsDAO().getAllModelweatherDetailsBasedOnVillageName(villageName);
        return weather;
    }

    /*public  LiveData<List<ModelCropMaster>> getCropBasedOnCropName(String cropname,String multipick)
    {
        cropmaster = database.cropMasterDao().getcropsbasedoncropnameAndmultipick(cropname,multipick);
        return cropmaster;
    }

    public  LiveData<List<ModelCropMaster>> getAllcrops()
    {
        cropmaster = database.cropMasterDao().getAllcrops();
        return cropmaster;
    }*/

    public LiveData<List<ModelOwnerDetails>> getOwnerdetails(String farmerID,String surveySpinnerValue)
    {
        ownerdetails = database.ownerDetailsDAO().getAllOwnerBasedOnFidsurvey(farmerID,surveySpinnerValue);
        return ownerdetails;
    }

    public LiveData<List<ModelCropRegistration>> getCropregistration(String farmerID)
    {
        cropregistration = database.cropRegistrationDao().getCropRegistrationDetailsBasedOnFid(farmerID);
        return cropregistration;
    }

    public LiveData<List<ModelMixedCropRegistration>> getMixedCropregistration(String farmerID)
    {
        mixedCropRegistration = database.mixedCropRegistrationDao().getMixedCropRegistrationDetailsBasedOnFid(farmerID);
        return mixedCropRegistration;
    }

    public LiveData<List<ModelInterCropRegistration>> getInterCropregistration(String farmerID)
    {
        interCropRegistration = database.interCropRegistrationDao().getInterCropRegistrationDetailsBasedOnFid(farmerID);
        return interCropRegistration;
    }

    public LiveData<List<ModelCropRegistration>> getCroprgisteredforweeklydata(String farmerID, String crID)
    {
        cropregcriddata = database.cropRegistrationDao().getCropRegistrationDetailsBasedOnFidCrid(farmerID,crID);
        return cropregcriddata;
    }

    public LiveData<List<ModelCropWeeklyData>> getWeek(String farmerID, String crID,String surveynum,String yearcode,String seasoncode,String ownerid,String cropname)
    {
        getweek = database.cropWeeklyDataDAO().getWeek(farmerID,crID,surveynum,yearcode,seasoncode,ownerid,cropname);
        return getweek;
    }

    public LiveData<List<ModelCropWeeklyData>> getCropweeklydata(String farmerID, String crID)
    {
        cropweeklydata = database.cropWeeklyDataDAO().getCropWeeklyDataBasedonfidcrid(farmerID,crID);
        return cropweeklydata;
    }
    public LiveData<List<ModelCropMasterType>> getCropMastertype()
    {
        cropmastertype = database.cropMasterTypeDao().getAllcroptype();
        return cropmastertype;
    }

    public LiveData<List<ModelCropMaster>> getCropmastercropcode(String cropcode)
    {
        cropmaster = database.cropMasterDao().getcropcode(cropcode);
        return cropmaster;
    }

    public LiveData<List<ModelCropMaster>> getCropmastercropcodecroptype(String cropcode,String croptype)
    {
        cropmaster = database.cropMasterDao().getcropbasedoncropcodecroptype(cropcode,croptype);
        return cropmaster;
    }

    public LiveData<List<ModelCropMaster>> getCropmastercropcodecroptypeintercrop(String cropcode,String croptype,String intercrop)
    {
        cropmaster = database.cropMasterDao().getcropbasedoncropcodecroptypeintercroptype(cropcode,croptype,intercrop);
        return cropmaster;
    }
    public LiveData<List<ModelCropMaster>> getAllcrops()
    {
        cropmaster = database.cropMasterDao().getAllcrops();
        return cropmaster;
    }
    public LiveData<List<ModelCropMaster>> getMixedCrops(String Seasonalcrop)
    {
        cropmaster = database.cropMasterDao().getMixedcrops(Seasonalcrop);
        return cropmaster;
    }

    public LiveData<List<ModelFertilizerCropMaster>> getFertilizercropmaster()
    {
        fertilizercropmaster = database.fertilizerCropMasterDAO().getAllcrops();
        return fertilizercropmaster;
    }

    public LiveData<List<ModelFertilizerCropMaster>> getallcropsf()
    {
        fertilizercropmaster = database.fertilizerCropMasterDAO().getAllcrops();

        return fertilizercropmaster;
    }
    public LiveData<List<ModelFertilizerCropMaster>> getcropsbasedontype(String croptype)
    {
        fertilizercropmaster = database.fertilizerCropMasterDAO().getCropType(croptype);
        System.out.print("fertilizer " + fertilizercropmaster);
        return fertilizercropmaster;
    }

    public LiveData<List<ModelPlantAgeMaster>> getAllPlants(String plantid)
    {
        plantagemaster = database.plantAgeMasterDAO().getplantagebasedonpid(plantid);
        return plantagemaster;
    }

    public LiveData<List<ModelFertilizerNameMaster>> getAllFertilizerNames(String fertilizertype)
    {
        fertilizernamemaster = database.fertilizerNameMasterDAO().getAllFertilizerNamesBasedontype(fertilizertype);
        return fertilizernamemaster;
    }

    public LiveData<List<ModelFertilizerNameMaster>> getAllFertilizerID(String fid)
    {
        fertilizernamemaster = database.fertilizerNameMasterDAO().getAllFertilizerBasedonid(fid);
        return fertilizernamemaster;
    }

    public LiveData<List<ModelCropFertilizerMasternpk>> getAllCropFertilizerMasternpk(String cropcode)
    {
        cropfertilizermaster = database.cropFertilizerMasterDAO().getAllcropbasedcropcode(cropcode);
        return cropfertilizermaster;
    }

    public LiveData<List<ModelCropFertilizerMasternpk>> getNutrients(String cropcode,String irrigationtype,String plantage)
    {
        cropfertilizermaster = database.cropFertilizerMasterDAO().getnutrients(cropcode,irrigationtype,plantage);
        return cropfertilizermaster;
    }

    public LiveData<List<ModelCropFertilizerMasternpk>> getFertilizers(String cropcode,String irrigationtype,String plantage)
    {
        cropfertilizermaster = database.cropFertilizerMasterDAO().getfertilizers(cropcode,irrigationtype,plantage);
        return cropfertilizermaster;
    }

    public LiveData<List<ModelIrrigationType>> getirrigationtype()
    {
        irrigationtypemaster = database.irrigationTypeDao().getAllirrigationtype();
        return irrigationtypemaster;
    }

    public  LiveData<List<ModelCropSurveyDetails>> getSeason(String farmerID,String year)
    {
        cropSurveys = database.cropDetailsDAO().getSeason(farmerID,year);
        return cropSurveys;
    }

    public  LiveData<List<ModelCropSurveyDetails>> getCropDetails(String farmerID,String year,String season)
    {
        cropSurveys = database.cropDetailsDAO().getCropsDetails(farmerID,year,season);
        return cropSurveys;
    }

    public LiveData<List<ModelFertilizerCropMaster>> getFertilizerCrops(String cropname)
    {
        fertilizercropmaster = database.fertilizerCropMasterDAO().getCropDetails(cropname);
        return fertilizercropmaster;
    }

    public LiveData<List<ModelCropFertilizerMasternpk>> getCropNPK(String cropcode)
    {
        cropfertilizermaster = database.cropFertilizerMasterDAO().getCropNPKdetails(cropcode);
        return cropfertilizermaster;
    }

    public LiveData<List<ModelFertilizerNameMaster>> getFertilizerNPK( String fertilizerid)
    {
        fertilizernamemaster = database.fertilizerNameMasterDAO().getFertilizerNPK(fertilizerid);
        return fertilizernamemaster;
    }


/*    public LiveData<List<ModelFarmFertilizer>> getFarmFertlizerData(String fID,String district,String taluk,String hobli,String village,String survey,String crop)
    {
        farmfertilizermaster = database.farmFertilizerDAO().getData(fID,district,taluk,hobli,village,survey,crop);
        return farmfertilizermaster;
    }*/


    /*public  LiveData<List<ModelCropVarietyMaster>> getCropVarietyBasedOnCropCode(int cropCode)
    {
        cropvariety = database.cropVarietyMasterDao().getAllCropVarietyBasedonCropCode(cropCode);
        return cropvariety;
    }*/
}