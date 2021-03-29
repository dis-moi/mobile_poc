import React from 'react';
import { Button } from 'react-native';
import { AccessibilityServiceModule } from '../nativeModules/get';

const NewModuleButton = () => {
  const onPress = () => {
    AccessibilityServiceModule.redirectToAppAccessibilitySettings().then(
      () => {}
    );
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
