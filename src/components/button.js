import React from 'react';
import { TouchableOpacity } from 'react-native';
import Paragraph from './paragraph';

function Button({ onPress, text, backgroundColor, disabled }) {
  return (
    <TouchableOpacity
      style={{
        height: 50,
        backgroundColor: backgroundColor || '#2855a2',
        borderRadius: 10,
        justifyContent: 'center',
      }}
      onPress={onPress}
      disabled={disabled}
    >
      <Paragraph bold color={'white'}>
        {text}
      </Paragraph>
    </TouchableOpacity>
  );
}

export default Button;
