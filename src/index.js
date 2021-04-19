import React from 'react';
import { Text, View } from 'react-native';
import useNativeModuleEffects from './useNativeModuleEffects';

function App() {
  useNativeModuleEffects();

  return (
    <View style={styles.centerScreen}>
      <Text>Welcome to DisMoi POC!</Text>
      <Text>1. Allow accessibility service for dismoi</Text>
      <Text>2. Allow overlap for dismoi</Text>
      <Text>3. Go to chrome app</Text>
    </View>
  );
}

const styles = {
  centerScreen: { flex: 1, justifyContent: 'center', alignItems: 'center' },
};

export default App;
