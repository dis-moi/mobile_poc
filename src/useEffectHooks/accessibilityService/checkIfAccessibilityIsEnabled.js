import React from 'react';
import { AccessibilityServiceModule } from '../../nativeModules/get';

function useFloatingBubbleRequestPermissionEffect(
  eventMessageFromAccessibilityServicePermission
) {
  const [
    isAccessibilityServiceEnabled,
    setIsAccessibilityServiceEnabled,
  ] = React.useState(false);

  React.useEffect(() => {
    function callIsAccessibilityEnabledMethod() {
      AccessibilityServiceModule.isAccessibilityEnabled((result) => {
        if (result === '1') {
          setIsAccessibilityServiceEnabled(true);
        }
        if (result === '0') {
          setIsAccessibilityServiceEnabled(false);
        }
      });
    }

    callIsAccessibilityEnabledMethod();
  }, [eventMessageFromAccessibilityServicePermission]);

  return isAccessibilityServiceEnabled;
}

export default useFloatingBubbleRequestPermissionEffect;
