package dev.blu3.npcfiles.utils;

import com.pixelmongenerations.core.enums.EnumNPCType;

import java.util.HashMap;

public class DataGSON {

    public HashMap<String, HashMap<EnumNPCType, String>> data = new HashMap();

    public HashMap<String, HashMap<EnumNPCType, String>> getData() { return this.data; }

    public void setData(HashMap<String, HashMap<EnumNPCType, String>> data) { this.data = data; }
}
