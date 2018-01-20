/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-01-20 16:25 创建
 */
package org.antframework.ids;

import org.antframework.common.util.id.IdGenerator;
import org.antframework.common.util.id.PeriodType;
import org.antframework.idcenter.client.Id;
import org.antframework.idcenter.client.IdContext;

/**
 * 唯一id（unique id）
 */
public class UID {
    // id长度
    private static final int ID_LENGTH = 20;
    // id上下文
    private static final IdContext ID_CONTEXT = IdsParams.createIdContext("common-uid");
    // workerId
    private static final int WORKER_ID = IdsParams.getWorkId(75000);
    // id生成器
    private static final IdGenerator ID_GENERATOR = IdsParams.createIdGenerator("common-uid", PeriodType.DAY, 10000000L);

    /**
     * 创建新的id
     */
    public static String newId() {
        String id = fromServer();
        if (id == null) {
            id = fromLocal();
        }
        return id;
    }

    /**
     * 创建新的long类型id
     *
     * @return
     */
    public static long newLongId() {
        return toLong(newId());
    }

    /**
     * 转换成long类型id
     *
     * @param id 字符串类型id
     * @return long类型id
     */
    public static Long toLong(String id) {
        return Long.parseLong(id.substring(1));
    }

    /**
     * 转换成字符串类型id
     *
     * @param id long类型id
     * @return 字符串类型id
     */
    public static String toString(long id) {
        return "2" + String.format("%0" + (ID_LENGTH - 1) + "d", id);
    }

    // 从服务端获取id（格式：yyyyMMddHH+10位数的id）
    private static String fromServer() {
        Id id = ID_CONTEXT.getAcquirer().getId();
        if (id == null) {
            return null;
        }
        // 构建id
        StringBuilder builder = new StringBuilder(ID_LENGTH);
        builder.append(id.getPeriod().toString());
        builder.append(String.format("%0" + (ID_LENGTH - builder.length()) + "d", id.getId()));
        return builder.toString();
    }

    // 从本地获取id（格式：yyyyMMdd+5位数的workerId+7位数的id）
    private static String fromLocal() {
        org.antframework.common.util.id.Id id = ID_GENERATOR.getId();
        // 构建id
        StringBuilder builder = new StringBuilder(ID_LENGTH);
        builder.append(id.getPeriod().toString());
        builder.append(Integer.toString(25000 + WORKER_ID));
        builder.append(String.format("%0" + (ID_LENGTH - builder.length()) + "d", id.getId()));
        return builder.toString();
    }
}
