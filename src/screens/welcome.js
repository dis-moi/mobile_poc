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
        <Text style={{ textAlign: 'center' }}>DISMOI</Text>
        <View
          style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}
        >
          <Text style={{ textAlign: 'center' }}>
            Merci d'avoir téléchargé l'application DisMoi. {'\n'}
          </Text>
          <Text style={{ textAlign: 'center' }}>
            Conseils et entraide directement sur les pages web que vous visitez.
            {'\n'}
          </Text>
          <Text style={{ textAlign: 'center' }}>
            S'il existe une meilleure alternative, une info éclairante, vous le
            saurez! {'\n'}
          </Text>
          <Text style={{ textAlign: 'center' }}>
            Gratuit, sans publicité, respecte votre vie privée et ne ralentit
            pas votre mobile {'\n'}
          </Text>
          <TouchableOpacity>
            <Text style={{ textAlign: 'center' }}>CGU?</Text>
          </TouchableOpacity>
        </View>
        <View
          style={{
            flexDirection: 'column',
            justifyContent: 'flex-end',
          }}
        >
          <Button
            title="Suivant"
            onPress={() => {
              return navigation.navigate('Tuto1');
            }}
          />
        </View>
      </Content>
    </Container>
  );
}

export default Welcome;
