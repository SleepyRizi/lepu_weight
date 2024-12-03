package com.sm.sm_lepu_weight;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** SmLepuWeightPlugin */
public class SmLepuWeightPlugin implements FlutterPlugin, MethodCallHandler ,ActivityAware{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private EventChannel eventChannel;

  public Context applicationContext;
  public Activity activity;

  public PermissionHelper permissionHelper;
  public ScanHelper scanHelper;
  public WeightHelper weightHelper;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "sm_lepu_weight");

    eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "sm_lepu_weight_status");


    channel.setMethodCallHandler(this);
    this.applicationContext = flutterPluginBinding.getApplicationContext();

    scanHelper=new ScanHelper();
    weightHelper=new WeightHelper(applicationContext);

  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }



    else if(call.method.equals("checkPermissions")){



      permissionHelper.checkPermissions();


    }

    else if(call.method.equals("init")){

     if( permissionHelper.isPermissionsGranted()) {

       eventChannel.setStreamHandler(weightHelper);

       weightHelper.init();
     }

     else{

       Toast.makeText(applicationContext, " Permission not granted\nPlease check permission first", Toast.LENGTH_SHORT).show();

     }

    }
    else if(call.method.equals("scan")){



      scanHelper.scan();


    }

    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    eventChannel.setStreamHandler(null);

  }



  @Override
  public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    // TODO: your plugin is now attached to an Activity
//    this.activity = activityPluginBinding.getActivity();
    this.activity = activityPluginBinding.getActivity();
//    this.applicationContext = activityPluginBinding.getApplicationContext();
    permissionHelper = new PermissionHelper(activity, applicationContext);


  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    // This call will be followed by onReattachedToActivityForConfigChanges().
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {
  }

  @Override
  public void onDetachedFromActivity() {

  }


}
