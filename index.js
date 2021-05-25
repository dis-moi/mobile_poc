import { AppRegistry } from 'react-native';
import App from './src/';
import { name as appName } from './app.json';
import { isValidHttpUrl } from './src/libraries';
import { FloatingModule } from './src/nativeModules/get';

function getNoticeIds(matchingContexts, eventMessageFromChromeURL) {
  return matchingContexts
    .map((res) => {
      const addWWWForBuildingURL = `www.${eventMessageFromChromeURL}`;

      // console.log(eventMessageFromChromeURL);

      if (addWWWForBuildingURL.match(new RegExp(res.urlRegex, 'g'))) {
        // console.log(res.urlRegex);

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

    let notices = [];

    console.log('notice ids');
    console.log(noticeIds);

    console.log(eventMessageFromChromeURL);

    if (eventMessageFromChromeURL === 'amazon.com/dp/B07PYLT6DN') {
      notices = await Promise.all(
        [1].map(() =>
          fetch(
            `https://notices.bulles.fr/api/v3/notices/1902`
          ).then((response) => response.json())
        )
      );
    }

    if (
      eventMessageFromChromeURL ===
      'childrenshealthdefense.org/defender/scientists-challenge-health-officials-on-vaccinating-covid/'
    ) {
      console.log(noticeIds);
      notices = await Promise.all(
        [1].map(() =>
          fetch(
            `https://notices.bulles.fr/api/v3/notices/1904`
          ).then((response) => response.json())
        )
      );
    }

    // console.log(eventMessageFromChromeURL);

    // let notices = await Promise.all(
    //   noticeIds.map((noticeId) =>
    //     fetch(
    //       `https://notices.bulles.fr/api/v3/notices/${noticeId}`
    //     ).then((response) => response.json())
    //   )
    // );

    console.log(notices);

    if (notices.length > 0) {
      const numberOfNotice = notices.length;

      //   console.log('notices');
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
