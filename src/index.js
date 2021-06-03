import React from 'react';
import useNativeModuleEffects from './useNativeModuleEffects';
import { NavigationContainer } from '@react-navigation/native';
import {
  createStackNavigator,
  TransitionPresets,
} from '@react-navigation/stack';

import Welcome from './screens/welcome';
import Tuto1 from './screens/tuto1';

const Stack = createStackNavigator();

import { Background } from './nativeModules/get';

const TransitionScreenOptions = {
  ...TransitionPresets.SlideFromRightIOS, // This is where the transition happens
  headerShown: false,
};

function App() {
  useNativeModuleEffects();

  React.useEffect(() => {
    function startDisMoiAppInBackground() {
      Background.startService();
    }
    startDisMoiAppInBackground();
  }, []);

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={TransitionScreenOptions}>
        <Stack.Screen name="Welcome" component={Welcome} />
        <Stack.Screen name="Tuto1" component={Tuto1} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
