package com.liu.aop.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jack.Cheng
 * @date  2019/7/30 16:43
 * @Desc 解决批量插入/更新重复代码问题
 **/
@Slf4j
public class CommonUtil {

    /**
     * 将大集合数据进行拆分，拆分为num大小的小集合，key：拆分时计算的index值，value：对应拆分后集合
     * @param originList 原始大集合数据
     * @param num 数据拆分粒度
     * @return 递增自然数key，集合为value的map
     */
    public static Map<Integer, List<?>> resolveListToMap(List<?> originList, Integer num) {
        Map<Integer, List<?>> resolvedMap = new HashMap<>();
        if(null != originList && null != num && originList.size() > 0 && num > 0){
            int size = originList.size();
            //插入循环次数
            int insertTimes = size/num;
            if(0 != size%num){
                insertTimes++;
            }
            for(int i=0; i<insertTimes; i++){
                int startIndex = i * num;
                int endIndex = (i + 1) * num;
                if(endIndex > size){
                    endIndex = size;
                }
                List<?> subList = originList.subList(startIndex,endIndex);
                resolvedMap.put(i,subList);
            }
            return resolvedMap;
        }
        return resolvedMap;
    }

    /**
     * 计算两个数字百分比
     * @param molecule 分子
     * @param denominator 分母
     * @return 计算后的百分比
     */
    public static String calculatePercent(int molecule , int denominator){
        if(denominator == 0){
            return String.valueOf(0);
        }
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        try {
            return numberFormat.format((float)molecule/(float)denominator * 100);
        }catch (Exception e){
            log.error("计算百分比异常,molecule:{},denominator{}",molecule,denominator);
            return String.valueOf(0);
        }
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     * @return ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.contains(",") ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 字符串匹配
     * @param beMatchedStr 需要匹配的字符串
     * @param endList 匹配串集合
     * @return 匹配成功true，
     */
    public static boolean isEndWithListStr(String beMatchedStr, List<String> endList) {
        for (String endStr : endList) {
            if(beMatchedStr.endsWith(endStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 集合拆分
     * @param list 原始集合
     * @param page 拆分开始下标
     * @param limit 拆分结束下标
     * @return 拆分后的集合
     */
    public static List pageList(List<?> list, int page, int limit){
        if(list == null){
            return null;
        }
        int startIndex = (page-1)*limit;
        if(startIndex < 0){
            startIndex = 0;
        }
        if(startIndex > list.size()-1){
            return null;
        }
        int endIndex = page*limit;
        if(endIndex > list.size()){
            endIndex = list.size();
        }
        return list.subList(startIndex,endIndex);
    }
}
