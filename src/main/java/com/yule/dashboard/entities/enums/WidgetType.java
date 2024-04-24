package com.yule.dashboard.entities.enums;

import com.yule.dashboard.entities.BookMark;
import com.yule.dashboard.entities.Utils;
import com.yule.dashboard.pbl.exception.ServerException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum WidgetType {
    BOOKMARK(BookMark.class), UTILS(Utils.class);

    private Class<?> bookMarkClass;

    WidgetType(Class<?> bookMarkClass) {
        this.bookMarkClass = bookMarkClass;
    }

    public static WidgetType getByClass(Class<?> type) {
        if (!(type.equals(BookMark.class) || type.equals(Utils.class))) throw new ServerException();
        return Arrays.stream(WidgetType.values()).filter(w -> w.getBookMarkClass().equals(type))
                .findFirst().orElseThrow(ServerException::new);
    }
}
