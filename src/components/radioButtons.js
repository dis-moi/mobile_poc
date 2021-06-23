import React from 'react';
import RadioForm from 'react-native-simple-radio-button';

function RadioButtons({ onPress }) {
  let radioProps = [
    { label: 'Tous', value: 'ALL' },
    { label: 'Conso', value: 'CONSO' },
    { label: 'Société', value: 'CULTURE' },
    { label: 'Militant', value: 'MILITANT' },
    { label: 'Divers', value: 'DIVERS' },
  ];

  return (
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
      onPress={onPress}
    />
  );
}

export default RadioButtons;
