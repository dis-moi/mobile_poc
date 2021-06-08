import React from 'react';
import { Text, View, TouchableOpacity, Dimensions } from 'react-native';
import { Container, Content } from 'native-base';

function Tuto1({ navigation }) {
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
              letterSpacing: 0.9,
              textAlign: 'center',
              fontFamily: 'Helvetica',
              color: '#000000',
              fontSize: 18,
            }}
          >
            Conso, culture, fact-checking, militant.. Les éclaireurs Dismoi vous
            conseillent et informent lorsque vous naviguez sur le web.
          </Text>
          <View
            style={{
              flexDirection: 'row',
              justifyContent: 'space-around',
              flexWrap: 'wrap',
            }}
          >
            <View
              style={{
                margin: 5,
                borderRadius:
                  Math.round(
                    Dimensions.get('window').width +
                      Dimensions.get('window').height
                  ) / 4,
                width: Dimensions.get('window').width * 0.25,
                height: Dimensions.get('window').width * 0.25,
                backgroundColor: 'blue',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <Text style={{ color: 'white', textAlign: 'center' }}>
                Selon Le Monde
              </Text>
            </View>
            <View
              style={{
                margin: 5,
                borderRadius:
                  Math.round(
                    Dimensions.get('window').width +
                      Dimensions.get('window').height
                  ) / 4,
                width: Dimensions.get('window').width * 0.25,
                height: Dimensions.get('window').width * 0.25,
                backgroundColor: 'blue',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <Text style={{ color: 'white', textAlign: 'center' }}>
                Amazon Antidote
              </Text>
            </View>
            <View
              style={{
                margin: 5,
                borderRadius:
                  Math.round(
                    Dimensions.get('window').width +
                      Dimensions.get('window').height
                  ) / 4,
                width: Dimensions.get('window').width * 0.25,
                height: Dimensions.get('window').width * 0.25,
                backgroundColor: 'blue',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <Text style={{ color: 'white', textAlign: 'center' }}>LMEM</Text>
            </View>
            <View
              style={{
                margin: 5,
                borderRadius:
                  Math.round(
                    Dimensions.get('window').width +
                      Dimensions.get('window').height
                  ) / 4,
                width: Dimensions.get('window').width * 0.25,
                height: Dimensions.get('window').width * 0.25,
                backgroundColor: 'blue',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <Text style={{ color: 'white', textAlign: 'center', padding: 5 }}>
                CaptainFact.io
              </Text>
            </View>
            <View
              style={{
                margin: 5,
                borderRadius:
                  Math.round(
                    Dimensions.get('window').width +
                      Dimensions.get('window').height
                  ) / 4,
                width: Dimensions.get('window').width * 0.25,
                height: Dimensions.get('window').width * 0.25,
                backgroundColor: 'blue',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <Text style={{ color: 'white', textAlign: 'center', padding: 5 }}>
                Arnaque Blocker
              </Text>
            </View>
            <View
              style={{
                margin: 5,
                borderRadius:
                  Math.round(
                    Dimensions.get('window').width +
                      Dimensions.get('window').height
                  ) / 4,
                width: Dimensions.get('window').width * 0.25,
                height: Dimensions.get('window').width * 0.25,
                backgroundColor: 'blue',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <Text style={{ color: 'white', textAlign: 'center', padding: 5 }}>
                Colibri écolo
              </Text>
            </View>
            <View
              style={{
                margin: 5,
                borderRadius:
                  Math.round(
                    Dimensions.get('window').width +
                      Dimensions.get('window').height
                  ) / 4,
                width: Dimensions.get('window').width * 0.25,
                height: Dimensions.get('window').width * 0.25,
                backgroundColor: 'blue',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <Text style={{ color: 'white', textAlign: 'center', padding: 5 }}>
                Selon Que Choisir
              </Text>
            </View>
          </View>
          <Text
            style={{
              letterSpacing: 0.9,
              textAlign: 'center',
              fontFamily: 'Helvetica',
              color: '#000000',
              fontSize: 18,
            }}
          >
            Et des dizaines d'autres...
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
              backgroundColor: '#2855a2',
              borderRadius: 10,
              justifyContent: 'center',
            }}
            onPress={() => {
              return navigation.navigate('Tuto2');
            }}
          >
            <Text style={{ textAlign: 'center', color: 'white', fontSize: 28 }}>
              Suivant
            </Text>
          </TouchableOpacity>
        </View>
      </Content>
    </Container>
    // <Container>
    //   <Content
    //     padder
    //     contentContainerStyle={{
    //       flex: 1,
    //     }}
    //   >
    //     <View
    //       style={{
    //         flex: 1,
    //         flexDirection: 'column',
    //         justifyContent: 'space-between',
    //       }}
    //     >
    //       <Text style={{ textAlign: 'center' }}>
    //         Conso, culture, fact-checking, militant.. Les éclaireurs Dismoi vous
    //         conseillent et informent lorsque vous naviguez sur le web {'\n'}
    //       </Text>
    //       <View
    //         style={{
    //           flexDirection: 'row',
    //           justifyContent: 'space-around',
    //           flexWrap: 'wrap',
    //         }}
    //       >
    //         <View
    //           style={{
    //             margin: 5,
    //             borderRadius:
    //               Math.round(
    //                 Dimensions.get('window').width +
    //                   Dimensions.get('window').height
    //               ) / 4,
    //             width: Dimensions.get('window').width * 0.25,
    //             height: Dimensions.get('window').width * 0.25,
    //             backgroundColor: 'blue',
    //             justifyContent: 'center',
    //             alignItems: 'center',
    //           }}
    //         >
    //           <Text style={{ color: 'white', textAlign: 'center' }}>
    //             Selon Le Monde
    //           </Text>
    //         </View>
    //         <View
    //           style={{
    //             margin: 5,
    //             borderRadius:
    //               Math.round(
    //                 Dimensions.get('window').width +
    //                   Dimensions.get('window').height
    //               ) / 4,
    //             width: Dimensions.get('window').width * 0.25,
    //             height: Dimensions.get('window').width * 0.25,
    //             backgroundColor: 'blue',
    //             justifyContent: 'center',
    //             alignItems: 'center',
    //           }}
    //         >
    //           <Text style={{ color: 'white', textAlign: 'center' }}>
    //             Amazon Antidote
    //           </Text>
    //         </View>
    //         <View
    //           style={{
    //             margin: 5,
    //             borderRadius:
    //               Math.round(
    //                 Dimensions.get('window').width +
    //                   Dimensions.get('window').height
    //               ) / 4,
    //             width: Dimensions.get('window').width * 0.25,
    //             height: Dimensions.get('window').width * 0.25,
    //             backgroundColor: 'blue',
    //             justifyContent: 'center',
    //             alignItems: 'center',
    //           }}
    //         >
    //           <Text style={{ color: 'white', textAlign: 'center' }}>LMEM</Text>
    //         </View>
    //         <View
    //           style={{
    //             margin: 5,
    //             borderRadius:
    //               Math.round(
    //                 Dimensions.get('window').width +
    //                   Dimensions.get('window').height
    //               ) / 4,
    //             width: Dimensions.get('window').width * 0.25,
    //             height: Dimensions.get('window').width * 0.25,
    //             backgroundColor: 'blue',
    //             justifyContent: 'center',
    //             alignItems: 'center',
    //           }}
    //         >
    //           <Text style={{ color: 'white', textAlign: 'center', padding: 5 }}>
    //             CaptainFact.io
    //           </Text>
    //         </View>
    //         <View
    //           style={{
    //             margin: 5,
    //             borderRadius:
    //               Math.round(
    //                 Dimensions.get('window').width +
    //                   Dimensions.get('window').height
    //               ) / 4,
    //             width: Dimensions.get('window').width * 0.25,
    //             height: Dimensions.get('window').width * 0.25,
    //             backgroundColor: 'blue',
    //             justifyContent: 'center',
    //             alignItems: 'center',
    //           }}
    //         >
    //           <Text style={{ color: 'white', textAlign: 'center', padding: 5 }}>
    //             Arnaque Blocker
    //           </Text>
    //         </View>
    //         <View
    //           style={{
    //             margin: 5,
    //             borderRadius:
    //               Math.round(
    //                 Dimensions.get('window').width +
    //                   Dimensions.get('window').height
    //               ) / 4,
    //             width: Dimensions.get('window').width * 0.25,
    //             height: Dimensions.get('window').width * 0.25,
    //             backgroundColor: 'blue',
    //             justifyContent: 'center',
    //             alignItems: 'center',
    //           }}
    //         >
    //           <Text style={{ color: 'white', textAlign: 'center', padding: 5 }}>
    //             Colibri écolo
    //           </Text>
    //         </View>
    //         <View
    //           style={{
    //             margin: 5,
    //             borderRadius:
    //               Math.round(
    //                 Dimensions.get('window').width +
    //                   Dimensions.get('window').height
    //               ) / 4,
    //             width: Dimensions.get('window').width * 0.25,
    //             height: Dimensions.get('window').width * 0.25,
    //             backgroundColor: 'blue',
    //             justifyContent: 'center',
    //             alignItems: 'center',
    //           }}
    //         >
    //           <Text style={{ color: 'white', textAlign: 'center', padding: 5 }}>
    //             Selon Que Choisir
    //           </Text>
    //         </View>
    //       </View>
    //       <Text style={{ textAlign: 'center' }}>
    //         Et des dizaines d'autres...
    //       </Text>
    //       <View
    //         style={{
    //           flexDirection: 'column',
    //           justifyContent: 'flex-end',
    //         }}
    //       >
    //         <Button
    //           title="Suivant"
    //           onPress={() => {
    //             return navigation.navigate('Tuto2');
    //           }}
    //         />
    //       </View>
    //     </View>
    //   </Content>
    // </Container>
  );
}

export default Tuto1;
