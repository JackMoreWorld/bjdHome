package com.bgd.support.base;

public enum FileTypeEnum implements DictionaryItem {
    OTHER {
        @Override
        public String toCode() {
            return OTHER_CODE;
        }

        @Override
        public String toName() {
            return OTHER_NAME;
        }
    },
    DOCUMENT {
        @Override
        public String toCode() {
            return DOCUMENT_CODE;
        }

        @Override
        public String toName() {
            return DOCUMENT_NAME;
        }

    },
    PICTURE {
        @Override
        public String toCode() {
            return PICTURE_CODE;
        }

        @Override
        public String toName() {
            return PICTURE_NAME;
        }
    };

    private static final String ENUM_NAME = "文件类型";
    private static final String ENUM_CODE = "FILE_TYPE";

    private static String OTHER_CODE = "OTH";
    private static String OTHER_NAME = "其他";
    private static String DOCUMENT_CODE = "DOC";
    private static String DOCUMENT_NAME = "文档";
    private static String PICTURE_CODE = "PIC";
    private static String PICTURE_NAME = "图片";

    @Override
    public String getEnumCode() {
        return ENUM_CODE;
    }

    @Override
    public String getEnumName() {
        return ENUM_NAME;
    }

    @Override
    public FileTypeEnum parseByCode(String code) {
        for (FileTypeEnum item : FileTypeEnum.values()) {
            if (item.toCode().equals(code))
                return item;
        }
        return null;
    }

    @Override
    public FileTypeEnum parseByName(String name) {
        for (FileTypeEnum item : FileTypeEnum.values()) {
            if (item.toName().equals(name))
                return item;
        }
        return null;
    }
}
