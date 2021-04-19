import { EVENT_FROM_CHROME_URL } from './nativeModules/eventListToListen';

import useHideOrShowNoticeEffect from './useEffectHooks/nativeModules';
import useInitializeEffect from './useEffectHooks/nativeModules/floating/sendMessage/initialize';
import useClickEventFromNoticeEffect from './useEffectHooks/nativeModules/floating/receiveMessage/clickEventFromNotice';

export default function useNativeModuleEffects() {
  useInitializeEffect();
  useHideOrShowNoticeEffect(EVENT_FROM_CHROME_URL);
  useClickEventFromNoticeEffect();
}
