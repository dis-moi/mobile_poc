import React from 'react';
import { View, AppState } from 'react-native';
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
import { Background } from './nativeModules/get';

import Authorizations from './screens/authorizations';

const Stack = createStackNavigator();

const TransitionScreenOptions = {
  ...TransitionPresets.SlideFromRightIOS, // This is where the transition happens
};

function App() {
  const [switchValueForOverlay, setSwitchValueForOverlay] = React.useState(
    false
  );

  const [appState, setAppState] = React.useState(false);

  function handleAppStateChange(nextAppState) {
    setAppState(nextAppState);
  }

  const [loaded, setLoaded] = React.useState(false);
  const [loaded1, setLoaded1] = React.useState(false);

  React.useEffect(() => {
    AppState.addEventListener('change', handleAppStateChange);

    return () => {
      AppState.removeEventListener('change', handleAppStateChange);
    };
  }, []);

  const [
    accessibilityServiceIsEnabled,
    setAccessibilityServiceEnabled,
  ] = React.useState(false);

  React.useEffect(() => {
    Background.canDrawOverlay((result) => {
      if (result === '1') {
        setSwitchValueForOverlay(true);
      }
      if (result === '0') {
        setSwitchValueForOverlay(false);
      }
    });
    setLoaded(true);
  }, [appState]);

  React.useEffect(() => {
    function callIsAccessibilityEnabledMethod() {
      Background.isAccessibilityEnabled((result) => {
        if (result === '1') {
          setAccessibilityServiceEnabled(true);
        }
        if (result === '0') {
          setAccessibilityServiceEnabled(false);
        }
      });

      setLoaded1(true);
    }

    callIsAccessibilityEnabledMethod();
  }, [appState]);

  let defaultPage;

  if (
    accessibilityServiceIsEnabled === true &&
    switchValueForOverlay === true
  ) {
    defaultPage = 'Finished';
  } else {
    defaultPage = 'Welcome';
  }

  if (loaded === false || loaded1 === false) {
    return <View />;
  }

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={TransitionScreenOptions}>
        {defaultPage === 'Welcome' ? (
          <>
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
          </>
        ) : (
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
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
