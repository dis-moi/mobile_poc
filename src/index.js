/* eslint-disable no-console */
import React from 'react';
import { Text, View, DeviceEventEmitter } from 'react-native';
import { FloatingLayout } from './nativeModules/get';

import Permission from './components/permission';
import { isValidHttpUrl } from './libraries';
import {
  EVENT_FROM_CHROME_URL,
  EVENT_FROM_ACCESSIBILITY_SERVICE_PERMISSION,
  EVENT_FROM_LEAVING_CHROME_APP,
} from './nativeModules/eventListToListen';

import useAccessibilityServiveEventToListenFromNativeModuleEffect from './useEffectHooks/accessibilityService/eventToListenFromNativeModule';
import useFloatingLayoutRequestPermissionEffect from './useEffectHooks/floatingLayout/requestPermission';
import useCheckIfAccessibilityIsEnabledEffect from './useEffectHooks/accessibilityService/checkIfAccessibilityIsEnabled';
import useFloatingLayoutInitializeEffect from './useEffectHooks/floatingLayout/initialize';

function App() {
  const eventMessageFromChromeURL = useAccessibilityServiveEventToListenFromNativeModuleEffect(
    EVENT_FROM_CHROME_URL
  );

  const eventMessageFromAccessibilityServicePermission = useAccessibilityServiveEventToListenFromNativeModuleEffect(
    EVENT_FROM_ACCESSIBILITY_SERVICE_PERMISSION
  );

  const eventMessageFromLeavingChromeApp = useAccessibilityServiveEventToListenFromNativeModuleEffect(
    EVENT_FROM_LEAVING_CHROME_APP
  );

  const accessibilityServiceIsEnabled = useCheckIfAccessibilityIsEnabledEffect(
    eventMessageFromAccessibilityServicePermission
  );

  useFloatingLayoutRequestPermissionEffect();

  useFloatingLayoutInitializeEffect();

  React.useEffect(() => {
    DeviceEventEmitter.addListener('floating-dismoi-bubble-press', (e) => {
      return FloatingLayout.showFloatingDisMoiMessage(10, 1500).then(() => {
        // What to do when user press on the bubble
        console.log('Bubble press');
      });
    });

    DeviceEventEmitter.addListener('floating-dismoi-bubble-remove', (e) => {
      // What to do when user removes the bubble
      return console.log('Remove Bubble');
    });

    DeviceEventEmitter.addListener('floating-dismoi-message-remove', (e) => {
      // What to do when user removes the bubble
      return console.log('DisMoi message remove');
    });

    DeviceEventEmitter.addListener('floating-dismoi-message-press', (e) => {
      // What to do when user removes the bubble
      console.log('DisMoi message press');
      return FloatingLayout.hideFloatingDisMoiMessage().then(() =>
        console.log('Hide Floating DisMoiMessage')
      );
    });

    async function manipulateFloatingLayout() {
      // Initialize bubble manage
      if (eventMessageFromLeavingChromeApp === 'true') {
        return await FloatingLayout.hideFloatingDisMoiBubble();
      }

      if (
        eventMessageFromChromeURL &&
        isValidHttpUrl(eventMessageFromChromeURL)
      ) {
        if (eventMessageFromChromeURL === 'backmarket.fr') {
          FloatingLayout.showFloatingDisMoiBubble(10, 1500).then(() =>
            console.log('Floating Bubble Added')
          );
        } else {
          FloatingLayout.hideFloatingDisMoiBubble().then(() =>
            console.log('Hide Floating Bubble')
          );
        }
      }
    }

    manipulateFloatingLayout();
  }, [eventMessageFromChromeURL, eventMessageFromLeavingChromeApp]);

  return (
    <View style={styles.centerScreen}>
      <Text>Welcome to DisMoi POC!</Text>
      <Permission
        isAccessibilityServiceEnabled={accessibilityServiceIsEnabled}
      />
    </View>
  );
}

const styles = {
  centerScreen: { flex: 1, justifyContent: 'center', alignItems: 'center' },
};

export default App;
