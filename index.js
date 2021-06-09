import { AppRegistry } from 'react-native';
import App from './src/';
import { name as appName } from './app.json';
import { isValidHttpUrl } from './src/libraries';
import { FloatingModule } from './src/nativeModules/get';
import { DeviceEventEmitter } from 'react-native';
import { Linking } from 'react-native';

let previousURL = '';

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

let i = 0;

const HeadlessTask = async (taskData) => {
  console.log('headless task');

  function callActionListeners() {
    DeviceEventEmitter.addListener('floating-dismoi-bubble-press', (e) => {
      return FloatingModule.showFloatingDisMoiMessage(10, 1500).then(() => {
        // What to do when user press on the bubble
        console.log('Bubble press');
      });
    });
    DeviceEventEmitter.addListener('floating-dismoi-message-press', (e) => {
      // What to do when user press on the message
      return FloatingModule.hideFloatingDisMoiMessage().then(() =>
        console.log('Hide Floating DisMoiMessage')
      );
    });
    DeviceEventEmitter.addListener('URL_CLICK_LINK', (event) => {
      Linking.openURL(event);
    });
  }

  if (i === 0) {
    callActionListeners();
    i++;
  }

  const eventMessageFromChromeURL = taskData.url;

  console.log(eventMessageFromChromeURL);

  if (eventMessageFromChromeURL === 'hide') {
    FloatingModule.hideFloatingDisMoiBubble().then(() =>
      console.log('Hide Floating Bubble')
    );
    FloatingModule.hideFloatingDisMoiMessage().then(() =>
      console.log('Hide Floating Bubble')
    );
  }

  if (eventMessageFromChromeURL && isValidHttpUrl(eventMessageFromChromeURL)) {
    // if (previousURL === eventMessageFromChromeURL) {
    //   console.log('INSIDE');
    //   return;
    // }
    // previousURL = eventMessageFromChromeURL;
    const matchingContexts = await fetch(
      'https://notices.bulles.fr/api/v3/matching-contexts'
    ).then((response) => {
      return response.json();
    });

    const noticeIds = getNoticeIds(matchingContexts, eventMessageFromChromeURL);

    // console.log(noticeIds);
    console.log('is valid');
    console.log(eventMessageFromChromeURL);
    console.log('previous url');
    console.log(previousURL);

    // if (eventMessageFromChromeURL === 'amazon.com/dp/B07PYLT6DN') {
    //   notices = await Promise.all(
    //     [1].map(() =>
    //       fetch(
    //         `https://notices.bulles.fr/api/v3/notices/1902`
    //       ).then((response) => response.json())
    //     )
    //   );
    // }

    // if (
    //   eventMessageFromChromeURL ===
    //   'childrenshealthdefense.org/defender/scientists-challenge-health-officials-on-vaccinating-covid/'
    // ) {
    //   notices = await Promise.all(
    //     [1].map(() =>
    //       fetch(
    //         `https://notices.bulles.fr/api/v3/notices/1904`
    //       ).then((response) => response.json())
    //     )
    //   );
    // }

    // console.log(eventMessageFromChromeURL);

    let notices = await Promise.all(
      noticeIds.map((noticeId) =>
        fetch(
          `https://notices.bulles.fr/api/v3/notices/${noticeId}`
        ).then((response) => response.json())
      )
    );

    console.log(notices);

    if (notices.length > 0) {
      const numberOfNotice = notices.length;

      //   console.log(notices.map((res) => res.contributor.name));

      //   // const filteredNotices = notices.filter(
      //   //   (res) =>
      //   //     res.contributor.name === 'Reviewed by Wirecutter' ||
      //   //     res.contributor.name === 'FactCheck.org'
      //   // );

      //   // notices = [];

      FloatingModule.showFloatingDisMoiBubble(
        10,
        1500,
        numberOfNotice,
        notices,
        eventMessageFromChromeURL
      ).then(() => {
        console.log('show floating dis moi bubble');
        notices = [];
      });
    }
    // if (notices.length === 0) {
    //   FloatingModule.hideFloatingDisMoiBubble().then(() =>
    //     console.log('Hide Floating Bubble')
    //   );
    //   FloatingModule.hideFloatingDisMoiMessage().then(() => {
    //     console.log('Hide Floating Bubble');
    //     FloatingModule.hideFloatingDisMoiBubble().then(() =>
    //       console.log('Hide Floating Bubble')
    //     );
    //   });
    // }
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
