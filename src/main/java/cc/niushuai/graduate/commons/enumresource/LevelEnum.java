package cc.niushuai.graduate.commons.enumresource;


public enum LevelEnum implements EnumMessage {
    ONE("1", "普通vip"),
    TWO("2", "高级vip"),
    THREE("3", "至尊vip");
    private final String code;
    private final String value;

    LevelEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }
}
