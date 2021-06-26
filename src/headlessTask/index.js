import { FloatingModule } from '../nativeModules/get';
import { DeviceEventEmitter } from 'react-native';
import { Linking } from 'react-native';
import moment from 'moment';

import 'moment/min/locales';

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

let matchingContexts = [];

function callActionListeners() {
  DeviceEventEmitter.addListener('floating-dismoi-bubble-press', (e) => {
    return FloatingModule.showFloatingDisMoiMessage(10, 1500).then(() => {
      // What to do when user press on the bubble
      console.log('Bubble press');
    });
  });
  DeviceEventEmitter.addListener('floating-dismoi-message-press', (e) => {
    // What to do when user press on the message
    return FloatingModule.hideFloatingDisMoiMessage().then(() => {});
  });

  DeviceEventEmitter.addListener('floating-dismoi-bubble-remove', (e) => {
    // What to do when user press on the message
    console.log('FLOATING DISMOI BUBBLE REMOVE');
  });

  DeviceEventEmitter.addListener('URL_CLICK_LINK', (event) => {
    FloatingModule.hideFloatingDisMoiBubble().then(() =>
      FloatingModule.hideFloatingDisMoiMessage()
    );
    Linking.openURL(event);
  });
}

async function callMatchingContext() {
  matchingContexts = await fetch(
    'https://notices.bulles.fr/api/v3/matching-contexts'
  ).then((response) => {
    return response.json();
  });
}

const HeadlessTask = async (taskData) => {
  if (matchingContexts.length === 0) {
    callActionListeners();
    await callMatchingContext();
  }

  if (taskData.hide === 'true') {
    FloatingModule.hideFloatingDisMoiBubble().then(() =>
      FloatingModule.hideFloatingDisMoiMessage()
    );

    return;
  }

  const eventMessageFromChromeURL = taskData.url;

  if (eventMessageFromChromeURL) {
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

      const noticesToShow = notices.map((res) => {
        const capitalize = (s) => {
          if (typeof s !== 'string') return '';
          return s.charAt(0).toUpperCase() + s.slice(1);
        };

        var month = moment(res.modified, 'YYYY-MM-DD')
          .locale('fr')
          .format('MMMM');
        var day = moment(res.modified, 'YYYY-MM-DD').format('D');

        var year = moment(res.modified, 'YYYY-MM-DD').format('Y');

        const formattedDate = `${moment(res.modified, 'YYYY-MM-DD')
          .locale('fr')
          .format('dddd')} ${day} ${month} ${year}`;

        res.modified = capitalize(formattedDate);

        return res;
      });

      FloatingModule.showFloatingDisMoiBubble(
        10,
        1500,
        numberOfNotice,
        noticesToShow,
        eventMessageFromChromeURL
      ).then(() => {
        notices = [];
      });
    }
  }
};

export default HeadlessTask;
