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
            const matchingContexts = await fetch(
              'https://notices.bulles.fr/api/v3/matching-contexts'
            ).then((response) => response.json());

            const noticeIds = getNoticeIds(
              matchingContexts,
              eventMessageFromChromeURL
            );

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
