package com.yule.dashboard.pbl.utils;

import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.exception.ServerException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LogicUtils {


    public static boolean isAllNull(Object... objs) {
        for (Object obj : objs) {
            if (obj != null) return true;
        }
        return false;
    }

    public static void ifAllNullThrow(Object... objs) {
        if (!isAllNull(objs)) {
            throw new ClientException(ExceptionCause.CAN_NOT_BE_ALL_NULL);
        }
    }
}
