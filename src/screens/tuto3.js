import React from 'react';
import { Text, View, ImageBackground, StyleSheet } from 'react-native';

function Tuto2() {
  return (
    <View style={styles.container}>
      <Text style={{ textAlign: 'center' }}>DISMOI</Text>
      <Text style={{ textAlign: 'center' }}>
        Consultez les éclairages {'\n'}
      </Text>
      <View style={styles.container}>
        <ImageBackground
          source={require('../images/webpageSnapshot.png')}
          style={styles.image}
        >
          {/* <View style={{ flex: 1 }}>
            <View style={styles.reminderView}>
              <TouchableOpacity style={styles.reminderTouch}>
                <Text style={styles.reminderBtn}>+</Text>
              </TouchableOpacity>
            </View>
          </View> */}
          <View
            style={{
              flex: 1,
              flexDirection: 'column',
              justifyContent: 'flex-end',
            }}
          >
            {/* <View
              style={{
                flex: 1,
                flexDirection: 'row',
                justifyContent: 'flex-end',
              }}
            > */}
          </View>
          {/* </View> */}
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
