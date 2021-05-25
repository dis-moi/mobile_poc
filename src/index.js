import React from 'react';
import useNativeModuleEffects from './useNativeModuleEffects';
import { TouchableOpacity, View, Image, Linking, Button } from 'react-native';

import { Background } from './nativeModules/get';

function App() {
  return (
    <View style={{ flex: 1, flexDirection: 'column' }}>
      <Button
        style={{ margin: 5 }}
        title="Start dismoi in background"
        onPress={() => Background.startService()}
      />
      <TouchableOpacity
        onPress={() => {
          Linking.openURL('https://www.google.com');
        }}
      >
        <Image source={require('./images/demo.png')} />
      </TouchableOpacity>
    </View>
  )
}

export default App;
