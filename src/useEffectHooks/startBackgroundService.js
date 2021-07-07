import React from 'react';

function StartBackgroundServiceUseEffect() {
  React.useEffect(() => {
    function startDisMoiAppInBackground() {}
    startDisMoiAppInBackground();
  }, []);
}

export default StartBackgroundServiceUseEffect;
