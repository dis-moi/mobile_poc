import React from 'react';
import { Text, FlatList, View, TouchableOpacity } from 'react-native';
import { Body, Left, Card, Thumbnail, CardItem } from 'native-base';
import { Background } from '../nativeModules/get';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Title from '../components/title';
import Button from '../components/button';
import RadioForm from 'react-native-simple-radio-button';
import SharedPreferences from 'react-native-shared-preferences';
import Paragraph from '../components/paragraph';
import { Linking } from 'react-native';

function Finished() {
  const [contributors, setContributors] = React.useState([]);
  const [filteredContributors, setFilteredContributors] = React.useState([]);

  const [itemIds, setItemIds] = React.useState([]);

  const [buttonValue, setButtonValue] = React.useState('ALL');

  let radioProps = [
    { label: 'Tous', value: 'ALL' },
    { label: 'Conso', value: 'CONSO' },
    { label: 'Société', value: 'CULTURE' },
    { label: 'Militant', value: 'MILITANT' },
    { label: 'Divers', value: 'DIVERS' },
  ];

  function sortNumberOfPublishedNoticeFromHighestToLowest(contributors1) {
    return contributors1.sort(function (a, b) {
      return (
        b.contribution.numberOfPublishedNotices -
        a.contribution.numberOfPublishedNotices
      );
    });
  }

  React.useEffect(() => {
    function startDisMoiAppInBackground() {
      Background.startService();
    }
    startDisMoiAppInBackground();
  }, []);

  React.useEffect(() => {
    AsyncStorage.getItem('alreadyLaunched').then((value) => {
      if (value == null) {
        AsyncStorage.setItem('alreadyLaunched', 'true'); // No need to wait for `setItem` to finish, although you might want to handle errors
      }
    });
  }, []);

  React.useEffect(() => {
    console.log('get contributors');

    async function getContributors() {
      SharedPreferences.getAll(async function (values) {
        const ids = [...new Set(values.map((res) => res[1]))];

        const contributors1 = await fetch(
          'https://notices.bulles.fr/api/v3/contributors'
        ).then((response) => {
          return response.json();
        });
        const contributorsSorted = sortNumberOfPublishedNoticeFromHighestToLowest(
          contributors1.filter(
            (contributor) => !ids.includes(String(contributor.id))
          )
        );
        const contributorsFollowedSorted = sortNumberOfPublishedNoticeFromHighestToLowest(
          contributors1.filter((contributor) =>
            ids.includes(String(contributor.id))
          )
        );
        setContributors([...contributorsFollowedSorted, ...contributorsSorted]);
        setItemIds(ids);
      });
    }
    getContributors();
  }, [buttonValue]);

  React.useEffect(() => {
    if (buttonValue !== 'ALL') {
      const filteredContributorsByCategories = contributors.filter((res) =>
        res.categories.includes(buttonValue)
      );

      const contributorsSorted = sortNumberOfPublishedNoticeFromHighestToLowest(
        filteredContributorsByCategories.filter(
          (contributor) => !itemIds.includes(String(contributor.id))
        )
      );
      const contributorsFollowedSorted = sortNumberOfPublishedNoticeFromHighestToLowest(
        filteredContributorsByCategories.filter((contributor) =>
          itemIds.includes(String(contributor.id))
        )
      );

      setFilteredContributors([
        ...contributorsFollowedSorted,
        ...contributorsSorted,
      ]);
    }
  }, [buttonValue, contributors, itemIds]);

  function renderItem({ item }) {
    if (item.name && item?.avatar?.normal?.url) {
      return (
        <Card style={{ borderRadius: 15 }}>
          <CardItem
            style={{ borderTopLeftRadius: 15, borderTopRightRadius: 15 }}
          >
            <Left>
              <Thumbnail
                style={{ width: 80, height: 80, borderRadius: 80 / 2 }}
                source={{ uri: item?.avatar?.normal.url }}
              />
              <Body>
                <Title left fontSize={20}>
                  {item.name}
                </Title>
                <View style={{ marginTop: 12 }}>
                  {itemIds.includes(String(item.id)) ? (
                    <Button
                      small
                      text={'Abonné(e)'}
                      icon
                      border
                      backgroundColor={'white'}
                      color={
                        itemIds.includes(String(item.id)) ? 'black' : 'white'
                      }
                      onPress={() => {
                        setItemIds(
                          itemIds.filter((id) => id !== String(item.id))
                        );
                        SharedPreferences.removeItem(item.name);
                      }}
                    />
                  ) : (
                    <Button
                      small
                      text={'Suivre'}
                      onPress={() => {
                        setItemIds([...itemIds, String(item.id)]);
                        SharedPreferences.setItem(item.name, String(item.id));
                      }}
                    />
                  )}
                </View>
              </Body>
            </Left>
          </CardItem>
          <CardItem cardBody>
            <Paragraph>{item.title}</Paragraph>
          </CardItem>
          <CardItem
            style={{ borderBottomLeftRadius: 15, borderBottomRightRadius: 15 }}
          >
            <TouchableOpacity
              onPress={() => {
                Linking.openURL(item.contribution.example.exampleMatchingUrl);
              }}
            >
              <Text
                style={{
                  color: '#1e52b4',
                  fontSize: 17,
                  fontFamily: 'Helvetica',
                  letterSpacing: 0.3,
                }}
              >
                Voir un exemple réel >
              </Text>
            </TouchableOpacity>
          </CardItem>
        </Card>
      );
    }
  }

  return (
    <View style={{ backgroundColor: '#e9eaef' }}>
      <View
        style={{
          height: 50,
          justifyContent: 'center',
        }}
      >
        <Title>Choix des contributeurs</Title>
      </View>
      <RadioForm
        labelStyle={{ textAlign: 'center' }}
        style={{
          flexDirection: 'row',
          justifyContent: 'space-between',
          alignItems: 'center',
          alignContent: 'center',
          padding: 10,
          borderRadius: 15,
          backgroundColor: 'white',
        }}
        radio_props={radioProps}
        formHorizontal={true}
        labelHorizontal={false}
        buttonColor={'#2855a2'}
        animation={true}
        initial={0}
        onPress={(value) => {
          setButtonValue(value);
        }}
      />
      <FlatList
        style={{ marginTop: 15 }}
        data={
          buttonValue === 'ALL'
            ? contributors
            : filteredContributors.sort((res) => res.contributions)
        }
        renderItem={renderItem}
        keyExtractor={(item) => String(item.id)}
      />
    </View>
  );
}

export default Finished;
