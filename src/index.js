import React from 'react';
import listenToAppStateUseEffect from './useEffectHooks/nativeModules/accessibilityService/listenMessage/appState';
import useCheckIfAccessibilityIsEnabledEffect from './useEffectHooks/nativeModules/accessibilityService/sendMessage/checkIfAccessibilityIsEnabled';
import useCheckIfCanDrawOverlayIsEnabledEffect from './useEffectHooks/nativeModules/accessibilityService/sendMessage/checkIfCanDrawOverlayIsEnabled';

import AsyncStorage from '@react-native-async-storage/async-storage';
import StartOnboarding from './navigations/onboarding';
import Authorizations from './navigations/authorizations';
import EverythingIsReady from './navigations/finished';

function App() {
  const appState = listenToAppStateUseEffect();

  const accessibilityServiceIsEnabled = useCheckIfAccessibilityIsEnabledEffect(
    appState
  );

  const canDrawOverlayIsChecked = useCheckIfCanDrawOverlayIsEnabledEffect(
    appState
  );

  const [firstLaunch, setFirstLaunch] = React.useState(null);

  React.useEffect(() => {
    let mounted = true;
    AsyncStorage.getItem('alreadyLaunched').then((value) => {
      if (mounted) {
        if (value == null) {
          setFirstLaunch(true);
        } else {
          setFirstLaunch(false);
        }
      }
    });

    return function cleanup() {
      mounted = false;
    };
  }, []);

  if (firstLaunch === null) {
    return null;
  }

  if (firstLaunch === true) {
    return <StartOnboarding />;
  }

  if (firstLaunch === false) {
    if (
      accessibilityServiceIsEnabled === true &&
      canDrawOverlayIsChecked === true
    ) {
      return <EverythingIsReady />;
    } else {
      return <Authorizations />;
    }
  }
}

export default App;
