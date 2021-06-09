import React from 'react';
import { Text } from 'react-native';

function Italic({ children }) {
  return (
    <Text
      style={{
        letterSpacing: 0.9,
        textAlign: 'center',
        fontFamily: 'Helvetica',
        color: '#000000',
        fontStyle: 'italic',
      }}
    >
      {children}
    </Text>
  );
}

export default Italic;
