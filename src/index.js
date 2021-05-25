import React from 'react';
import useNativeModuleEffects from './useNativeModuleEffects';
import { TouchableOpacity, View, Image, Linking, Button } from 'react-native';

import { Background } from './nativeModules/get';

function App() {
  useNativeModuleEffects();

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
  );
}

const styles = {
  centerScreen: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  view: {
    flex: 0.5,
    justifyContent: 'center',
    alignItems: 'center',
  },
  button: {
    backgroundColor: 'gray',
    padding: 10,
    margin: 10,
  },
};

export default App;
