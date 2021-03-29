import React from 'react';
import { NativeEventEmitter } from 'react-native';
import { AccessibilityServiceModule } from '../../nativeModules/get';

function useAccessibilityServiveEventToListenFromNativeModuleEffect(
  eventToListenFromNativeModule
) {
  let eventListener = React.useRef(null);

  const [eventMessage, setEventMessage] = React.useState('');

  React.useEffect(() => {
    function createListener() {
      const eventEmitter = new NativeEventEmitter(AccessibilityServiceModule);
      eventListener.current = eventEmitter.addListener(
        eventToListenFromNativeModule,
        (event) => {
          setEventMessage(event);
        }
      );
    }

    createListener();

    return () => {
      eventListener.current.remove();
    };
  }, [eventToListenFromNativeModule]);

  return eventMessage;
}

export default useAccessibilityServiveEventToListenFromNativeModuleEffect;
