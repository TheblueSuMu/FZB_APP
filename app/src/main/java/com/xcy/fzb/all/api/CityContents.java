package com.xcy.fzb.all.api;

public class CityContents {
    static String familyId = "";

    static String cityType = "";

    static String ReadRecordStatus = "";

    static String OneKey = "";

    static String addClient = "";

    static String WeChatType = "";

    static String WeChatJson = "";

    static String IsRead = "";

    public static String getIsRead() {
        return IsRead;
    }

    public static void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public static String getWeChatType() {
        return WeChatType;
    }

    public static void setWeChatType(String weChatType) {
        WeChatType = weChatType;
    }

    public static String getWeChatJson() {
        return WeChatJson;
    }

    public static void setWeChatJson(String weChatJson) {
        WeChatJson = weChatJson;
    }

    public static String getAddClient() {
        return addClient;
    }

    public static void setAddClient(String addClient) {
        CityContents.addClient = addClient;
    }

    public static String getOneKey() {
        return OneKey;
    }

    public static void setOneKey(String oneKey) {
        OneKey = oneKey;
    }

    public static String getReadRecordStatus() {
        return ReadRecordStatus;
    }

    public static void setReadRecordStatus(String readRecordStatus) {
        ReadRecordStatus = readRecordStatus;
    }

    public static String getCityType() {
        return cityType;
    }

    public static void setCityType(String cityType) {
        CityContents.cityType = cityType;
    }

    public static String getFamilyId() {
        return familyId;
    }

    public static void setFamilyId(String familyId) {
        CityContents.familyId = familyId;
    }
}
