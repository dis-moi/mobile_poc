import { isValidHttpUrl } from '../libraries';
import { FloatingModule } from '../nativeModules/get';
import { DeviceEventEmitter } from 'react-native';
import { Linking } from 'react-native';

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

let matchingContexts = [];

async function callMatchingContext() {
  matchingContexts = await fetch(
    'https://notices.bulles.fr/api/v3/matching-contexts'
  ).then((response) => {
    return response.json();
  });
}

const HeadlessTask = async (taskData) => {
  if (i === 0) {
    callActionListeners();
    await callMatchingContext();
    i++;
  }

  const eventMessageFromChromeURL = taskData.url;

  if (eventMessageFromChromeURL === 'hide') {
    FloatingModule.hideFloatingDisMoiBubble().then(() =>
      console.log('Hide Floating Bubble')
    );
    FloatingModule.hideFloatingDisMoiMessage().then(() =>
      console.log('Hide Floating Bubble')
    );
  }

  if (eventMessageFromChromeURL && isValidHttpUrl(eventMessageFromChromeURL)) {
    const noticeIds = getNoticeIds(matchingContexts, eventMessageFromChromeURL);

    let notices = await Promise.all(
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
        notices = [];
      });
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

export default HeadlessTask;
