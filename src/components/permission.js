import React from 'react';
import {Text} from 'react-native';

function Permission(props) {
  if (props.isAccessibilityServiceEnabled === true) {
    return (
      <Text
        style={{
          textAlign: 'center',
          color: 'green',
          paddingLeft: 40,
          paddingRight: 40,
        }}>
        You can now browse on the chrome app with dis_moi poc
      </Text>
    );
  }

  return (
    <Text
      style={{
        textAlign: 'center',
        color: 'tomato',
        paddingLeft: 40,
        paddingRight: 40,
      }}>
      Please enable accessibility service for dismoi_poc to work well
    </Text>
  );
}

export default Permission;
