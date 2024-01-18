package druidelf.annotation;

import lombok.extern.log4j.Log4j2;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * 校验枚举值指定属性的有效性
 */
@SuppressWarnings("unused")
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValue.Validator.class)
public @interface EnumValue {

    Class<? extends Enum<?>> value() default DefaultEnum.class;

    String message() default "{参数不符合规定要求}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> enumClass() default DefaultEnum.class;

    String enumField() default "statusCode";

    boolean isAllowNull() default false;

    @Log4j2
    class Validator implements ConstraintValidator<EnumValue, Object> {

        private Class<? extends Enum<?>> enumClass;
        private String enumField;
        private  boolean isAllowNull;

        @Override
        public void initialize(EnumValue enumValue) {
            enumField = enumValue.enumField();
            enumClass = enumValue.value();
            isAllowNull = enumValue.isAllowNull();
            if ( enumClass.getSimpleName().equals( "DefaultEnum" ) ) enumClass = enumValue.enumClass();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

            if ( isAllowNull && value == null){
                return Boolean.TRUE;
            }

            if (value == null ) {
                return Boolean.FALSE;
            }

            if (enumClass == null || enumField == null) {
                return Boolean.FALSE;
            }

            Class clazz = enumClass;
            if ( clazz.getSimpleName().equals( "DefaultEnum" )) {
                log.warn("没有定义需要验证的枚举类");
                return Boolean.FALSE;
            }
            Object[] objs = clazz.getEnumConstants();
            Field statusCode;

            try {
                statusCode = clazz.getDeclaredField(enumField);
            } catch (NoSuchFieldException e) {
                 log.error("默认属性"+enumField+"不存在，如果你的枚举里没有该属性，请使用EnumValue中的enumField来重新定义需要校验的属性名称");
                 throw new RuntimeException(e);
            }
            for (Object object : objs ) {
                try {
                    statusCode.setAccessible(true);
                    if (statusCode.get(object).equals(value)) {
                        return Boolean.TRUE;
                    }
                } catch (IllegalAccessException e) {
                    log.error("得不到"+enumField+"属性的值");
                    throw new RuntimeException( e);
                }
            }
        return Boolean.FALSE;
        }
    }

    enum DefaultEnum {
    }
}
