package com.sm.sm_lepu_weight;


import android.util.Log;
import java.util.ArrayList;
import android.content.Context;
import android.app.Activity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import cn.icomon.icdevicemanager.ICDeviceManager;
import cn.icomon.icdevicemanager.callback.ICScanDeviceDelegate;
import cn.icomon.icdevicemanager.model.device.ICScanDeviceInfo;

public  class ScanHelper implements ICScanDeviceDelegate{

    ArrayList<ICScanDeviceInfo> _devices = new ArrayList<>();

//    Context applicationContext;
//    ScanHelper(Context applicationContext ){
//
//        this.applicationContext=applicationContext;
//    }

    public void scan(){


        Log.i("init","*******************************   this is from scan");

        ICDeviceManager.shared().scanDevice(this);

    }

    @Override
    protected void finalize() throws Throwable {
        ICDeviceManager.shared().stopScan();
        super.finalize();
    }

    @Override
    public void onScanResult(ICScanDeviceInfo deviceInfo) {
        boolean isE = false;




        for (ICScanDeviceInfo deviceInfo1 : _devices) {

            Log.d("device","device name *******************  \n"+deviceInfo1.getName());


            if (deviceInfo1.getMacAddr().equalsIgnoreCase(deviceInfo.getMacAddr())) {
                deviceInfo1.setRssi(deviceInfo.getRssi());
                isE = true;
                break;
            }
        }
        if (!isE) {
            _devices.add(deviceInfo);


        }
        for (ICScanDeviceInfo deviceInfo1 : _devices) {

            if(deviceInfo1.getName().toLowerCase().contains("my_scale")){

                ICDeviceManager.shared().stopScan();
                ICScanDeviceInfo device = deviceInfo1;
                EventMgr.post("SCAN", device);
              //  finish();
                String str = deviceInfo1.getName() + "   " + deviceInfo1.getMacAddr() + "   " + deviceInfo1.getRssi();

                Log.d("device","device name *******************  \n"+str);
            }


            // data.add(str);
        }
        //  adapter.notifyDataSetChanged();
    }



}