import React from 'react';
import {FloatingBubble} from '../../nativeModules/get';

function useFloatingBubbleInitializeEffect() {
  React.useEffect(() => {
    function callFloatingBubbleInitializeMethod() {
      FloatingBubble.initialize().then(() =>
        console.log('Initialized the bubble'),
      );
    }

    callFloatingBubbleInitializeMethod();
  }, []);
}

export default useFloatingBubbleInitializeEffect;
