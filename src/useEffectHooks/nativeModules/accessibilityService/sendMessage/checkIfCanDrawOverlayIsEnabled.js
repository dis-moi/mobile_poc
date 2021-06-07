import React from 'react';
import { Background } from '../../../../nativeModules/get';

function CheckIfCanDrawOverlayIsEnabledEffect(appState) {
  const [
    canDrawOverlayServiceEnabled,
    setCanDrawOverlayServiceEnabled,
  ] = React.useState(false);

  React.useEffect(() => {
    function callCheckIfOverlayEnabledMethod() {
      Background.canDrawOverlay((result) => {
        if (result === '1') {
          setCanDrawOverlayServiceEnabled(true);
        }
        if (result === '0') {
          setCanDrawOverlayServiceEnabled(false);
        }
      });
    }

    callCheckIfOverlayEnabledMethod();
  }, [appState]);

  return canDrawOverlayServiceEnabled;
}

export default CheckIfCanDrawOverlayIsEnabledEffect;
