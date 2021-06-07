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

const TransitionScreenOptions = {
  ...TransitionPresets.SlideFromRightIOS, // This is where the transition happens
  // headerShown: false,
};

function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={TransitionScreenOptions}>
        <Stack.Screen
          name="Welcome"
          component={Welcome}
          options={{
            title: 'Dismoi',
            headerStyle: {
              backgroundColor: '#1e52b4',
            },
            headerTintColor: '#fff',
            headerTitleStyle: {
              fontWeight: 'bold',
              alignSelf: 'center',
            },
          }}
        />
        <Stack.Screen
          name="Tuto1"
          component={Tuto1}
          options={{
            title: 'Dismoi',
            headerLeft: () => null,
            headerStyle: {
              backgroundColor: '#1e52b4',
            },
            headerTintColor: '#fff',
            headerTitleStyle: {
              fontWeight: 'bold',
              alignSelf: 'center',
            },
          }}
        />

        <Stack.Screen
          name="Tuto2"
          component={Tuto2}
          options={{
            title: 'Dismoi',
            headerLeft: () => null,
            headerStyle: {
              backgroundColor: '#1e52b4',
            },
            headerTintColor: '#fff',
            headerTitleStyle: {
              fontWeight: 'bold',
              alignSelf: 'center',
            },
          }}
        />
        <Stack.Screen
          name="Tuto3"
          component={Tuto3}
          options={{
            title: 'Dismoi',
            headerLeft: () => null,
            headerStyle: {
              backgroundColor: '#1e52b4',
            },
            headerTintColor: '#fff',
            headerTitleStyle: {
              fontWeight: 'bold',
              alignSelf: 'center',
            },
          }}
        />
        <Stack.Screen
          name="Tuto4"
          component={Tuto4}
          options={{
            title: 'Dismoi',
            headerLeft: () => null,
            headerStyle: {
              backgroundColor: '#1e52b4',
            },
            headerTintColor: '#fff',
            headerTitleStyle: {
              fontWeight: 'bold',
              alignSelf: 'center',
            },
          }}
        />
        <Stack.Screen
          name="Authorizations"
          component={Authorizations}
          options={{
            title: 'Dismoi',
            headerLeft: () => null,
            headerStyle: {
              backgroundColor: '#1e52b4',
            },
            headerTintColor: '#fff',
            headerTitleStyle: {
              fontWeight: 'bold',
              alignSelf: 'center',
            },
          }}
        />
        <Stack.Screen
          name="Finished"
          component={Finished}
          options={{
            title: 'Dismoi',
            headerLeft: () => null,
            headerStyle: {
              backgroundColor: '#1e52b4',
            },
            headerTintColor: '#fff',
            headerTitleStyle: {
              fontWeight: 'bold',
              alignSelf: 'center',
            },
          }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
