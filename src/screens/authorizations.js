import React from 'react';
import { Text, View, AppState } from 'react-native';
import { Switch, ListItem, Body, Right } from 'native-base';
import { Background } from '../nativeModules/get';
import Screen from '../components/screen';
import Button from '../components/button';
import Title from '../components/title';
import Paragraph from '../components/paragraph';
import Italic from '../components/italic';

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
    <Screen>
      <View
        style={{
          flex: 1,
          flexDirection: 'column',
          justifyContent: 'space-around',
        }}
      >
        <Title>Finaliser l'installation</Title>
        <Paragraph>
          Pour fonctionner, Dismoi nécessite encore les autorisations
          d'interagir avec votre navigateur.
        </Paragraph>
        <View>
          <ListItem icon style={{ marginBottom: 15 }}>
            <Body>
              <Text
                style={{
                  letterSpacing: 0.9,
                  fontFamily: 'Helvetica',
                  color: '#000000',
                  fontSize: 14,
                }}
              >
                Paramètres d'accessibilité
              </Text>
            </Body>
            <Right>
              <Switch
                trackColor={{ false: '#767577', true: '#2855a2' }}
                style={{ transform: [{ scaleX: 1.6 }, { scaleY: 1.5 }] }}
                thumbColor={'#f4f3f4'}
                onValueChange={() => {
                  Background.redirectToAppAccessibilitySettings().then(
                    () => {}
                  );
                }}
                value={accessibilityServiceIsEnabled}
              />
            </Right>
          </ListItem>
          <ListItem icon style={{ marginTop: 15 }}>
            <Body>
              <Text
                style={{
                  letterSpacing: 0.9,
                  fontFamily: 'Helvetica',
                  color: '#000000',
                  fontSize: 14,
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
                trackColor={{ false: '#767577', true: '#2855a2' }}
                thumbColor={'#f4f3f4'}
                value={switchValueForOverlay}
              />
            </Right>
          </ListItem>
        </View>
        <Italic>
          Pour rappel, DisMoi n'exploite et ne revend aucune donnée personnelle.
        </Italic>
      </View>
      <View
        style={{
          flexDirection: 'column',
          justifyContent: 'flex-end',
        }}
      >
        <Button
          text={'Terminer'}
          backgroundColor={
            accessibilityServiceIsEnabled === false ||
            switchValueForOverlay === false
              ? '#a9a9a9'
              : '#2855a2'
          }
          onPress={() => {
            return navigation.navigate('Finished');
          }}
          disabled={
            accessibilityServiceIsEnabled === false ||
            switchValueForOverlay === false
          }
        />
      </View>
    </Screen>
  );
}

export default Authorizations;
