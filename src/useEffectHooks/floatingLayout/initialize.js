/* eslint-disable no-console */
import React from 'react';
import { FloatingLayout } from '../../nativeModules/get';

function useFloatingLayoutInitializeEffect() {
  React.useEffect(() => {
    function callFloatingLayoutInitializeMethod() {
      FloatingLayout.initialize().then(() =>
        console.log('Initialized the bubble')
      );
    }

    callFloatingLayoutInitializeMethod();
  }, []);
}

export default useFloatingLayoutInitializeEffect;
