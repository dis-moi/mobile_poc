import React from 'react';
import {Text, View, NativeEventEmitter} from 'react-native';
import AccessibilityServiceForChromeURL from './getAccessibilityServiceForChromeURLNativeModule';

const RANDOM_NUMBER_TO_TEST = 123;

function App() {
  const [
    successMessageFromNativeModule,
    setSuccessMessageFromNativeModule,
  ] = React.useState('');

  const [
    eventMessageFromChromeURLNativeModule,
    setEventMessageFromChromeURLNativeModule,
  ] = React.useState('');

  let eventEmitterListener = React.useRef(null);

  React.useEffect(() => {
    AccessibilityServiceForChromeURL.sampleMethod(
      'TestingIfReactNativeIsConnectedToChromeURLNativeModule',
      RANDOM_NUMBER_TO_TEST,
      (messageFromNativeModule) => {
        setSuccessMessageFromNativeModule(messageFromNativeModule);
      },
    );

    const eventEmitter = new NativeEventEmitter(
      AccessibilityServiceForChromeURL,
    );
    eventEmitterListener.current = eventEmitter.addListener(
      'EventFromChromeURLNativeModule',
      (event) => {
        setEventMessageFromChromeURLNativeModule(event);
      },
    );

    return () => {
      eventEmitterListener.current.remove();
    };
  }, []);

  return (
    <View style={styles.centerScreen}>
      <Text>Welcome to react native dismoi POC!</Text>
      <Text style={{color: 'green', paddingLeft: 20, paddingRight: 20}}>
        {successMessageFromNativeModule}
      </Text>
      <Text style={{color: 'tomato', paddingLeft: 20, paddingRight: 20}}>
        {eventMessageFromChromeURLNativeModule}
      </Text>
    </View>
  );
}

const styles = {
  centerScreen: {flex: 1, justifyContent: 'center', alignItems: 'center'},
};

export default App;
