import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import {
  createStackNavigator,
  TransitionPresets,
} from '@react-navigation/stack';

import Welcome from './screens/welcome';
import Tuto1 from './screens/tuto1';

const Stack = createStackNavigator();

const TransitionScreenOptions = {
  ...TransitionPresets.SlideFromRightIOS, // This is where the transition happens
};

function App() {
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
