# sm_lepu_weight

This plugin used to connect and read data from lepu weight device

## Getting Started

Please follow steps below to use it perfectly.

To add this plugin to your project you can add below tow lines in your project pubspec

      sm_lepu_weight:
        git: https://github.com/SmartMindSYSCoder/lepu_weight.git

To import it:
    
      import 'package:sm_lepu_weight/sm_lepu_weight.dart';
      import 'package:sm_lepu_weight/weight_model.dart';

Now you can create instance of plugin :

     final smLepuWeight = SmLepuWeight();


To start use this plugin must be call getPermissions() like below:


                smLepuWeight.checkPermissions();



Then call init  method like below:


                     smLepuWeight.init();


To start scan and read from device:


                smLepuWeight.scan();


                smLepuWeight.getEvents().listen((event) {
                  final  data = jsonDecode(event);
                  weightModel = WeightModel.fromJson(data);

        

                  setState(() {

                  });
                });

Note that :  the stream listen to check the update of state of connection and retrieve data


You can also see the example

I hope this clear 