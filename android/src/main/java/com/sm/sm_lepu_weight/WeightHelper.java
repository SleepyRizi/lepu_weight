package com.sm.sm_lepu_weight;

import android.content.Context;
import android.util.Log;

import java.util.Locale;

import io.flutter.plugin.common.EventChannel;
import org.json.JSONObject;

import static cn.icomon.icdevicemanager.model.other.ICConstant.ICPeopleType.ICPeopleTypeNormal;
import cn.icomon.icdevicemanager.ICDeviceManager;
import cn.icomon.icdevicemanager.ICDeviceManagerDelegate;
import cn.icomon.icdevicemanager.ICDeviceManagerSettingManager;
import cn.icomon.icdevicemanager.callback.ICScanDeviceDelegate;
import cn.icomon.icdevicemanager.model.data.ICCoordData;
import cn.icomon.icdevicemanager.model.data.ICKitchenScaleData;
import cn.icomon.icdevicemanager.model.data.ICRulerData;
import cn.icomon.icdevicemanager.model.data.ICSkipData;
import cn.icomon.icdevicemanager.model.data.ICSkipFreqData;
import cn.icomon.icdevicemanager.model.data.ICWeightCenterData;
import cn.icomon.icdevicemanager.model.data.ICWeightData;
import cn.icomon.icdevicemanager.model.data.ICWeightHistoryData;
import cn.icomon.icdevicemanager.model.device.ICDevice;
import cn.icomon.icdevicemanager.model.device.ICDeviceInfo;
import cn.icomon.icdevicemanager.model.device.ICScanDeviceInfo;
import cn.icomon.icdevicemanager.model.device.ICUserInfo;
import cn.icomon.icdevicemanager.model.other.ICConstant;
import cn.icomon.icdevicemanager.model.other.ICDeviceManagerConfig;

