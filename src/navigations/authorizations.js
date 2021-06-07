import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import {
  createStackNavigator,
  TransitionPresets,
} from '@react-navigation/stack';

import Finished from '../screens/finished';
import Authorizations from '../screens/authorizations';
import stackScreenOptions from './stackScreenOptions';

const Stack = createStackNavigator();

const TransitionScreenOptions = {
  ...TransitionPresets.SlideFromRightIOS, // This is where the transition happens
};

function AuthorizationsMustBeEnabled() {
  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={TransitionScreenOptions}>
        <Stack.Screen
          name="Authorizations"
          component={Authorizations}
          options={stackScreenOptions()}
        />
        <Stack.Screen
          name="Finished"
          component={Finished}
          options={stackScreenOptions()}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default AuthorizationsMustBeEnabled;
