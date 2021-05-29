import useClickEventFromNoticeEffect from './useEffectHooks/nativeModules/floating/receiveMessage/clickEventFromNotice';
import useRequestPermissionEffect from './useEffectHooks/nativeModules/floating/sendMessage/requestPermission';

export default function useNativeModuleEffects() {
  useRequestPermissionEffect();
  useClickEventFromNoticeEffect();
}
