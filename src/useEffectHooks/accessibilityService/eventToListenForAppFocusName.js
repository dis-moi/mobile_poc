import React from 'react';
import { NativeEventEmitter } from 'react-native';
import { AccessibilityServiceModule } from '../../nativeModules/get';

function useAccessibilityServiveEventForAppNameFocusEffect(
  eventToListenForAppNameFocus
) {
  let eventListener = React.useRef(null);

  const [eventMessage, setEventMessage] = React.useState('');

  React.useEffect(() => {
    function createListener() {
      const eventEmitter = new NativeEventEmitter(AccessibilityServiceModule);
      eventListener.current = eventEmitter.addListener(
        eventToListenForAppNameFocus,
        (event) => {
          setEventMessage(event);
        }
      );
    }

    createListener();

    return () => {
      eventListener.current.remove();
    };
  }, [eventToListenForAppNameFocus]);

  return eventMessage;
}

export default useAccessibilityServiveEventForAppNameFocusEffect;
