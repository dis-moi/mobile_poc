import React from 'react';
import { Text, View, AppState, TouchableOpacity } from 'react-native';
import { Container, Content, Switch, ListItem, Body, Right } from 'native-base';
import { Background } from '../nativeModules/get';

function Authorizations({ navigation }) {
  const [switchValueForOverlay, setSwitchValueForOverlay] = React.useState(
    false
  );

  const [
    accessibilityServiceIsEnabled,
    setAccessibilityServiceEnabled,
  ] = React.useState(false);

  const [appState, setAppState] = React.useState(false);

  function handleAppStateChange(nextAppState) {
    setAppState(nextAppState);
  }

  React.useEffect(() => {
    AppState.addEventListener('change', handleAppStateChange);

    return () => {
      AppState.removeEventListener('change', handleAppStateChange);
    };
  }, []);

  React.useEffect(() => {
    Background.canDrawOverlay((result) => {
      if (result === '1') {
        setSwitchValueForOverlay(true);
      }
      if (result === '0') {
        setSwitchValueForOverlay(false);
      }
    });
  }, [appState]);

  React.useEffect(() => {
    function callIsAccessibilityEnabledMethod() {
      Background.isAccessibilityEnabled((result) => {
        if (result === '1') {
          setAccessibilityServiceEnabled(true);
        }
        if (result === '0') {
          setAccessibilityServiceEnabled(false);
        }
      });
    }

    callIsAccessibilityEnabledMethod();
  }, [appState]);

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
            DisMoi est bien installé.
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
            Pour fonctionner, il nécessite encore les autorisations d'interagir
            avec votre navigateur.
          </Text>
          <View>
            <ListItem icon>
              <Body>
                <Text
                  style={{
                    letterSpacing: 0.9,
                    fontFamily: 'Helvetica',
                    color: '#000000',
                    fontSize: 18,
                  }}
                >
                  Paramètres d'accessibilité
                </Text>
              </Body>
              <Right>
                <Switch
                  style={{ transform: [{ scaleX: 1.6 }, { scaleY: 1.5 }] }}
                  onValueChange={() => {
                    Background.redirectToAppAccessibilitySettings().then(
                      () => {}
                    );
                  }}
                  trackColor={{ false: '#ffffff', true: '#2855a2' }}
                  thumbColor={'#ffffff'}
                  value={accessibilityServiceIsEnabled}
                />
              </Right>
            </ListItem>
            <ListItem icon>
              <Body>
                <Text
                  style={{
                    letterSpacing: 0.9,
                    fontFamily: 'Helvetica',
                    color: '#000000',
                    fontSize: 18,
                  }}
                >
                  Superposition
                </Text>
              </Body>
              <Right>
                <Switch
                  onValueChange={() => {
                    Background.redirectToAppAdvancedSettings().then(() => {});
                  }}
                  style={{ transform: [{ scaleX: 1.6 }, { scaleY: 1.5 }] }}
                  trackColor={{ false: '#ffffff', true: '#2855a2' }}
                  thumbColor={'#ffffff'}
                  value={switchValueForOverlay}
                />
              </Right>
            </ListItem>
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
            Pour rappel, DisMoi n'exploite et ne revend aucune donnée
            personnelle.
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
              backgroundColor:
                accessibilityServiceIsEnabled === false ||
                switchValueForOverlay === false
                  ? '#a9a9a9'
                  : '#2855a2',
              borderRadius: 10,
              justifyContent: 'center',
            }}
            onPress={() => {
              return navigation.navigate('Finished');
            }}
            disabled={
              accessibilityServiceIsEnabled === false ||
              switchValueForOverlay === false
            }
          >
            <Text style={{ textAlign: 'center', color: 'white', fontSize: 28 }}>
              Terminer
            </Text>
          </TouchableOpacity>
        </View>
      </Content>
    </Container>
  );
}

export default Authorizations;
