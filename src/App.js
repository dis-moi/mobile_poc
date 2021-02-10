import React from 'react';
import {Text, View} from 'react-native';
import AccessibilityServiceForChromeURL from './getAccessibilityServiceForChromeURLNativeModule';

const RANDOM_NUMBER_TO_TEST = 123;

function App() {
  const [
    successMessageFromNativeModule,
    setSuccessMessageFromNativeModule,
  ] = React.useState('');

  React.useEffect(() => {
    AccessibilityServiceForChromeURL.sampleMethod(
      'TestingIfReactNativeIsConnectedToChromeURLNativeModule',
      RANDOM_NUMBER_TO_TEST,
      (messageFromNativeModule) => {
        console.log('messageFromModule', messageFromNativeModule);
        setSuccessMessageFromNativeModule(messageFromNativeModule);
      },
    );
  }, []);

  return (
    <View style={styles.centerScreen}>
      <Text>Welcome to react native dismoi POC!</Text>
      <Text style={{color: 'green', paddingLeft: 20, paddingRight: 20}}>
        {successMessageFromNativeModule}
      </Text>
    </View>
  );
}

const styles = {
  centerScreen: {flex: 1, justifyContent: 'center', alignItems: 'center'},
};

export default App;
