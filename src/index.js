import React from 'react';
import useNativeModuleEffects from './useNativeModuleEffects';
import { TouchableOpacity, View, Image, Linking, Button } from 'react-native';

import { Background } from './nativeModules/get';

function App() {
  useNativeModuleEffects();

  React.useEffect(() => {
    function startDisMoiAppInBackground() {
      Background.startService();
    }
    startDisMoiAppInBackground();
  }, []);

  return (
    <View style={{ flex: 1, flexDirection: 'column' }}>
      <TouchableOpacity
        onPress={() => {
          Linking.openURL('https://www.google.com');
        }}
      >
        <Image source={require('./images/demo.png')} />
      </TouchableOpacity>
    </View>
  );
}

export default App;
