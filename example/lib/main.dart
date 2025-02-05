import 'dart:convert';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:sm_lepu_weight/sm_lepu_weight.dart';
import 'package:sm_lepu_weight/weight_model.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final smLepuWeight = SmLepuWeight();

  WeightModel weightModel =WeightModel();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await smLepuWeight.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }


  String connectionState="";
  String data="";

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Smartmind Lepu Weight Plugin'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [

              TextButton(onPressed: (){

                smLepuWeight.checkPermissions();

              }, child: Text("checkPermissions")),

              SizedBox(height: 30,),
              TextButton(onPressed: (){

                smLepuWeight.init();





              }, child: Text("Init")),

              SizedBox(height: 30,),
              TextButton(onPressed: (){

                smLepuWeight.scan();


                smLepuWeight.getEvents().listen((event) {
                  final  data = jsonDecode(event);
                  weightModel = WeightModel.fromJson(data);


                  setState(() {
                  });
                });


              }, child: Text("scan")),

              SizedBox(height: 30,),


              Text("Connection : ${weightModel.connectionState}"),
              SizedBox(height: 10,),

              Text("Message : ${weightModel.msg}"),


              Text("Weight : ${weightModel.weight}"),


            ],
          ),
        ),
      ),
    );
  }
}
