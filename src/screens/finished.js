import React from 'react';
import { Text, View, Linking, TouchableOpacity } from 'react-native';

import { Background } from '../nativeModules/get';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Title from '../components/title';
import Paragraph from '../components/paragraph';
import Screen from '../components/screen';
import Button from '../components/button';

function Finished() {
  React.useEffect(() => {
    function startDisMoiAppInBackground() {
      Background.startService();
    }
    startDisMoiAppInBackground();
  }, []);

  React.useEffect(() => {
    AsyncStorage.getItem('alreadyLaunched').then((value) => {
      if (value == null) {
        AsyncStorage.setItem('alreadyLaunched', 'true'); // No need to wait for `setItem` to finish, although you might want to handle errors
      }
    });
  }, []);

  return (
    <Screen>
      <View
        style={{
          flex: 1,
          flexDirection: 'column',
          justifyContent: 'space-around',
        }}
      >
        <Title>Tout est prêt.</Title>
        <Paragraph>
          Nous vous avons automatiquement abonné à tout le monde.
        </Paragraph>
      </View>
      <View
        style={{
          flexDirection: 'column',
          justifyContent: 'flex-end',
        }}
      >
        <Button
          onPress={() => {
            return Linking.openURL('https://www.backmarket.fr');
          }}
          text={'Naviguez informé(e)'}
        />
      </View>
    </Screen>
  );
}

export default Finished;
