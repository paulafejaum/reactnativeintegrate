package com.reactnativeintegrate;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReactNativeIntegrate extends ReactContextBaseJavaModule {

  public ReactNativeIntegrate(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  private static Map<String, Class> activitiesMap = new HashMap<>();

  private static Map<String, String> fragmentsMap = new HashMap<>();

  private static Map<String, Object> constants = new HashMap<>();

  private static Class genericActivityClass;

  public static final String PARAM_FRAGMENT_NAME = "param_fragment_name";

  public static final String PARAM_BUNDLE_NAME = "param_bundle_name";


  @Override
  public String getName() {
    return "ReactNativeIntegrate";
  }

  @Override
  public Map<String, Object> getConstants() {
    return constants;
  }

  private static void populateConstants() {

    for (String activity:
         activitiesMap.keySet()) {
      constants.put(activity, activity);
    }
    for (String fragment:
            fragmentsMap.keySet()) {
      constants.put(fragment, fragment);
    }
  }

  public static void setActivitiesMap(Map<String, Class> map) {
    activitiesMap = map;
    populateConstants();
  }

  public static void setFragmentsMap(Map<String, String> map) {
    fragmentsMap = map;
    populateConstants();
  }

  public static void setGenericActivityClass(Class genericClass) {
    genericActivityClass = genericClass;
  }

  @ReactMethod
  public void showScreen(String screenName) {
    showScreenWithParameters(screenName, null);
  }

  @ReactMethod
  public void showScreenWithParameters(String screenName, ReadableMap params) {
    Bundle bundle = new Bundle();
    if (params != null) {
      bundle = convertMapToBundle(params);
    }

    screenName = screenName.toUpperCase();
    Class screenClass = activitiesMap.get(screenName);
    String screenFragment = fragmentsMap.get(screenName);

    if (screenClass != null) {
      startActivity(screenClass, bundle);
    } else if (screenFragment != null && genericActivityClass != null) {
      startFragmentActivity(getReactApplicationContext().getCurrentActivity(), screenFragment, bundle);
    }
  }

  private void startActivity(Class screenClass, Bundle bundle) {
    Intent screenIntent = new Intent(getReactApplicationContext(), screenClass);
    screenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    screenIntent.putExtra(PARAM_BUNDLE_NAME, bundle);
    getReactApplicationContext().startActivity(screenIntent);
  }

  private void startFragmentActivity(Activity currentActivity, String fragmentName, Bundle bundle) {
    Intent intent = new Intent(currentActivity, genericActivityClass);
    intent.putExtra(PARAM_FRAGMENT_NAME, fragmentName);
    intent.putExtra(PARAM_BUNDLE_NAME, bundle);
    currentActivity.startActivity(intent);
  }

  public static Bundle convertMapToBundle(ReadableMap readableMap) {
    Bundle bundle = new Bundle();
    ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      switch (readableMap.getType(key)) {
        case Null:
          break;
        case Boolean:
          bundle.putBoolean(key, readableMap.getBoolean(key));
          break;
        case Number:
          bundle.putDouble(key, readableMap.getDouble(key));
          break;
        case String:
          bundle.putString(key, readableMap.getString(key));
          break;
      }
    }
    return bundle;
  }

}
