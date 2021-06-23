import React from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';

function EverythingIsReady() {
  React.useEffect(() => {
    AsyncStorage.getItem('alreadyLaunched').then((value) => {
      if (value == null) {
        AsyncStorage.setItem('alreadyLaunched', 'true');
      }
    });
  }, []);
}

export default EverythingIsReady;
