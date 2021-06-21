import React from 'react';
import {
  Text,
  View,
  ImageBackground,
  Image,
  StyleSheet,
  TouchableOpacity,
} from 'react-native';
import Balloon from 'react-native-balloon';
import Paragraph from '../components/paragraph';
import Screen from '../components/screen';

function Tuto2({ navigation }) {
  return (
    <Screen>
      <View style={styles.container}>
        <Paragraph>Dismoi apparaît sur votre navigateur.</Paragraph>
        <View style={styles.container}>
          <ImageBackground
            source={require('../assets/images/webpageSnapshot.png')}
            style={styles.image}
          >
            <TouchableOpacity
              onPress={() => {
                return navigation.navigate('Tuto3');
              }}
              style={{
                flex: 1,
                flexDirection: 'column',
                justifyContent: 'flex-end',
              }}
            >
              <Balloon
                borderColor="#2855a2"
                backgroundColor="white"
                borderWidth={5}
                triangleOffset={'7%'}
                borderRadius={10}
                triangleSize={15}
                triangleDirection={'bottom'}
                containerStyle={{ right: 10 }}
              >
                <Text
                  style={{
                    letterSpacing: 0.9,
                    textAlign: 'center',
                    color: '#2855a2',
                    fontFamily: 'Helvetica-Bold',
                    fontSize: 18,
                  }}
                >
                  Ouvrir
                </Text>
              </Balloon>

              <Image
                style={{ bottom: 0, height: 80, width: 80 }}
                resizeMode="contain"
                source={require('../assets/images/logo-and-notif.png')}
              />
            </TouchableOpacity>
          </ImageBackground>
        </View>
      </View>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
  },
  image: {
    flex: 1,
    justifyContent: 'center',
  },
  text: {
    color: 'white',
    fontSize: 42,
    fontWeight: 'bold',
    textAlign: 'center',
    backgroundColor: '#000000a0',
  },
});

export default Tuto2;
