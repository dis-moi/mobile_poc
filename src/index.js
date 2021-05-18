/* eslint-disable no-console */
import React from 'react';
import {
  TouchableOpacity,
  View,
  DeviceEventEmitter,
  Image,
  Linking,
} from 'react-native';

import { FloatingModule } from './nativeModules/get';

import Permission from './components/permission';
import AllowPermissionButton from './components/allowPermissionButton';
import { isValidHttpUrl } from './libraries';
import {
  EVENT_FROM_CHROME_URL,
  EVENT_FROM_ACCESSIBILITY_SERVICE_PERMISSION,
} from './nativeModules/eventListToListen';

import useAccessibilityServiveEventToListenFromNativeModuleEffect from './useEffectHooks/accessibilityService/eventToListenFromNativeModule';
import useFloatingModuleRequestPermissionEffect from './useEffectHooks/floating/requestPermission';
import useCheckIfAccessibilityIsEnabledEffect from './useEffectHooks/accessibilityService/checkIfAccessibilityIsEnabled';
import useFloatingModuleInitializeEffect from './useEffectHooks/floating/initialize';

function App() {
  const eventMessageFromChromeURL = useAccessibilityServiveEventToListenFromNativeModuleEffect(
    EVENT_FROM_CHROME_URL
  );

  const eventMessageFromAccessibilityServicePermission = useAccessibilityServiveEventToListenFromNativeModuleEffect(
    EVENT_FROM_ACCESSIBILITY_SERVICE_PERMISSION
  );

  const accessibilityServiceIsEnabled = useCheckIfAccessibilityIsEnabledEffect(
    eventMessageFromAccessibilityServicePermission
  );

  useFloatingModuleRequestPermissionEffect();

  useFloatingModuleInitializeEffect();

  React.useEffect(() => {
    DeviceEventEmitter.addListener('floating-dismoi-bubble-press', (e) => {
      return FloatingModule.showFloatingDisMoiMessage(10, 1500).then(() => {
        // What to do when user press on the bubble
        console.log('Bubble press');
      });
    });

    DeviceEventEmitter.addListener('floating-dismoi-bubble-remove', (e) => {
      // What to do when user removes the bubble
      return console.log('Remove Bubble');
    });

    DeviceEventEmitter.addListener('floating-dismoi-message-remove', (e) => {
      // What to do when user removes the message
      return console.log('DisMoi message remove');
    });

    DeviceEventEmitter.addListener('floating-dismoi-message-press', (e) => {
      // What to do when user press on the message
      return FloatingModule.hideFloatingDisMoiMessage().then(() =>
        console.log('Hide Floating DisMoiMessage')
      );
    });
  }, []);

  React.useEffect(() => {
    function getNoticeIds(matchingContexts) {
      return matchingContexts
        .map((res) => {
          const addWWWForBuildingURL = `www.${eventMessageFromChromeURL}`;

          if (addWWWForBuildingURL.match(new RegExp(res.urlRegex, 'g'))) {
            return res.noticeId;
          }
        })
        .filter(Boolean);
    }

    async function manipulateFloatingModule() {
      if (
        eventMessageFromChromeURL &&
        isValidHttpUrl(eventMessageFromChromeURL)
      ) {
        const matchingContexts = await fetch(
          'https://notices.bulles.fr/api/v3/matching-contexts'
        ).then((response) => response.json());

        const noticeIds = getNoticeIds(matchingContexts);

        const notices = await Promise.all(
          noticeIds.map((noticeId) =>
            fetch(
              `https://notices.bulles.fr/api/v3/notices/${noticeId}`
            ).then((response) => response.json())
          )
        );

        if (notices.length > 0) {
          const numberOfNotice = notices.length;

          FloatingModule.showFloatingDisMoiBubble(
            10,
            1500,
            numberOfNotice,
            notices,
            eventMessageFromChromeURL
          ).then(() => {
            console.log('show floating dis moi bubble');
          });
        }
        if (notices.length === 0) {
          FloatingModule.hideFloatingDisMoiBubble().then(() =>
            console.log('Hide Floating Bubble')
          );
          FloatingModule.hideFloatingDisMoiMessage().then(() =>
            console.log('Hide Floating Bubble')
          );
        }
      }
      if (isValidHttpUrl(eventMessageFromChromeURL) === false) {
        FloatingModule.hideFloatingDisMoiBubble().then(() =>
          console.log('Hide Floating Bubble')
        );
        FloatingModule.hideFloatingDisMoiMessage().then(() =>
          console.log('Hide Floating Bubble')
        );
      }
    }

    manipulateFloatingModule();
  }, [eventMessageFromChromeURL]);

  return (
    <View style={{ flex: 1, flexDirection: 'column' }}>
      <TouchableOpacity
        onPress={() => {
          Linking.openURL('https://www.google.com');
        }}
      >
        <Image source={require('./images/demo.png')} />
      </TouchableOpacity>
    </View>
  );
}

const styles = {
  centerScreen: { flex: 1, justifyContent: 'center', alignItems: 'center' },
};

export default App;
