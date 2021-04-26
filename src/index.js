import React from 'react';
import { Text, TouchableOpacity, View, Button } from 'react-native';
import useNativeModuleEffects from './useNativeModuleEffects';
import {
  List,
  ListItem,
  Thumbnail,
  Left,
  Body,
  Right,
  Container,
  Content,
  Header,
  Title,
} from 'native-base';
import factCheck from './images/factcheck.png';
import imdb from './images/imdb.png';
import wirecutter from './images/wirecutter.png';
import { Background } from './nativeModules/get';
import AllowPermissionButton from './components/allowPermissionButton';

function App() {
  useNativeModuleEffects();

  return (
    <Container>
      <Header style={{ backgroundColor: '#0c52b4' }}>
        <Left />
        <Body>
          <Title>DisMoi</Title>
        </Body>
        <Right />
      </Header>
      <Content
        padder
        contentContainerStyle={{
          flex: 1,
        }}
      >
        <View style={{ margin: 5 }}>
          <Button
            style={{ margin: 5 }}
            title="Start dismoi in background"
            onPress={() => Background.startService()}
          />
        </View>
        <View style={{ margin: 5 }}>
          <Button
            title="Stop dismoi in background"
            onPress={() => Background.stopService()}
          />
        </View>
        <View style={{ margin: 5 }}>
          <AllowPermissionButton />
        </View>

        {/* <Text style={{ fontSize: 20 }}>Your subscriptions</Text>
        <List
          style={{
            justifyContent: 'center',
          }}
        >
          <ListItem thumbnail>
            <Left>
              <Thumbnail square source={wirecutter} />
            </Left>
            <Body>
              <Text>Wirecutter</Text>
              <Text note numberOfLines={4}>
                Wirecutter strives to be the most trusted product recommendation
                service on the internet.
              </Text>
            </Body>
            <Right>
              <Button transparent>
                <Text>View</Text>
              </Button>
            </Right>
          </ListItem>
          <ListItem thumbnail>
            <Left>
              <Thumbnail square source={imdb} />
            </Left>
            <Body>
              <Text>IMDb</Text>
              <Text note numberOfLines={10}>
                IMDb (an acronym for Internet Movie Database) is an online
                database of information related to films, television programs,
                home videos, video games, and streaming content online –
                including cast, production crew and personal biographies, plot
                summaries, trivia, ratings, and fan and critical reviews
              </Text>
            </Body>
            <Right>
              <Button transparent>
                <Text>View</Text>
              </Button>
            </Right>
          </ListItem>
          <ListItem thumbnail>
            <Left>
              <Thumbnail square source={factCheck} />
            </Left>
            <Body>
              <Text>FactCheck</Text>
              <Text note numberOfLines={5}>
                FactCheck is a nonpartisan, nonprofit “consumer advocate” for
                voters that aims to reduce the level of deception and confusion
                in U.S. politics.
              </Text>
            </Body>
            <Right>
              <Button transparent>
                <Text>View</Text>
              </Button>
            </Right>
          </ListItem>
        </List> */}
      </Content>
    </Container>
  );
}

const styles = {
  centerScreen: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  view: {
    flex: 0.5,
    justifyContent: 'center',
    alignItems: 'center',
  },
  button: {
    backgroundColor: 'gray',
    padding: 10,
    margin: 10,
  },
};

export default App;
