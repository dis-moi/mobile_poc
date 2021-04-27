import { AppRegistry } from 'react-native';
import App from './src/';
import { name as appName } from './app.json';
import { isValidHttpUrl } from './src/libraries';
import { FloatingModule } from './src/nativeModules/get';

function getNoticeIds(matchingContexts, eventMessageFromChromeURL) {
  return matchingContexts
    .map((res) => {
      const addWWWForBuildingURL = `www.${eventMessageFromChromeURL}`;

      if (addWWWForBuildingURL.match(new RegExp(res.urlRegex, 'g'))) {
        return res.noticeId;
      }
    })
    .filter(Boolean);
}

const HeadlessTask = async (taskData) => {
  const eventMessageFromChromeURL = taskData.url;
  if (eventMessageFromChromeURL && isValidHttpUrl(eventMessageFromChromeURL)) {
    const matchingContexts = await fetch(
      'https://notices.bulles.fr/api/v3/matching-contexts'
    ).then((response) => {
      return response.json();
    });
    const noticeIds = getNoticeIds(matchingContexts, eventMessageFromChromeURL);
    const notices = await Promise.all(
      noticeIds.map((noticeId) =>
        fetch(
          `https://notices.bulles.fr/api/v3/notices/${noticeId}`
        ).then((response) => response.json())
      )
    );

    if (notices.length > 0) {
      const numberOfNotice = notices.length;

      FloatingModule.showFloatingDisMoiBubble(
        10,
        1500,
        numberOfNotice,
        notices,
        eventMessageFromChromeURL
      ).then(() => {
        console.log('show floating dis moi bubble');
      });
    }
    if (notices.length === 0) {
      FloatingModule.hideFloatingDisMoiBubble().then(() =>
        console.log('Hide Floating Bubble')
      );
      FloatingModule.hideFloatingDisMoiMessage().then(() =>
        console.log('Hide Floating Bubble')
      );
    }
  }
  if (isValidHttpUrl(eventMessageFromChromeURL) === false) {
    FloatingModule.hideFloatingDisMoiBubble().then(() =>
      console.log('Hide Floating Bubble')
    );
    FloatingModule.hideFloatingDisMoiMessage().then(() =>
      console.log('Hide Floating Bubble')
    );
  }
};

AppRegistry.registerHeadlessTask('Background', () => HeadlessTask);
AppRegistry.registerComponent(appName, () => App);
