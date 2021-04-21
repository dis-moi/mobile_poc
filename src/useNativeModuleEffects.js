import { EVENT_FROM_CHROME_URL } from './nativeModules/eventListToListen';

import useHideOrShowNoticeEffect from './useEffectHooks/nativeModules';
import useInitializeEffect from './useEffectHooks/nativeModules/floating/sendMessage/initialize';
import useClickEventFromNoticeEffect from './useEffectHooks/nativeModules/floating/receiveMessage/clickEventFromNotice';
import useRequestPermissionEffect from './useEffectHooks/nativeModules/floating/sendMessage/requestPermission';

export default function useNativeModuleEffects() {
  useInitializeEffect();
  useRequestPermissionEffect();
  useHideOrShowNoticeEffect(EVENT_FROM_CHROME_URL);
  useClickEventFromNoticeEffect();
}