public class WeightHelper
implements  ICScanDeviceDelegate, ICDeviceManagerDelegate, EventMgr.Event   ,EventChannel.StreamHandler {

    private final   Context applicationContext;

    private EventChannel.EventSink events;


    WeightHelper(  Context applicationContext){

        this.applicationContext =applicationContext;
    };
    int height = 170;
    int age = 24;
    int sex = 1;
    Double currentWeight=0.0;

    ICScanDeviceInfo _deviceInfo;
    ICDevice device;
    String connectionStateName ="";




    @Override
    public void onListen(Object args, EventChannel.EventSink events) {
        this.events = events;
        try {
            JSONObject o = new JSONObject();
            o.put("connectState", connectionStateName);
            o.put("isConnected", "Connected".equals(connectionStateName));
            o.put("isFinished", false);
            o.put("hasData", false);
            o.put("msg", "");
            o.put("weight", "0.00");
            events.success(o.toString());
        } catch (Exception ignored) {}
    }

    @Override
    public void onCancel(Object args) {
    }

    public void init(){




        EventMgr.addEvent("SCAN", this);

        initSDK();



    }


    void initSDK() {
        ICDeviceManagerConfig config = new ICDeviceManagerConfig();
        config.context = applicationContext;

        ICUserInfo userInfo = new ICUserInfo();
        userInfo.age = age;
        userInfo.height = height;
        userInfo.sex = ICConstant.ICSexType.ICSexTypeMale;
        userInfo.peopleType = ICPeopleTypeNormal;
        ICDeviceManager.shared().setDelegate(this);
        ICDeviceManager.shared().updateUserInfo(userInfo);

        ICDeviceManager.shared().initMgrWithConfig(config);
    }

    @Override
    public void onCallBack(Object obj) {
        _deviceInfo = (ICScanDeviceInfo) obj;
        if (device == null)
            device = new ICDevice();
        device.setMacAddr(_deviceInfo.getMacAddr());


        ICDeviceManager.shared().addDevice(device, new ICConstant.ICAddDeviceCallBack() {
            @Override
            public void onCallBack(ICDevice device, ICConstant.ICAddDeviceCallBackCode code) {




                  addLog("device state : " + code.name()+"\n code num: "+code.ordinal());



                if(code.ordinal()==0){
                    connectionStateName ="Connected";

                }
                else if(code.ordinal()==1){
                    connectionStateName ="Disconnected";


                }
                else{
                    connectionStateName ="Unknown state";

                }


             //   addLog( " device  state :" + stateName);

            }
        });
    }


    void addLog(String log) {
        // String srcLog = txt_log.getText().toString();
        // srcLog += "\r\n";
        // srcLog += log;
//        txt_log.setText(log);


        try {


        JSONObject inputObject = new JSONObject();
        inputObject.put("connectState", connectionStateName);
        inputObject.put("isConnected", connectionStateName=="Connected");
        inputObject.put("isFinished", false);
        inputObject.put("msg", log);
        inputObject.put("weight", currentWeight.toString());
        Log.d("logdata",log);

        if (events != null) {
            events.success(inputObject.toString());
        }


        }catch  (Exception var5) {
            Log.i("erorr",var5.getMessage());

        }
    }





    @Override
    public void onScanResult(ICScanDeviceInfo deviceInfo) {

    }

    @Override
    public void onInitFinish(boolean bSuccess) {
        addLog("SDK init result:" + bSuccess);


    }

    @Override
    public void onBleState(ICConstant.ICBleState state) {
        addLog("ble state:" + state.name());
        final int[] index = { 0 };
        if (state == ICConstant.ICBleState.ICBleStatePoweredOn) {


        }

        else if(state == ICConstant.ICBleState.ICBleStatePoweredOff){

            connectionStateName="PoweredOff";

            addLog("ble state:" + state.name());

        }


    }

    @Override
    public void onDeviceConnectionChanged(final ICDevice device, final ICConstant.ICDeviceConnectState state) {

        String stateName="";

        if(state.ordinal()==0){
            stateName="Connected";

        }
        else if(state.ordinal()==1){
            stateName="Disconnected";


        }
        else{
            stateName="Unknown state";

        }


        addLog( " connect state :" + stateName);


    }

    @Override
    public void onNodeConnectionChanged(ICDevice device, int nodeId, ICConstant.ICDeviceConnectState state) {

    }

    @Override
    public void onReceiveWeightData(ICDevice device, ICWeightData data) {

        currentWeight=data.weight_kg;

        if (data.isStabilized) {
            if (data.imp != 0) {
//                String t = String.format(
//                        "weight=%.2f bmi=%.2f,body fat=%.2f,muscle=%.2f,water=%.2f,bone=%.2f,protein=%.2f,bmr=%d,visceral=%.2f,skeletal muscle=%.2f,physical age=%d",
//                      data.weight_kg,  data.bmi, data.bodyFatPercent, data.musclePercent, data.moisturePercent, data.boneMass,
//                        data.proteinPercent, data.bmr, data.visceralFat, data.smPercent, (int) data.physicalAge);
//

              //  addLog( String.format(Locale.ENGLISH, "weight= %.2f",data.weight_kg));

                try {


                JSONObject inputObject = new JSONObject();
                inputObject.put("connectState", connectionStateName);
                inputObject.put("isConnected", connectionStateName=="Connected");
                inputObject.put("isFinished", false);
                inputObject.put("hasData", data.weight_kg>0);

                inputObject.put("msg", "");
                inputObject.put("weight", String.format(Locale.ENGLISH, "%.2f",data.weight_kg));


                if (events != null) {
                    events.success(inputObject.toString());
                }


                }catch  (Exception var5) {
                    Log.i("erorr",var5.getMessage());

                }

                if (device != null) {
                    ICDeviceManager.shared().removeDevice(device, new ICConstant.ICRemoveDeviceCallBack() {
                        @Override
                        public void onCallBack(ICDevice device, ICConstant.ICRemoveDeviceCallBackCode code) {


                      //      addLog(String.format(Locale.ENGLISH, "weight= %.2f",data.weight_kg)+"\ndelete device state : " + code);

                                connectionStateName ="Complete Reading";



                            try {


    JSONObject inputObject = new JSONObject();
    inputObject.put("connectState", connectionStateName);
    inputObject.put("isConnected", connectionStateName == "Connected");
    inputObject.put("isFinished", true);
    inputObject.put("hasData", data.weight_kg>0);
    inputObject.put("msg", "");
    inputObject.put("weight", String.format(Locale.ENGLISH, "%.2f", data.weight_kg));


    if (events != null) {
        events.success(inputObject.toString());
    }


}catch  (Exception var5) {
    Log.i("erorr",var5.getMessage());

}
                        }
                    });
                }





            }
        }
        else {

            addLog( String.format(Locale.ENGLISH, "weight= %.2f",data.weight_kg));

        }

        // addLog(device.getMacAddr() + ": weight data :" + data.weight_kg + " " +
        // data.temperature + " " + data.imp
        // + " " + data.isStabilized);
        if (data.isStabilized) {
            int i = 0;
        }
    }

    @Override
    public void onReceiveKitchenScaleData(ICDevice device, ICKitchenScaleData data) {
        addLog( " kitchen data:" + data.value_g + "\t" + data.value_lb + "\t" + data.value_lb_oz
                + "\t" + data.isStabilized);
    }

    @Override
    public void onReceiveKitchenScaleUnitChanged(ICDevice device, ICConstant.ICKitchenScaleUnit unit) {
        addLog( " kitchen unit changed :" + unit);
    }

    @Override
    public void onReceiveCoordData(ICDevice device, ICCoordData data) {
        addLog( " coord data:" + data.getX() + "\t" + data.getY() + "\t" + data.getTime());

    }

    @Override
    public void onReceiveRulerData(ICDevice device, ICRulerData data) {
        addLog( " ruler data :" + data.getDistance_cm() + "\t" + data.getPartsType() + "\t"
                + data.getTime() + "\t" + data.isStabilized());
        if (data.isStabilized()) {
            // demo, auto change device show body parts type
            if (data.getPartsType() == ICConstant.ICRulerBodyPartsType.ICRulerPartsTypeCalf) {
                return;
            }

            ICDeviceManager.shared().getSettingManager().setRulerBodyPartsType(device,
                    ICConstant.ICRulerBodyPartsType.valueOf(data.getPartsType().getValue() + 1),
                    new ICDeviceManagerSettingManager.ICSettingCallback() {
                        @Override
                        public void onCallBack(ICConstant.ICSettingCallBackCode code) {

                        }
                    });
        }
    }

    @Override
    public void onReceiveRulerHistoryData(ICDevice icDevice, ICRulerData icRulerData) {

    }

    @Override
    public void onReceiveWeightCenterData(ICDevice icDevice, ICWeightCenterData data) {
        addLog( " center data :L=" + data.getLeftPercent() + "   R=" + data.getRightPercent()
                + "\t" + data.getTime() + "\t" + data.isStabilized());
    }

    @Override
    public void onReceiveWeightUnitChanged(ICDevice icDevice, ICConstant.ICWeightUnit unit) {
        addLog( " weigth unit changed :" + unit);
    }

    @Override
    public void onReceiveRulerUnitChanged(ICDevice icDevice, ICConstant.ICRulerUnit unit) {
        addLog(" ruler unit changed :" + unit);

    }

    @Override
    public void onReceiveRulerMeasureModeChanged(ICDevice icDevice, ICConstant.ICRulerMeasureMode mode) {
        addLog( " ruler measure mode changed :" + mode);

    }

    // eight eletrode scale callback
    @Override
    public void onReceiveMeasureStepData(ICDevice icDevice, ICConstant.ICMeasureStep step, Object data2) {
        switch (step) {
            case ICMeasureStepMeasureWeightData: {
                ICWeightData data = (ICWeightData) data2;
                onReceiveWeightData(device, data);
            }
            break;
            case ICMeasureStepMeasureCenterData: {
                ICWeightCenterData data = (ICWeightCenterData) data2;
                onReceiveWeightCenterData(device, data);
            }
            break;
            case ICMeasureStepAdcStart: {
                addLog( " start imp... ");
            }
            break;
            case ICMeasureStepAdcResult: {
                addLog( " imp over");
            }
            break;
            case ICMeasureStepHrStart: {
                addLog( " start hr");
            }
            break;

            case ICMeasureStepHrResult: {
                ICWeightData hrData = (ICWeightData) data2;
                addLog( " over hr: " + hrData.hr);

            }
            break;
            case ICMeasureStepMeasureOver: {
                ICWeightData data = (ICWeightData) data2;
                data.isStabilized = true;
                addLog( " over measure");
                onReceiveWeightData(device, data);
            }
            break;

            default:
                break;
        }
    }

    @Override
    public void onReceiveWeightHistoryData(ICDevice icDevice, ICWeightHistoryData icWeightHistoryData) {
        addLog( " history weight_kg=" + icWeightHistoryData.weight_kg + ", imp="
                + icWeightHistoryData.imp);
    }

    @Override
    public void onReceiveSkipData(ICDevice icDevice, ICSkipData data) {
        addLog(device.getMacAddr() + " skip data: mode=" + data.mode + ", param=" + data.setting + ",use_time="
                + data.elapsed_time + ",count=" + data.skip_count);
        if (data.isStabilized) {
         //   txt_log.setText("");
            StringBuilder freqs = new StringBuilder();
            freqs.append("[");
            for (ICSkipFreqData freqData : data.freqs) {
                freqs.append("dur=").append(freqData.duration).append(", jumpcount=").append(freqData.skip_count)
                        .append(";");
            }
            freqs.append("]");
            addLog(device.getMacAddr() + ": skip data2 : time=" + data.time + " mode=" + data.mode + ", param="
                    + data.setting + ",use_time=" + data.elapsed_time + ",count=" + data.skip_count + ", avg="
                    + data.avg_freq + ", fastest=" + data.fastest_freq + ", freqs=" + freqs);
        }
    }

    @Override
    public void onReceiveHistorySkipData(ICDevice icDevice, ICSkipData icSkipData) {

    }

    @Override
    public void onReceiveBattery(ICDevice device, int battery, Object ext) {

    }

    @Override
    public void onReceiveUpgradePercent(ICDevice icDevice, ICConstant.ICUpgradeStatus icUpgradeStatus, int i) {

    }

    @Override
    public void onReceiveDeviceInfo(ICDevice icDevice, ICDeviceInfo icDeviceInfo) {

    }

    @Override
    public void onReceiveDebugData(ICDevice icDevice, int i, Object o) {

    }

    @Override
    public void onReceiveConfigWifiResult(ICDevice icDevice, ICConstant.ICConfigWifiState icConfigWifiState) {

    }

    @Override
    public void onReceiveHR(ICDevice device, int hr) {

    }

    @Override
    public void onReceiveUserInfo(ICDevice device, ICUserInfo userInfo) {

    }

    @Override
    public void onReceiveRSSI(ICDevice device, int rssi) {

    }

}