import React from 'react';
import { Container, Content } from 'native-base';
import { StatusBar } from 'react-native';

export default function Screen({ children }) {
  return (
    <Container>
      <StatusBar animated={true} backgroundColor="#2855a2" />
      <Content
        padder
        contentContainerStyle={{
          flex: 1,
          margin: 15,
        }}
      >
        {children}
      </Content>
    </Container>
  );
}
