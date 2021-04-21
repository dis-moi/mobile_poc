import React from 'react';
import { NativeEventEmitter } from 'react-native';
import { AccessibilityServiceModule } from '../../nativeModules/get';
import { FloatingModule } from '../../nativeModules/get';
import { isValidHttpUrl } from '../../libraries';

function useHideOrShowNoticeEffect(eventToListen) {
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

  let eventListener = React.useRef(null);

  React.useEffect(() => {
    function createListener() {
      const eventEmitter = new NativeEventEmitter(AccessibilityServiceModule);
      eventListener.current = eventEmitter.addListener(
        eventToListen,
        async (eventMessageFromChromeURL) => {
          if (
            eventMessageFromChromeURL &&
            isValidHttpUrl(eventMessageFromChromeURL)
          ) {
            console.log('FETCH MATCHING CONTEXT');
            const matchingContexts = await fetch(
              'https://notices.bulles.fr/api/v3/matching-contexts'
            ).then((response) => response.json());
            console.log('END FETCH MATCHING CONTEXT');

            console.log('GET NOTICE IDS');
            const noticeIds = getNoticeIds(
              matchingContexts,
              eventMessageFromChromeURL
            );
            console.log('END GET NOTICE IDS');

            console.log('MAP NOTICE ID');
            const notices = await Promise.all(
              noticeIds.map((noticeId) =>
                fetch(
                  `https://notices.bulles.fr/api/v3/notices/${noticeId}`
                ).then((response) => response.json())
              )
            );
            console.log('END MAP NOTICE ID');

            if (notices.length > 0) {
              const numberOfNotice = notices.length;

              console.log('show bubble');

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
        }
      );
    }

    createListener();
    return () => {
      eventListener.current.remove();
    };
  }, [eventToListen]);
}

export default useHideOrShowNoticeEffect;
