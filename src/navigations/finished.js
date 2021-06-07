import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Finished from '../screens/finished';
import stackScreenOptions from './stackScreenOptions';

const Stack = createStackNavigator();

function StartOnboarding() {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen
          name="Finished"
          component={Finished}
          options={stackScreenOptions()}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default StartOnboarding;
