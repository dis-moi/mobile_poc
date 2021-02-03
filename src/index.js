import React from 'react';
import {Text, View, DeviceEventEmitter} from 'react-native';

import {FloatingBubble} from './nativeModules/get';

import Permission from './modules/permission';
import {isValidHttpUrl} from './libraries';
import {
  EVENT_FROM_CHROME_URL,
  EVENT_FROM_ACCESSIBILITY_SERVICE_PERMISSION,
  EVENT_FROM_LEAVING_CHROME_APP,
} from './nativeModules/eventListToListen';

import useAccessibilityServiveEventToListenFromNativeModuleEffect from './useEffectHooks/accessibilityService/accessibilityServiceEventToListenFromNativeModule';
import useFloatingBubbleRequestPermissionEffect from './useEffectHooks/floatingBubble/floatingBubbleRequestPermission';
import useCheckIfAccessibilityIsEnabledEffect from './useEffectHooks/accessibilityService/checkIfAccessibilityIsEnabled';
import useFloatingBubbleInitializeEffect from './useEffectHooks/floatingBubble/floatingBubbleInitialize';

function App() {
  const eventMessageFromChromeURL = useAccessibilityServiveEventToListenFromNativeModuleEffect(
    EVENT_FROM_CHROME_URL,
  );

  const eventMessageFromAccessibilityServicePermission = useAccessibilityServiveEventToListenFromNativeModuleEffect(
    EVENT_FROM_ACCESSIBILITY_SERVICE_PERMISSION,
  );

  const eventMessageFromLeavingChromeApp = useAccessibilityServiveEventToListenFromNativeModuleEffect(
    EVENT_FROM_LEAVING_CHROME_APP,
  );

  const accessibilityServiceIsEnabled = useCheckIfAccessibilityIsEnabledEffect(
    eventMessageFromAccessibilityServicePermission,
  );

  useFloatingBubbleRequestPermissionEffect();

  useFloatingBubbleInitializeEffect();

  React.useEffect(() => {
    function manipulateFloatingBubble() {
      // Initialize bubble manage
      if (eventMessageFromLeavingChromeApp === 'true') {
        return FloatingBubble.hideFloatingBubble().then(() =>
          console.log('Hide Floating Bubble'),
        );
      }

      if (
        eventMessageFromChromeURL &&
        isValidHttpUrl(eventMessageFromChromeURL)
      ) {
        if (eventMessageFromChromeURL === 'backmarket.fr') {
          FloatingBubble.showFloatingBubble(10, 1500).then(() =>
            console.log('Floating Bubble Added'),
          );
        } else {
          FloatingBubble.hideFloatingBubble().then(() =>
            console.log('Hide Floating Bubble'),
          );
        }
      }

      DeviceEventEmitter.addListener('floating-bubble-press', (e) => {
        FloatingBubble.showFloatingDisMoiMessage(10, 1500).then(() => {
          // What to do when user press on the bubble
          console.log('Bubble press');
        });
      });

      DeviceEventEmitter.addListener('floating-bubble-remove', (e) => {
        // What to do when user removes the bubble
        console.log('Remove Bubble');
      });
    }

    manipulateFloatingBubble();
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
  centerScreen: {flex: 1, justifyContent: 'center', alignItems: 'center'},
};

export default App;
