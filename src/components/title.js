import React from 'react';
import { Text } from 'react-native';

function Title({ children }) {
  return (
    <Text
      style={{
        textAlign: 'center',
        fontFamily: 'Helvetica-Bold',
        fontSize: 25,
        letterSpacing: 0.73,
        color: '#000000',
      }}
    >
      {children}
    </Text>
  );
}

export default Title;
