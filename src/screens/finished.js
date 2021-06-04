import React from 'react';
import { Text, View, Button, Linking } from 'react-native';
import { Container, Content } from 'native-base';

function Finished({ navigation }) {
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
            justifyContent: 'space-between',
          }}
        >
          <Text style={{ textAlign: 'center' }}>DISMOI</Text>
          <Text style={{ textAlign: 'center' }}>Tout est prêt {'\n'}</Text>
          <Text style={{ textAlign: 'center' }}>
            Nous vous avons automatiquement abonné à tout le monde {'\n'}
          </Text>
          <View
            style={{
              flexDirection: 'column',
              justifyContent: 'flex-end',
            }}
          >
            <Button
              title="Naviguez informé(e)"
              onPress={() => {
                Linking.openURL('https://www.google.com');
              }}
            />
          </View>
        </View>
      </Content>
    </Container>
  );
}

export default Finished;
