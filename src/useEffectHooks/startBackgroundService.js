import React from 'react';
import { Background } from '../nativeModules/get';

function StartBackgroundServiceUseEffect(isAccessibilityEnabled) {
  React.useEffect(() => {
    function startDisMoiAppInBackground() {
      if (isAccessibilityEnabled) {
        Background.startService();
      }
    }
    startDisMoiAppInBackground();
  }, [isAccessibilityEnabled]);
}

export default StartBackgroundServiceUseEffect;
