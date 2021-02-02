import React from 'react';
import {Text, View} from 'react-native';

function App() {
  return (
    <View style={styles.centerScreen}>
      <Text>Welcome to react native dismoi POC!</Text>
    </View>
  );
}

const styles = {
  centerScreen: {flex: 1, justifyContent: 'center', alignItems: 'center'},
};

export default App;
