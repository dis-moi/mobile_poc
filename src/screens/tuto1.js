import React from 'react';
import { Text, View, Dimensions, Image } from 'react-native';

import Paragraph from '../components/paragraph';
import Screen from '../components/screen';
import Button from '../components/button';

export default function Tuto1({ navigation }) {
  return (
    <Screen>
      <View
        style={{
          flex: 1,
          flexDirection: 'column',
          justifyContent: 'space-around',
        }}
      >
        <Paragraph>
          Conso, culture, fact-checking, militant.. Les éclaireurs Dismoi vous
          conseillent et informent lorsque vous naviguez sur le web.
        </Paragraph>
        <View
          style={{
            flexDirection: 'row',
            justifyContent: 'space-around',
            flexWrap: 'wrap',
          }}
        >
          <Image
            style={{ height: 80, width: 80, margin: 5 }}
            source={require('../assets/images/quechoisir.png')}
          />
          <Image
            style={{ height: 80, width: 80, margin: 5 }}
            source={require('../assets/images/lemonde.png')}
          />
          <Image
            style={{ height: 80, width: 80, margin: 5 }}
            source={require('../assets/images/lesinrocks.png')}
          />
          <Image
            style={{ height: 80, width: 80, margin: 5 }}
            source={require('../assets/images/60millions.png')}
          />
          <Image
            style={{ height: 80, width: 80, margin: 5 }}
            source={require('../assets/images/selonMicode.png')}
          />
          <Image
            style={{ height: 80, width: 80, margin: 5 }}
            source={require('../assets/images/lesNumeriques.png')}
          />
          <Image
            style={{ height: 80, width: 80, margin: 5 }}
            source={require('../assets/images/wirecutter.png')}
          />
        </View>
        <Text
          style={{
            letterSpacing: 0.9,
            textAlign: 'center',
            fontFamily: 'Helvetica',
            color: '#000000',
            fontSize: 18,
          }}
        >
          Et des dizaines d'autres...
        </Text>
      </View>
      <View
        style={{
          flexDirection: 'column',
          justifyContent: 'flex-end',
        }}
      >
        <Button
          onPress={() => {
            return navigation.navigate('Tuto2');
          }}
          text={'Suivant'}
        />
      </View>
    </Screen>
  );
}
