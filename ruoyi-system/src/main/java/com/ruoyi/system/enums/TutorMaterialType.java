package com.ruoyi.system.enums;

/**
 * 教员审核材料类型及其存储目录。
 */
public enum TutorMaterialType {
    ID_CARD_FRONT(1, "sfz_front", "身份证正面"),
    ID_CARD_BACK(2, "sfz_back", "身份证反面"),
    STUDENT_CARD(3, "xsz", "学生证"),
    CERTIFICATE(4, "certification", "证书");

    private final int code;
    private final String directoryName;
    private final String displayName;

    TutorMaterialType(int code, String directoryName, String displayName) {
        this.code = code;
        this.directoryName = directoryName;
        this.displayName = displayName;
    }

    public int getCode() {
        return code;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TutorMaterialType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TutorMaterialType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
