import React from 'react';
import { TouchableOpacity, Dimensions, View, Text } from 'react-native';
import { Icon } from 'native-base';

function Button({
  onPress,
  text,
  backgroundColor = '#2855a2',
  disabled,
  small,
  border,
  icon,
  color = 'white',
}) {
  return (
    <TouchableOpacity
      style={{
        height: small ? 50 : 60,
        backgroundColor: backgroundColor,
        borderColor: border ? 'black' : backgroundColor,
        borderWidth: 2,
        borderRadius: 10,
        justifyContent: 'space-around',
        width: small ? 130 : Dimensions.get('window').width - 50,
        flexDirection: 'row',
      }}
      onPress={onPress}
      disabled={disabled}
    >
      {icon && (
        <View style={{ justifyContent: 'center', paddingLeft: 5 }}>
          <Icon type="Entypo" name="check" />
        </View>
      )}
      <View style={{ justifyContent: 'center', paddingRight: icon ? 5 : 0 }}>
        <Text
          style={{
            letterSpacing: 0.9,
            fontFamily: 'Helvetica',
            fontWeight: 'bold',
            color: color,
            fontSize: 17,
          }}
        >
          {text}
        </Text>
      </View>
    </TouchableOpacity>
  );
}

export default Button;
