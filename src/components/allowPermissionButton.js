import React from 'react';
import { Button } from 'react-native';
import { Background } from '../nativeModules/get';

const NewModuleButton = () => {
  const onPress = () => {
    Background.redirectToAppAccessibilitySettings();
  };

  return (
    <Button
      title="Allow accessibility permission"
      color="#841584"
      onPress={onPress}
    />
  );
};

export default NewModuleButton;
