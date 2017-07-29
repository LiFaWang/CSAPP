package huansi.net.qianjingapp.utils;

import android.content.Context;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Map;


public class WebServices {
	public static final String CUS_SERVICE_IP="";
	public static final String CUS_SERVICE_CHECK_CODE="";
    //本机
   public static final String HS_SERVICE_IP="http://192.168.0.60/GU/APPWS.asmx";
   //public static final String HS_SERVICE_IP="http://192.168.10.9:8011/GHIot/APPWS.asmx";
	public static final String HS_SERVICE_CHECK_CODE="4A8D54D1-C7A1-48DC-AD7E-8CA1686FBF85";

	/**
	 * 服务器的类型
	 */
	public enum WebServiceType{
		HS_SERVICE,//环思服务器
		CUS_SERVICE,//前进app服务器

	}

	public String FEndPoint = "";
	public String FNameSpace ="http://tempuri.org/";
	public String FCheckCode = "";

	//---------------------------------------------------------------------------------------
	public WebServices() {
//		this(WebServiceType.CUS_SERVICE);
	}

	/**
	 *
	 * @param serviceTpe
     */
	public WebServices(WebServiceType serviceTpe, Context context) {
		switch (serviceTpe){
			//环思服务器
			case HS_SERVICE:
//				String ip = SPUtils.readMacIp(context);
//				FEndPoint = "http://192.168.10.9:8011/GHIot/APPWS.asmx";
				FEndPoint  = "http://183.134.73.181:3082/HSIOTAPPWS/appws.asmx";
//				FEndPoint = HS_SERVICE_IP;
				FCheckCode = HS_SERVICE_CHECK_CODE;
				break;
			//定制app服务器
			case CUS_SERVICE:
				FEndPoint = CUS_SERVICE_IP;
				FCheckCode =CUS_SERVICE_CHECK_CODE;
				break;
		}
	}


	public String getData(String functionName, Map<String,String> parameter) {
	    // 命名空间xx
	    String nameSpace = FNameSpace;
	    // 调用的方法名称  
	    String methodName = functionName;
	    // EndPoint  
	    String endPoint = FEndPoint;
	    // SOAP Action  
	    String soapAction = nameSpace + methodName;
	 
	    // 指定WebService的命名空间和调用的方法名  
	    SoapObject rpc = new SoapObject(nameSpace, methodName);  
	 
	    // 设置需调用WebService接口需要传入的两个参数mobileCode、userId  
//	    rpc.addProperty("sCheckCode", FCheckCode);
//	    rpc.addProperty("sUserNo", sUserNo);
//	    rpc.addProperty("sPassword", sPassword);
		String sParmName="";
		String sParaValue="";
		rpc.addProperty("sCheckCode", FCheckCode);
		if(parameter!=null) {
			for (Map.Entry<String, String> entry : parameter.entrySet()) {
				sParmName = entry.getKey();
				sParaValue = entry.getValue();
				rpc.addProperty(sParmName, sParaValue);
			}
		}
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
	    envelope.bodyOut = rpc;
	    // 设置是否调用的是dotNet开发的WebService  
	    envelope.dotNet = true;
	    // 等价于envelope.bodyOut = rpc;
	    envelope.setOutputSoapObject(rpc);
	    HttpTransportSE transport = new HttpTransportSE(endPoint,1000*60);
	    try {
	        // 调用WebService  
	        transport.call(soapAction, envelope);
			// 获取返回的数据
			SoapObject object = (SoapObject) envelope.bodyIn;
//			SoapObject object=envelope.sObject.getProperty("return");
//			SoapObject soReturn = (SoapObject)object.getProperty("return");
//			soReturn.getProperty("insurer").toString();
			String result = "";
			// 获取返回的结果
			if(object!=null) {
				result = object.getProperty(0).toString();
			}
			return (result==null||result.isEmpty()||result.equals("anyType{}"))?"":result;
	    } catch (Exception e) {
	        e.printStackTrace();
			return "";
	    }
	}
}
