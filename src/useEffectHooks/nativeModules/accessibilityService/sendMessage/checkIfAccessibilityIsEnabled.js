import React from 'react';
import { Background } from '../../../../nativeModules/get';

function useFloatingBubbleRequestPermissionEffect(appState) {
  const [
    isAccessibilityServiceEnabled,
    setIsAccessibilityServiceEnabled,
  ] = React.useState(false);

  React.useEffect(() => {
    function callIsAccessibilityEnabledMethod() {
      Background.isAccessibilityEnabled((result) => {
        if (result === '1') {
          setIsAccessibilityServiceEnabled(true);
        }
        if (result === '0') {
          setIsAccessibilityServiceEnabled(false);
        }
      });
    }

    callIsAccessibilityEnabledMethod();
  }, [appState]);

  console.log('is accessibility enable');
  console.log(isAccessibilityServiceEnabled);

  return isAccessibilityServiceEnabled;
}

export default useFloatingBubbleRequestPermissionEffect;
