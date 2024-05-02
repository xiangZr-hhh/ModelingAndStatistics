package com.modeling.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * <h1>错误码</h1>
 * <hr/>
 * 用于定义错误码
 *
 * @author xiao_lfeng
 * @version v1.1.0
 * @since v1.0.0
 */
@Slf4j
@Getter
public enum ErrorCode {
    WRONG_PASSWORD("WrongPassword", 40010, "密码错误"),
    PARAMETER_ERROR("ParameterError", 40011, "参数错误"),
    REQUEST_BODY_ERROR("RequestBodyError", 40012, "请求体错误"),
    USER_EXIST("UserExist", 40013, "用户名已存在"),
    TIMESTAMP_ERROR("TimestampError", 40014, "时间戳错误"),
    USER_NOT_EXIST("UserNotExist", 40015, "用户不存在"),
    NOT_PERMISSION("NotPermission", 40019, "没有权限"),
    UNAUTHORIZED("Unauthorized", 40100, "未授权"),
    TOKEN_EXPIRED("TokenExpired", 40101, "Token已过期"),
    VERIFICATION_INVALID("VerificationInvalid", 40102, "验证码无效"),
    TOKEN_NOT_EXIST("TokenNotExist", 40103, "Token不存在"),
    CLASS_COPY_EXCEPTION("ClassCopyException", 40104, "实体类拷贝异常"),
    USER_IS_LOCKED("UserIsLocked", 40300, "用户已被锁定"),
    USER_IS_DEACTIVATED("UserIsDeactivated", 40301, "用户已被禁用"),
    NOT_SAME("PasswordNotSame", 40304, "两次密码不一致"),
    USER_ALREADY_DELETE("UserAlreadyDelete", 40306, "用户已被删除"),
    USER_DISABLED("UserDisabled", 40307, "用户已被禁用"),
    USER_LOCKED("UserLocked", 40308, "用户已被锁定"),
    USER_EXPIRED("UserExpired", 40309, "用户已过期"),
    ID_NOT_EXIST("IdNotExist", 40400, "ID不存在"),
    ROLE_NOT_FOUNDED("RoleNotFounded", 40401, "角色不存在"),
    ROLE_NAME_REPEAT("RoleNameRepeat", 40402, "角色名称重复"),
    MESSAGE_ONLY_DELETE_BY_THEMSELVES("MessageOnlyDeleteByThenSelves", 40500, "用户只能删除自己的消息"),
    PERMISSION_NOT_EXIST("permissionNotExist", 40501, "权限不存在"),
    DATABASE_INSERT_ERROR("DatabaseInsertError", 50010, "数据库插入错误"),
    DATABASE_UPDATE_ERROR("DatabaseUpdateError", 50011, "数据库更新错误"),
    DATABASE_DELETE_ERROR("DatabaseDeleteError", 50012, "数据库删除错误"),

    DATA_TABLE_EXIST("DataTableExist", 60001, "Excel数据表已存在"),
    EXCEL_FILE_IS_NULL("ExcelFileIsNull", 60002, "Excel文件为空"),
    EXCEL_COVER_EXCEPTION("ExcelCoverException", 60003, "Excel转换错误"),
    EXCEL_READ_EXCEPTION("ExcelReadException", 60004, "Excel文件读取错误"),
    FILE_FORMAT_NOT_SUPPORTED("FileFormatNotSupported", 60005, "文件类型不支持"),
    EXCEL_SHEET_HEADER_CONTENT_NULL("ExcelSheetHeaderContentNull", 60006, "Excel表头为空"),
    EXCEL_SHEET_HEADER_INCONSISTENT("ExcelSheetHeaderInconsistent", 60007, "Excel表头错误"),
    EXCEL_SHEET_DATA_FORMAT_ERROR("ExcelSheetDataFormatError", 60008, "Excel数据格式错误"),
    EXCEL_DATA_NULL("ExcelDataNull", 60009,"Excel数据内容为空"),
    TABLE_DATA_NOT_EXIST("TableDataNotExist",60010,"数据表不存在"),
    FILED_NOT_EXIST("FiledNotExist",60011 ,"列名不存在"),
    FILED_TYPE_NOT_TRUE("FiledTypeNotTrue",60012 ,"列类型不一致" );


    private final String output;
    private final Integer code;
    private final String message;

    ErrorCode(String output, Integer code, String message) {
        this.output = output;
        this.code = code;
        this.message = message;
    }

}