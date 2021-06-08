import React from 'react';
import { Text, View, Button, Linking, TouchableOpacity } from 'react-native';
import { Container, Content } from 'native-base';
import { Background } from '../nativeModules/get';
import AsyncStorage from '@react-native-async-storage/async-storage';

function Finished({ navigation }) {
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
    <Container>
      <Content
        padder
        contentContainerStyle={{
          flex: 1,
        }}
      >
        <View
          style={{
            flex: 1,
            flexDirection: 'column',
            justifyContent: 'space-around',
          }}
        >
          <Text
            style={{
              textAlign: 'center',
              fontFamily: 'Helvetica-Bold',
              fontSize: 28,
              letterSpacing: 0.73,
              color: '#000000',
            }}
          >
            Tout est prêt.
          </Text>
          <Text
            style={{
              letterSpacing: 0.9,
              textAlign: 'center',
              fontFamily: 'Helvetica',
              color: '#000000',
            }}
          >
            Nous vous avons automatiquement abonné à tout le monde.
          </Text>
        </View>
        <View
          style={{
            flexDirection: 'column',
            justifyContent: 'flex-end',
          }}
        >
          <TouchableOpacity
            style={{
              height: 70,
              backgroundColor: '#2855a2',
              borderRadius: 10,
              justifyContent: 'center',
            }}
            onPress={() => {
              return Linking.openURL('https://www.backmarket.fr');
            }}
          >
            <Text style={{ textAlign: 'center', color: 'white', fontSize: 28 }}>
              Naviguez informé(e)
            </Text>
          </TouchableOpacity>
        </View>
      </Content>
    </Container>
    // <Container>
    //   <Content
    //     padder
    //     contentContainerStyle={{
    //       flex: 1,
    //     }}
    //   >
    //     <View
    //       style={{
    //         flex: 1,
    //         flexDirection: 'column',
    //         justifyContent: 'space-between',
    //       }}
    //     >
    //       <Text style={{ textAlign: 'center' }}>DISMOI</Text>
    //       <Text style={{ textAlign: 'center' }}>Tout est prêt {'\n'}</Text>
    //       <Text style={{ textAlign: 'center' }}>
    //         Nous vous avons automatiquement abonné à tout le monde {'\n'}
    //       </Text>
    //       <View
    //         style={{
    //           flexDirection: 'column',
    //           justifyContent: 'flex-end',
    //         }}
    //       >
    //         <Button
    //           title="Naviguez informé(e)"
    //           onPress={() => {
    //             Linking.openURL('https://www.google.com');
    //           }}
    //         />
    //       </View>
    //     </View>
    //   </Content>
    // </Container>
  );
}

export default Finished;
