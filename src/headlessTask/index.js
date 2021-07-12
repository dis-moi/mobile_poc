import { Background, FloatingModule } from '../nativeModules/get';
import { DeviceEventEmitter } from 'react-native';
import { Linking } from 'react-native';

import { formatDate } from '../libraries';

import SharedPreferences from 'react-native-shared-preferences';

import 'moment/min/locales';

let matchingContexts = [];
let HTML = '';
let noticeIds = [];

async function getNoticeIds(eventMessageFromChromeURL) {
  for (const matchingContext of matchingContexts) {
    const addWWWForBuildingURL = `www.${eventMessageFromChromeURL}`;

    if (addWWWForBuildingURL.match(new RegExp(matchingContext.urlRegex, 'g'))) {
      if (matchingContext.xpath) {
        const result = await Background.testWithXpath(
          HTML,
          matchingContext.xpath
        );

        if (result === 'true') {
          noticeIds.push(matchingContext.noticeId);
        }
        continue;
      }
      noticeIds.push(matchingContext.noticeId);
    }
  }
}

function callActionListeners() {
  DeviceEventEmitter.addListener('floating-dismoi-bubble-press', (e) => {
    return FloatingModule.showFloatingDisMoiMessage(0, 1500).then(() => {
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
    FloatingModule.initialize().then(() => {
      FloatingModule.hideFloatingDisMoiBubble().then(() =>
        FloatingModule.hideFloatingDisMoiMessage()
      );
      Linking.openURL(event);
    });
  });
}

let url = '';

let matchingContextFetchApi =
  'https://notices.bulles.fr/api/v3/matching-contexts?';

async function callMatchingContext(savedUrlMatchingContext) {
  console.log('_________________CALL MATHING CONTEXT____________________');

  matchingContexts = await fetch(
    matchingContextFetchApi + savedUrlMatchingContext
  ).then((response) => {
    console.log(
      '_________________END CALL MATHING CONTEXT____________________'
    );
    return response.json();
  });
}

async function getHTMLOfCurrentChromeURL(eventMessageFromChromeURL) {
  HTML = await fetch(`http://www.${eventMessageFromChromeURL}`).then(function (
    response
  ) {
    // The API call was successful!
    return response.text();
  });
}

const HeadlessTask = async (taskData) => {
  SharedPreferences.getItem('url', async function (savedUrlMatchingContext) {
    callActionListeners();
    FloatingModule.initialize();
    await Promise.all([
      await callMatchingContext(savedUrlMatchingContext),
      await getHTMLOfCurrentChromeURL(taskData.url),
    ]);

    if (taskData.hide === 'true') {
      FloatingModule.hideFloatingDisMoiBubble().then(() =>
        FloatingModule.hideFloatingDisMoiMessage()
      );
      return;
    }
    const eventMessageFromChromeURL = taskData.url;

    if (eventMessageFromChromeURL) {
      if (
        eventMessageFromChromeURL.indexOf(' ') >= 0 ||
        taskData.eventType === 'TYPE_VIEW_TEXT_CHANGED'
      ) {
        FloatingModule.hideFloatingDisMoiBubble().then(() =>
          FloatingModule.hideFloatingDisMoiMessage()
        );
        return;
      }

      if (taskData.eventText === '') {
        await getNoticeIds(eventMessageFromChromeURL);

        let uniqueIds = [...new Set(noticeIds)];

        let notices = await Promise.all(
          uniqueIds.map((noticeId) =>
            fetch(
              `https://notices.bulles.fr/api/v3/notices/${noticeId}`
            ).then((response) => response.json())
          )
        );

        if (notices.length > 0) {
          const noticesToShow = notices.map((res) => {
            const formattedDate = formatDate(res);
            res.modified = formattedDate;

            return res;
          });

          FloatingModule.showFloatingDisMoiBubble(
            10,
            1500,
            notices.length,
            noticesToShow,
            eventMessageFromChromeURL
          ).then(() => {
            noticeIds = [];
          });
        }
      }
    }
  });
};

export default HeadlessTask;
