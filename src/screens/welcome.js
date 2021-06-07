import React from 'react';
import { Text, View, Button, TouchableOpacity } from 'react-native';
import { Container, Content } from 'native-base';

function Welcome({ navigation }) {
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
            Merci d'avoir téléchargé l'application DisMoi.
          </Text>
          <View>
            <Text
              style={{
                letterSpacing: 0.9,
                textAlign: 'center',
                fontFamily: 'Helvetica',
                color: '#000000',
                fontSize: 18,
              }}
            >
              Conseils et entraide directement sur les pages web que vous
              visitez.
            </Text>
            <Text
              style={{
                letterSpacing: 0.9,
                textAlign: 'center',
                fontFamily: 'Helvetica',
                color: '#000000',
                fontSize: 18,
              }}
            >
              S'il existe une meilleure alternative, une info éclairante, vous
              le saurez!
            </Text>
          </View>
          <Text
            style={{
              letterSpacing: 0.9,
              textAlign: 'center',
              fontFamily: 'Helvetica',
              color: '#000000',
              fontStyle: 'italic',
            }}
          >
            Gratuit, sans publicité, respecte votre vie privée et ne ralentit
            pas votre mobile.
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
              backgroundColor: '#1e52b4',
              borderRadius: 10,
              justifyContent: 'center',
            }}
            onPress={() => {
              return navigation.navigate('Tuto1');
            }}
          >
            <Text style={{ textAlign: 'center', color: 'white', fontSize: 28 }}>
              Suivant
            </Text>
          </TouchableOpacity>
        </View>
      </Content>
    </Container>
  );
}

export default Welcome;
