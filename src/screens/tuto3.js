import React from 'react';
import {
  Text,
  View,
  ImageBackground,
  StyleSheet,
  TouchableOpacity,
  Image,
  useWindowDimensions,
} from 'react-native';
import Balloon from 'react-native-balloon';
import Paragraph from '../components/paragraph';
import Screen from '../components/screen';

function Tuto3({ navigation }) {
  const window = useWindowDimensions();
  return (
    <Screen>
      <Paragraph>
        Consultez la contribution, puis revenez à votre navigation.
      </Paragraph>
      <View style={styles.container}>
        <ImageBackground
          source={require('../assets/images/webpageSnapshot.png')}
          style={styles.image}
        >
          <TouchableOpacity
            onPress={() => {
              return navigation.navigate('Authorizations');
            }}
            style={{
              flex: 1,
              flexDirection: 'column',
              justifyContent: 'flex-end',
            }}
          >
            <View style={{ top: 160, zIndex: 1 }}>
              <Balloon
                borderColor="#2855a2"
                backgroundColor="white"
                borderWidth={5}
                triangleOffset={'88%'}
                borderRadius={10}
                triangleSize={15}
                triangleDirection={'bottom'}
                width={200}
                containerStyle={{ left: 90 }}
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
                  Cliquez ici
                </Text>
              </Balloon>
            </View>

            <Image
              style={{ top: 70, width: window.width - 50 }}
              source={require('../assets/images/selon-le-monde.png')}
              resizeMode="contain"
            />
          </TouchableOpacity>
        </ImageBackground>
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

export default Tuto3;
