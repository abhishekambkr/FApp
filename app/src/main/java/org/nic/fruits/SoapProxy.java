package org.nic.fruits;

import android.content.Context;
import android.os.StrictMode;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



//Programming by Harsha  for version 1.0 release
public class SoapProxy {

    private Context context;
    String version;
    private int TimeOut = 5000000;
    String URL = "https://fruits.karnataka.gov.in/FRUITSAPPService/MOBILEDATA.ASMX";

    public SoapProxy(Context context) {
        this.context = context;
    }


    public String getOTP(String KeyValue, String MobileNo, String AadharNumber) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "ValidateMobileUser");
            request.addProperty("KeyValue", KeyValue);
            request.addProperty("MobileNo", MobileNo);
            request.addProperty("AadharNumber", AadharNumber);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, TimeOut);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/ValidateMobileUser", envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("ValidateMobileUserResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                System.out.println("str      " + str);
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String getPaymentDetails(String keyValue, String farmerId) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "GetFarmerPaymentData");
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/GetFarmerPaymentData", envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("GetFarmerPaymentDataResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                System.out.println("str      " + str);
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String getLandDetails(String keyValue, String farmerId) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "GetFarmerLandData ");
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/GetFarmerLandData ", envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("GetFarmerLandDataResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                System.out.println("str " + str);
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String CheckNPCStatus(String keyValue, String farmerId) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "CheckNPCISatatus");
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/CheckNPCISatatus", envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("CheckNPCISatatusResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                System.out.println("str      " + str);
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String getWeatherData(String keyValue, String farmerId) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "GetWeatherData ");
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);

            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/GetWeatherData ", envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("GetWeatherDataResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                System.out.println("str      " + str);
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String getFarmerData(String keyValue, String farmerId) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "GetFarmerData ");
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/GetFarmerData ", envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("GetFarmerDataResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                System.out.println("str      " + str);
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String getCropSurveyData(String keyValue, String farmerId) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "GetFarmerCropSurveyData");
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/GetFarmerCropSurveyData ", envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("GetFarmerCropSurveyDataResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                System.out.println("str      " + str);
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String getSyncFarmerData(String keyValue, String farmerId) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "GetSynchronizeFarmerData");
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/GetSynchronizeFarmerData ", envelope);

            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("GetSynchronizeFarmerDataResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                System.out.println("str      " + str);
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String getSynchronizeFarmerLandData(String keyValue, String farmerId) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "GetSynchronizeFarmerLandData");
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);

            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/GetSynchronizeFarmerLandData ", envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("GetSynchronizeFarmerLandDataResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                System.out.println("str      " + str);
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String getSynchronizePaymentDetails(String keyValue, String farmerId) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "GetSynchronizeFarmerPaymentData");
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/GetSynchronizeFarmerPaymentData ", envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("GetSynchronizeFarmerPaymentDataResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                System.out.println("str      " + str);
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String sendTextFeedBack(String keyValue, String farmerId, String farmerName, String feedbackText) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "SaveTextFeedBack");
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            request.addProperty("FarmerName", farmerName);
            request.addProperty("Data", feedbackText);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);

            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/SaveTextFeedBack", envelope);
            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("SaveTextFeedBackResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }

    public String sendVoiceFeedBack(String keyValue, String farmerId, String farmerName, byte[] feedbackVoice) {
        String version = null;
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "SaveAudioFeedBack");
            System.out.println("calling aa sendVoiceFeedBack" + keyValue + " " + farmerId + " " + farmerName + " " + feedbackVoice);
            request.addProperty("KeyValue", keyValue);
            request.addProperty("FarmerID", farmerId);
            request.addProperty("FarmerName", farmerName);
            request.addProperty("Data", feedbackVoice);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            new MarshalBase64().register(envelope);
            envelope.encodingStyle = SoapEnvelope.ENC;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SSLConection.allowAllSSL();
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL, TimeOut);

            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/SaveAudioFeedBack", envelope);

            if (envelope.bodyIn instanceof SoapObject) {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                version = soapObject.getProperty("SaveAudioFeedBackResult").toString();
                System.out.println(version);
            } else if (envelope.bodyIn instanceof SoapFault) {
                @SuppressWarnings("unused")
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                version = "Failureerror";
            }
        } catch (Exception e) {
            e.printStackTrace();
            version = "Failure1";
        }
        return version;
    }
}
