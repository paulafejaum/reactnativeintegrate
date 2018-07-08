/**
 * This exposes the native ReactNativeIntegrate module as a JS module. 
 * This has the following functions:
 * 
 * function showScreen(String : constant representing the screen to be rendered)
 * function showScreenWithParameters(String : constant representing the screen to be rendered, 
 *                                   Object : Parameters to the screen)
 */
import { NativeModules } from 'react-native';

module.exports = NativeModules.ReactNativeIntegrate;