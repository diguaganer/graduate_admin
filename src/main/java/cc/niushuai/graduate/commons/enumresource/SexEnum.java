package cc.niushuai.graduate.commons.enumresource;

public enum SexEnum implements EnumMessage {
    MEN("1", "男"),
    WOMEN("2", "女"),
    UNKNOWN("0", "未知");
    private final String code;
    private final String value;

    SexEnum(String code, String value) {
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
