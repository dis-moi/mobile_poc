import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import {
  createStackNavigator,
  TransitionPresets,
} from '@react-navigation/stack';

import Welcome from './screens/welcome';
import Tuto1 from './screens/tuto1';
import Tuto2 from './screens/tuto2';
import Tuto3 from './screens/tuto3';
import Tuto4 from './screens/tuto4';
import Finished from './screens/finished';

import Authorizations from './screens/authorizations';

const Stack = createStackNavigator();

import { Background } from './nativeModules/get';

const TransitionScreenOptions = {
  ...TransitionPresets.SlideFromRightIOS, // This is where the transition happens
  headerShown: false,
};

function App() {
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

        <Stack.Screen name="Tuto2" component={Tuto2} />
        <Stack.Screen name="Tuto3" component={Tuto3} />
        <Stack.Screen name="Tuto4" component={Tuto4} />
        <Stack.Screen name="Authorizations" component={Authorizations} />
        <Stack.Screen name="Finished" component={Finished} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
