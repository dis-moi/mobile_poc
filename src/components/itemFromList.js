import React from 'react';
import { Card } from 'native-base';

function ItemFromList({ children, borderRadius }) {
  return <Card style={{ borderRadius }}>{children}</Card>;
}

export default ItemFromList;
