import 'package:flutter_test/flutter_test.dart';
import 'package:sm_lepu_weight/sm_lepu_weight.dart';

import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockSmLepuWeightPlatform
    with MockPlatformInterfaceMixin
    implements SmLepuWeightPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final SmLepuWeightPlatform initialPlatform = SmLepuWeightPlatform.instance;

  test('$MethodChannelSmLepuWeight is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelSmLepuWeight>());
  });

  test('getPlatformVersion', () async {
    SmLepuWeight smLepuWeightPlugin = SmLepuWeight();
    MockSmLepuWeightPlatform fakePlatform = MockSmLepuWeightPlatform();
    SmLepuWeightPlatform.instance = fakePlatform;

    expect(await smLepuWeightPlugin.getPlatformVersion(), '42');
  });
}
