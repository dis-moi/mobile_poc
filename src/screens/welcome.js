import React from 'react';
import { StatusBar, Text, View } from 'react-native';
import Title from '../components/title';
import Paragraph from '../components/paragraph';
import Italic from '../components/italic';
import Screen from '../components/screen';
import Button from '../components/button';

function Welcome({ navigation }) {
  return (
    <Screen>
      <View
        style={{
          flex: 1,
          flexDirection: 'column',
          justifyContent: 'space-around',
        }}
      >
        <Title>Merci d'avoir téléchargé l'application DisMoi.</Title>
        <View>
          <Paragraph>
            Conseils et entraide directement sur les pages web que vous visitez.
          </Paragraph>
          <Paragraph>
            S'il existe une meilleure alternative, une info éclairante, vous le
            saurez!
          </Paragraph>
        </View>
        <Italic>
          Gratuit, sans publicité, respecte votre vie privée et ne ralentit pas
          votre mobile.
        </Italic>
      </View>
      <View
        style={{
          flexDirection: 'column',
          justifyContent: 'flex-end',
        }}
      >
        <Button
          onPress={() => {
            return navigation.navigate('Tuto1');
          }}
          text={'Suivant'}
        />
      </View>
    </Screen>
  );
}

export default Welcome;
