import { AppRegistry } from 'react-native';
import App from './src/';
import { name as appName } from './app.json';
import HeadlessTask from './src/headlessTask';

AppRegistry.registerHeadlessTask('Background', () => HeadlessTask);
AppRegistry.registerComponent(appName, () => App);
