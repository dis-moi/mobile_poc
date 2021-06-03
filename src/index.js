import React from 'react';
import useNativeModuleEffects from './useNativeModuleEffects';
import { Container, Content } from 'native-base';
import { TouchableOpacity, View, Linking, Button, Text } from 'react-native';

import { Background } from './nativeModules/get';

function App() {
  useNativeModuleEffects();

  React.useEffect(() => {
    function startDisMoiAppInBackground() {
      Background.startService();
    }
    startDisMoiAppInBackground();
  }, []);

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
          <TouchableOpacity
            onPress={() => {
              Linking.openURL('https://www.dismoi.io/cgu/');
            }}
          >
            <Text style={{ textAlign: 'center' }}>CGU?</Text>
          </TouchableOpacity>
        </View>
        <View
          style={{
            flexDirection: 'column',
            justifyContent: 'flex-end',
          }}
        >
          <Button title="Suivant" />
        </View>
      </Content>
    </Container>
  );
}

export default App;
