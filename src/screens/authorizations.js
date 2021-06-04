import React from 'react';
import { Text, View, Button, AppState } from 'react-native';
import { Container, Content, Switch, ListItem, Body, Right } from 'native-base';
import { Background } from '../nativeModules/get';

import { EVENT_FROM_ACCESSIBILITY_SERVICE_PERMISSION } from '../nativeModules/eventListToListen';

import useAccessibilityServiceEventToListenFromNativeModuleEffect from '../useEffectHooks/nativeModules/accessibilityService/listenMessage/eventToListenFromNativeModule';
import useCheckIfAccessibilityIsEnabledEffect from '../useEffectHooks/nativeModules/accessibilityService/sendMessage/checkIfAccessibilityIsEnabled';

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

  // const eventMessageFromAccessibilityServicePermission = useAccessibilityServiceEventToListenFromNativeModuleEffect(
  //   EVENT_FROM_ACCESSIBILITY_SERVICE_PERMISSION
  // );
  // console.log(eventMessageFromAccessibilityServicePermission);

  React.useEffect(() => {
    console.log('can draw overlay');
    console.log(appState);

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
      console.log('accessibility service is enabled');
      console.log(appState);

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
        <Text style={{ textAlign: 'center' }}>DISMOI</Text>
        <View
          style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}
        >
          <Text style={{ textAlign: 'center' }}>
            DisMoi est bien installé. {'\n'}
          </Text>
          <Text style={{ textAlign: 'center' }}>
            Pour fonctionner, il nécessite encore les autorisations d'interagir
            avec votre navigateur.
            {'\n'}
          </Text>
          <Text style={{ textAlign: 'center' }}>
            Pour rappel, DisMoi n'exploite et ne revend aucune donnée
            personnelle. {'\n'}
          </Text>
          <ListItem icon>
            <Body>
              <Text>Paramètres d'accessibilité</Text>
            </Body>
            <Right>
              <Switch
                onValueChange={(value) => {
                  Background.redirectToAppAccessibilitySettings().then(
                    () => {}
                  );
                }}
                value={accessibilityServiceIsEnabled}
              />
            </Right>
          </ListItem>
          <ListItem icon>
            <Body>
              <Text>Superposition</Text>
            </Body>
            <Right>
              <Switch
                onValueChange={() => {
                  Background.redirectToAppAdvancedSettings().then(() => {});
                }}
                value={switchValueForOverlay}
              />
            </Right>
          </ListItem>
        </View>
        <View
          style={{
            flexDirection: 'column',
            justifyContent: 'flex-end',
          }}
        >
          <Button
            title="Terminer"
            disabled={
              accessibilityServiceIsEnabled === false ||
              switchValueForOverlay === false
            }
            onPress={() => {
              return navigation.navigate('Finished');
            }}
          />
        </View>
      </Content>
    </Container>
  );
}

export default Authorizations;
