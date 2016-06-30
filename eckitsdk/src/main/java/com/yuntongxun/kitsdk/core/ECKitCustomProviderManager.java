package com.yuntongxun.kitsdk.core;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.yuntongxun.kitsdk.custom.provider.IBaseProvider;
import com.yuntongxun.kitsdk.custom.provider.chat.ECCustomChatActionProvider;
import com.yuntongxun.kitsdk.custom.provider.chat.ECCustomChatPlusExtendProvider;
import com.yuntongxun.kitsdk.custom.provider.chat.ECCustomChatUIProvider;
import com.yuntongxun.kitsdk.custom.provider.conversation.ECCustomConversationListActionProvider;
import com.yuntongxun.kitsdk.custom.provider.conversation.ECCustomConversationListUIProvider;

public class ECKitCustomProviderManager

{
	private static Map<String, String> providerMap = new HashMap<String ,String>();

	public static void addCustomProvider(ECCustomProviderEnum customEnum,
			Class<? extends IBaseProvider> baseProvider) {
		providerMap.put(customEnum.getName(), baseProvider.getName());
	}

	public static String getCustomProviderName(
			ECCustomProviderEnum customProvider) {
		return (String) providerMap.get(customProvider.getName());
	}
	
	public static void release(){
		
		if(providerMap!=null){
			providerMap.clear();
			
		}
		
	}

	public static ECCustomConversationListActionProvider getCustomConversationAction() {

		ECCustomConversationListActionProvider instance = null;
		String name = ECKitCustomProviderManager
				.getCustomProviderName(ECCustomProviderEnum.CONVERSATION_PROVIDER);

		if (!TextUtils.isEmpty(name)) {
			try {
				Class clazz = Class.forName(name);
				Object obj = clazz.newInstance();

				if (obj instanceof ECCustomConversationListActionProvider) {

					instance = (ECCustomConversationListActionProvider) obj;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return instance;

	}
	
	public static ECCustomChatActionProvider getCustomChatActionProvider() {

		ECCustomChatActionProvider instance = null;
		String name = ECKitCustomProviderManager
				.getCustomProviderName(ECCustomProviderEnum.CHATTING_PROVIDER);

		if (!TextUtils.isEmpty(name)) {
			try {
				Class clazz = Class.forName(name);
				Object obj = clazz.newInstance();

				if (obj instanceof ECCustomChatActionProvider) {

					instance = (ECCustomChatActionProvider) obj;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return instance;

	}
	
	
	public static ECCustomConversationListUIProvider getCustomConversationListUIProvider() {

		ECCustomConversationListUIProvider instance = null;
		String name = ECKitCustomProviderManager
				.getCustomProviderName(ECCustomProviderEnum.CONVERSATION_PROVIDER);

		if (!TextUtils.isEmpty(name)) {
			try {
				Class clazz = Class.forName(name);
				Object obj = clazz.newInstance();

				if (obj instanceof ECCustomConversationListUIProvider) {

					instance = (ECCustomConversationListUIProvider) obj;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return instance;

	}
	
	
	public static ECCustomChatUIProvider getCustomChatUIProvider() {

		ECCustomChatUIProvider instance = null;
		String name = ECKitCustomProviderManager
				.getCustomProviderName(ECCustomProviderEnum.CHATTING_PROVIDER);

		if (!TextUtils.isEmpty(name)) {
			try {
				Class clazz = Class.forName(name);
				Object obj = clazz.newInstance();

				if (obj instanceof ECCustomChatUIProvider) {

					instance = (ECCustomChatUIProvider) obj;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return instance;

	}
	
	
	public static ECCustomChatPlusExtendProvider getCustomChatPlusExtendProvider() {

		ECCustomChatPlusExtendProvider instance = null;
		String name = ECKitCustomProviderManager
				.getCustomProviderName(ECCustomProviderEnum.CHATTING_PROVIDER);

		if (!TextUtils.isEmpty(name)) {
			try {
				Class clazz = Class.forName(name);
				Object obj = clazz.newInstance();

				if (obj instanceof ECCustomChatPlusExtendProvider) {

					instance = (ECCustomChatPlusExtendProvider) obj;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return instance;

	}
	
	
	
}