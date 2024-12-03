
import 'dart:async';
import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:sm_lepu_weight/weight_model.dart';


class SmLepuWeight {

  final methodChannel = const MethodChannel('sm_lepu_weight');



  @visibleForTesting
  final eventChannel = const EventChannel("sm_lepu_weight_status");


  Future<String?> getPlatformVersion() {
    return  methodChannel.invokeMethod<String>('getPlatformVersion');
  }



  Future<dynamic> checkPermissions() {
    return  methodChannel.invokeMethod('checkPermissions');
  }

  Future<dynamic> init() async{
      methodChannel.invokeMethod('init');

      _readStatus();
      scan();

  }


  Future<dynamic> scan() {
    return  methodChannel.invokeMethod('scan');
  }



  final StreamController _controller = StreamController<dynamic>();

  Stream<dynamic> get statusStream => _controller.stream;
  bool get isListening => _controller.hasListener;

  Future<dynamic>  _readStatus() async{

    eventChannel
        .receiveBroadcastStream().listen((result){


          try {
            final Map<String, dynamic> data = jsonDecode(result.toString());
            WeightModel weightModel = WeightModel.fromJson(data);


            if (!_controller.isClosed) {
              _controller.sink.add(weightModel);
            }

            if(weightModel.isFinished){

             // _controller.close();
            }
          }catch (_){}
    },

      onError: (error) {
        debugPrint(' ***********************   Error in event stream: $error');
      },
    );
  }



}
