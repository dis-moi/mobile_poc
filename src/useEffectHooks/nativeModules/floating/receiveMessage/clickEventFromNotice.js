import React from 'react';
import { DeviceEventEmitter } from 'react-native';
import { FloatingModule } from '../../../../nativeModules/get';

function useClickEventFromNoticeEffect() {
  React.useEffect(() => {
    function callActionListeners() {
      DeviceEventEmitter.addListener('floating-dismoi-bubble-press', (e) => {
        return FloatingModule.showFloatingDisMoiMessage(10, 1500).then(() => {
          // What to do when user press on the bubble
          console.log('Bubble press');
        });
      });

      DeviceEventEmitter.addListener('floating-dismoi-bubble-remove', (e) => {
        // What to do when user removes the bubble
        return console.log('Remove Bubble');
      });

      DeviceEventEmitter.addListener('floating-dismoi-message-remove', (e) => {
        // What to do when user removes the message
        return console.log('DisMoi message remove');
      });

      DeviceEventEmitter.addListener('floating-dismoi-message-press', (e) => {
        // What to do when user press on the message
        return FloatingModule.hideFloatingDisMoiMessage().then(() =>
          console.log('Hide Floating DisMoiMessage')
        );
      });
    }

    callActionListeners();
  }, []);
}

export default useClickEventFromNoticeEffect;
