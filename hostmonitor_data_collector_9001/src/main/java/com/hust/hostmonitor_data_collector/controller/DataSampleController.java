package com.hust.hostmonitor_data_collector.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hust.hostmonitor_data_collector.service.DataCollectorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.Map;

@Controller
public class DataSampleController {
    @Resource
    DataCollectorService dataCollectorService;

    @GetMapping(value="/getSummary/Dashboard")
    @ResponseBody
    public String getSummary_Dashboard(){
        String result= dataCollectorService.getDashboardSummary();
        return result;
    }


    @GetMapping(value="/getHostInfo/All/Dashboard")
    @ResponseBody
    public String getHostInfo_All_Dashboard(){
        String result= dataCollectorService.getHostInfoDashboardAll();
        return result;
    }

    @GetMapping(value="/getDiskInfo/All/Dashboard")
    @ResponseBody
    public String getDiskInfo_All_Dashboard(){
        String result= dataCollectorService.getDiskInfoAll();
        return result;
    }

    @GetMapping(value="/getHostInfo/HostDetail/{Ip}")
    @ResponseBody
    public String getHostInfo_HostDetail(@PathVariable Map<String,String> map){
        String result= dataCollectorService.getHostInfoDetail(map.get("Ip"));
        return result;
    }
    @GetMapping(value = "/getHostInfo/AllHostDetail/")
    @ResponseBody
    public String getHostInfo_AllHostDetail(){
        String result=dataCollectorService.getAllHostsInfoDetail();
        return result;
    }

    @GetMapping(value="/getHostInfo/Trend/HostDetail/{Ip}")
    @ResponseBody
    public String getHostInfo_Trend_HostDetail(@PathVariable Map<String,String> map){
        String result= dataCollectorService.getHostInfoDetailTrend(map.get("Ip"));
        return result;
    }


    @GetMapping(value="/getDiskInfo/{Ip}")
    @ResponseBody
    public String getDiskInfo(@PathVariable Map<String,String> map){
        String result= dataCollectorService.getDiskInfo(map.get("Ip"));
        return result;
    }

    @GetMapping(value="/getSpeedMeasurementInfo/All")
    @ResponseBody
    public String getSpeedMeasurementInfo_All(){
        String result= dataCollectorService.getSpeedMeasurementInfoAll();
        return result;
    }

    @GetMapping(value="/getDFPSummaryInfo")
    @ResponseBody
    public String getDFPSummaryInfo(){
        String result= dataCollectorService.getDFPSummary();
        return result;

    }

    @GetMapping(value="/getDFPInfo/Trend/{Ip}/{diskName}")
    @ResponseBody
    public String getDFPInfoTrend(@PathVariable Map<String,String> map){
        String result= dataCollectorService.getDFPInfoTrend(map.get("Ip"),map.get("diskName"));
        return result;
    }

    @GetMapping(value="/getDFPInfo/List")
    @ResponseBody
    public String getDFPInfoList(){
        String result= dataCollectorService.getDFPInfoAll();
        return result;
    }

    @PostMapping(value="/dfpTrain",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String dfpTrain(@RequestBody JSONObject jsonParam){
        //服务端身份验证-管理员
        String userID = jsonParam.getString("userID");
        String password = jsonParam.getString("password");
        if(!dataCollectorService.userAuthoirtyCheck(userID,password,1)){
            return "Permission Denied";
        }
        //数据预处理
        //TODO 对传回的数据进行检验
        float positiveDataProportion = jsonParam.getFloat("positiveDataProportion");
        float negativeDataProportion = jsonParam.getFloat("negativeDataProportion");
        float verifyProportion = jsonParam.getFloat("verifyProportion");
        //模型具体参数
        int modelType = jsonParam.getInteger("modelType");
        JSONObject extraParams= new JSONObject();
        if(modelType == 1){
            JSONArray maxDepth = jsonParam.getJSONArray("maxDepth");
            JSONArray maxFeatures = jsonParam.getJSONArray("maxFeatures");
            JSONArray nEstimators = jsonParam.getJSONArray("nEstimators");
            extraParams.put("max_depth",maxDepth);
            extraParams.put("max_features",maxFeatures);
            extraParams.put("n_estimators",nEstimators);
            System.out.println("[模型训练]开始训练");
            dataCollectorService.train(modelType,positiveDataProportion,negativeDataProportion,verifyProportion,extraParams,userID);
        }
        return "Train";
    }

    @GetMapping(value="/getDFPTrainProgress")
    @ResponseBody
    public String getDFPTrainProgress(){
        return dataCollectorService.getTrainProgress().toString();
    }

    @GetMapping(value="/getDFPTrainRecord/List")
    @ResponseBody
    public String getDFPTrainRecordList(){
        String string= dataCollectorService.getDFPTrainList();
        return string;
    }
    @GetMapping(value = "/getHostsRouter")
    @ResponseBody
    public String getHostsRouterInfo(){
        String string=dataCollectorService.getHostsRouterInfo();
        return string;
    }

    @PostMapping(value="/diskSpeedTest",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String remoteTest(@RequestBody JSONObject jsonParam){
        String nodeIp=jsonParam.getString("IP");
        String result=dataCollectorService.remoteTest(nodeIp);
        return result;
    }


    //TODO 真实磁盘损坏标记
    @PostMapping(value="/getDFPInfo/setDiskState",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setDiskState(@RequestBody JSONObject jsonParam){
        String diskSerial= jsonParam.getString("diskSerial");
        boolean state= jsonParam.getBoolean("failureSymbol");
        String result=dataCollectorService.setDiskState(diskSerial,state);
        return result;
    }

    @GetMapping(value = "/test2")
    @ResponseBody
    public String test(){
        return dataCollectorService.test();
    }

    @PostMapping(value="/addHostNode",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addHostNode(@RequestBody JSONObject jsonParam) throws FileNotFoundException {
        //服务端身份验证-管理员
        String webUserID = jsonParam.getString("WebUserID");
        String webPassword = jsonParam.getString("WebUserPassword");
        if(!dataCollectorService.userAuthoirtyCheck(webUserID,webPassword,1)){
            return "false";
        }

        String hostIP = jsonParam.getString("HostIP");
        String osType = jsonParam.getString("OsType");
        String userName = jsonParam.getString("UserName");
        String userType = jsonParam.getString("UserType");
        String userPassword = jsonParam.getString("UserPassword");
        String proxyType = jsonParam.getString("ProxyType");
        String proxyIP = jsonParam.getString("ProxyIP");
        String routerIP = jsonParam.getString("RouterIP");

        System.out.println(webUserID);
        System.out.println(webPassword);
        System.out.println(hostIP);
        System.out.println(osType);
        System.out.println(userName);
        System.out.println(userType);
        System.out.println(userPassword);
        System.out.println(proxyType);
        System.out.println(proxyIP);
        System.out.println(routerIP);

        return dataCollectorService.addHostNode(jsonParam);
    }
}
