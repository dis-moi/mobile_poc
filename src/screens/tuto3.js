import React from 'react';
import {
  Text,
  View,
  ImageBackground,
  StyleSheet,
  TouchableOpacity,
  Image,
} from 'react-native';
import Balloon from 'react-native-balloon';

function Tuto2({ navigation }) {
  return (
    <View style={styles.container}>
      <Text style={{ textAlign: 'center' }}>DISMOI</Text>
      <Text style={{ textAlign: 'center' }}>
        Consultez les éclairages {'\n'}
      </Text>
      <View style={styles.container}>
        <ImageBackground
          source={require('../assets/images/webpageSnapshot.png')}
          style={styles.image}
        >
          <View
            style={{
              flex: 1,
              flexDirection: 'column',
              justifyContent: 'flex-end',
            }}
          >
            <Balloon
              borderColor="#2E86C1"
              backgroundColor="#D6EAF8"
              borderWidth={2}
              triangleOffset={'7%'}
              borderRadius={20}
              triangleSize={15}
              triangleDirection={'bottom'}
              containerStyle={{ right: 10 }}
              onPress={() => console.log('press')}
            >
              <Text>Cliquez pour voir le détail!</Text>
            </Balloon>
            <TouchableOpacity
              onPress={() => {
                return navigation.navigate('Tuto4');
              }}
            >
              <Image
                style={{ bottom: 0 }}
                source={require('../assets/images/dismoi_round.png')}
              />
            </TouchableOpacity>
          </View>
        </ImageBackground>
        {/* <ImageBackground source={require('../images/webpageSnapshot.png')} /> */}
      </View>
    </View>
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
