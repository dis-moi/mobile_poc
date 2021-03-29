/* eslint-disable no-console */
import React from 'react';
import { FloatingModule } from '../../nativeModules/get';

function useFloatingLayoutInitializeEffect() {
  React.useEffect(() => {
    function callFloatingLayoutInitializeMethod() {
      FloatingModule.initialize().then(() =>
        console.log('Initialized the bubble')
      );
    }

    callFloatingLayoutInitializeMethod();
  }, []);
}

export default useFloatingLayoutInitializeEffect;
