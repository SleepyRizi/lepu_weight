class WeightModel{

 final bool isConnected,hasData,isFinished;
 final String connectionState,msg;
final double weight;

 WeightModel({this.isConnected=false,this.hasData=false,this.isFinished=false,this.connectionState='',this.msg='',this.weight=0.0});


 factory WeightModel.fromJson(dynamic json)=>WeightModel(

  isConnected: json['isConnected'] ?? false,
  hasData: json['hasData'] ?? false,
  isFinished: json['isFinished'] ?? false,
  connectionState: json['connectState'] ?? "",
  msg: json['msg'] ?? "",


  weight: double.tryParse(json['weight']) ??0.0,



 );

}

