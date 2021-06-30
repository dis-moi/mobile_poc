import React from 'react';
import { Background } from '../nativeModules/get';

function StartBackgroundServiceUseEffect() {
  React.useEffect(() => {
    function startDisMoiAppInBackground() {
      Background.startService();
    }
    startDisMoiAppInBackground();
  }, []);
}

export default StartBackgroundServiceUseEffect;
