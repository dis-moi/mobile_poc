import React from 'react';
import { Background } from '../../../../nativeModules/get';

function useFloatingBubbleRequestPermissionEffect(appState) {
  const [
    isAccessibilityServiceEnabled,
    setIsAccessibilityServiceEnabled,
  ] = React.useState(false);

  React.useEffect(() => {
    let cancelled = false;

    Background.isAccessibilityEnabled((result) => {
      if (!cancelled) {
        if (result === '1') {
          setIsAccessibilityServiceEnabled(true);
        }
        if (result === '0') {
          setIsAccessibilityServiceEnabled(false);
        }
      }
    });

    return () => {
      cancelled = true;
    };
  }, [appState]);

  return isAccessibilityServiceEnabled;
}

export default useFloatingBubbleRequestPermissionEffect;
