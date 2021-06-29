import React from 'react';
import { Image } from 'react-native';

function LogoTitle() {
  return (
    <Image
      style={{ width: 100, height: 50 }}
      source={require('../assets/images/disMoi.png')}
      resizeMode="contain"
    />
  );
}

export default function stackScreenOptions() {
  return {
    headerTitle: () => <LogoTitle />,
    headerTitleAlign: 'center',
    headerLeft: () => null,
    headerStyle: {
      backgroundColor: '#2855a2',
    },
    headerTintColor: '#fff',
  };
}
