/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: Liu Shilei
 * @version: V1.0
 * @Date: 2016年11月8日 下午4:11:34
 */
package com.mz.core.mvc.controller.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mz.core.annotation.base.MethodName;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.util.excelUtil.ExcelUtil;
import com.mz.util.file.FileUtil;
import com.mz.util.http.HttpConnectionUtil;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Liu Shilei
 * @Date : 2016年11月8日 下午4:11:34
 */
@Controller
@RequestMapping("/export/exportutil")
public class ExportUtilController {

    @MethodName(name = "导出excel...loadding")
    @RequestMapping("/loadding")
    public String loadding(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = StringUtils.trim(request.getParameter(name));
            if (StringUtils.isNotBlank(value)) {

                map.put(name, value);
            }
        }
        request.setAttribute("map", map);

        return "loadding";
    }

    @MethodName(name = "下载excel")
    @RequestMapping("/down")
    @ResponseBody
    public void down(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filePathStr = request.getParameter("excel_filePath");
        if (!(filePathStr.endsWith(".xls") || filePathStr.endsWith("xlsx"))) {
            return;
        }

        if (filePathStr == null) {
            return;
        }
        File sendFile = new File(filePathStr);
        if (!sendFile.exists()) {
            return;
        }
        //下载
        FileUtil.download(sendFile, sendFile.getName(), request, response);
        System.out.println("send all content of file to client: " + sendFile.getCanonicalPath());
        //下载完删除文件
        sendFile.delete();
    }


    @MethodName(name = "导出excel")
    @RequestMapping("/excel")
    @ResponseBody
    public JsonResult excel(HttpServletRequest request, HttpServletResponse response) throws Exception {

        JsonResult jsonResult = new JsonResult();
        //表头
        String titles = request.getParameter("excel_titles");
        //显示的列
        String cvs = request.getParameter("excel_cvs");
        //sessionId
        String sessionId = request.getParameter("excel_sessionId");
        // 远端请求接口，先构建远端请求接口
        URL url;
        try {
            url = new URL(request.getParameter("excel_url"));
        } catch (MalformedURLException e) {
            URL requestUrl = new URL(request.getRequestURL().toString());
            url = new URL(requestUrl.getProtocol(), request.getRemoteAddr(), request.getRemotePort(), request.getParameter("excel_url"));
        }
        //文件名称
        String filename = request.getParameter("excel_filename");
        String[] headers = titles.split(",");
        String[] columns = cvs.split(",");

        //获得渲染规则
        //分别为  "参数位置 ":"计算规则:规则明细"
        //组成 1:VALUE_CONVERT:1=甲类,2=乙类,3=丙类,else=
        //或  1:VALUE_OPERATION: transactionMoney-fee
        //规则用  & 连接
        String renderRules = request.getParameter("excel_renderRules");
        HashMap<String, String[]> rulesMap = new HashMap<>();
        ArrayList<Integer> noshowLines = new ArrayList<>();//不显示的列
        if (renderRules != null && !"".equals(renderRules)) {
            String[] rules2 = null;
            if (renderRules.contains("&")) {
                String[] rules1 = renderRules.split("&");
                for (int i = 0; i < rules1.length; i++) {
                    rules2 = rules1[i].split(":");
                }
            } else {
                rules2 = renderRules.split(":");
            }
            if (rules2 != null && rules2.length == 3) {
                String[] mmm = new String[2];
                mmm[0] = rules2[1];
                mmm[1] = rules2[2];
                rulesMap.put(rules2[0], mmm);
            } else {
                System.out.println("渲染条件为:" + renderRules + ",不符合规则，请重新填写！");
                jsonResult.setMsg("渲染条件为:" + renderRules + ",不符合规则，请重新填写！");
                jsonResult.setSuccess(false);
                return jsonResult;
            }
        }

        Map<String, String> param = new HashMap<>();
        param.put("start", "0");
        param.put("length", "5000");
        param.put("msid", sessionId);
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = StringUtils.trim(request.getParameter(name));
            if (!name.contains("excel_")) {
                if (StringUtils.isNotBlank(value)) {
                    param.put(name, value);
                }
            }
        }

        String query = url.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                param.put(pair.split("=")[0], pair.split("=")[1]);
            }
        }

        StringBuffer _param = new StringBuffer();
        for (String key : param.keySet()) {
            _param.append(key);
            _param.append("=");
            _param.append(param.get(key));
            _param.append("&");
        }
        _param.deleteCharAt(_param.lastIndexOf("&"));
        String postSend = null;
        try {
            // 此处使用本地端口，减小因为网络的问题，导致请求失败
            URL otherUrl = new URL(url.getProtocol(), request.getLocalAddr(), request.getLocalPort(), url.getPath());
            postSend = HttpConnectionUtil.getSend(otherUrl.toString(), _param.toString());
        } catch (NumberFormatException e) {
            System.out.println("端口错误");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (postSend == null) {
            try {
                URL otherUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath());
                postSend = HttpConnectionUtil.getSend(otherUrl.toString(), _param.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (postSend == null) {
            jsonResult.setMsg("请求失败，无法导出excel");
            jsonResult.setSuccess(false);
            return jsonResult;
        }

        // 得到结果转换成pageResult
        ObjectMapper objectMapper = new ObjectMapper();
        PageResult pageResult = objectMapper.readValue(postSend, PageResult.class);
        List rows = pageResult.getRows();
        // 循环分析对象中是否包含对象属性,如果有则对a.b类型的字段名，进行附值操作
        if (rows != null && rows.size() > 0) {
            for (int i = 0; i < rows.size(); i++) {
                Map<String, Object> object = (Map<String, Object>) rows.get(i);
                for (int j = 0; j < columns.length; j++) {
                    String c = columns[j];
                    if (c.contains(".")) {
                        String[] split = c.split("\\.");
                        if (split.length == 2) {// 一级对象，包对象 a.b类型
                            Map<String, Object> son = (Map<String, Object>) object.get(split[0]);
                            object.put(c, son.get(split[1]));
                        }
                        if (split.length == 3) {// 二级对象，包对象 a.b.c类型
                            Map<String, Object> son = (Map<String, Object>) object.get(split[0]);
                            Map<String, Object> son2 = (Map<String, Object>) son.get(split[1]);
                            object.put(c, son2.get(split[2]));
                        }
                    }

                    //渲染赋值
                    if (rulesMap.containsKey(j + "")) {
                        String[] ruleAndValue = rulesMap.get(j + "");
                        String rule = ruleAndValue[0];//规则
                        String ruleValue = ruleAndValue[1];//规则详情

                        Object oldValue = object.get(c);
                        if (ExcelUtil.VALUE_CONVERT.equals(rule)) {//单纯值的替换
                            String value = ExcelUtil.getConvertValue(ruleValue, oldValue);
                            object.put(c, value);
                        } else if (ExcelUtil.VALUE_OPERATION.equals(rule)) {//值得只加或只减运算
                            BigDecimal value = ExcelUtil.getOperationValue(ruleValue, object);
                            //四舍五入保留三位小数
                            double dou = value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            object.put(c, dou);
                        } else if (ExcelUtil.NOT_SHOW.equals(rule) && !noshowLines.contains(j)) {//不显示的列
                            noshowLines.add(j);
                        }
                    }
                }
            }
        }
        try {
            //去掉不显示的列
            if (noshowLines != null && noshowLines.size() > 0) {
                headers = removeNoshowLines(headers, noshowLines);
                columns = removeNoshowLines(columns, noshowLines);
            }
            //生成并返回文件路径filename
            String path = ExcelUtil.exportExcel(headers, columns, pageResult.getRows(), filename);
            jsonResult.setMsg(path);
            jsonResult.setSuccess(true);
            return jsonResult;
            //下载
            //FileUtil.download(new File(path), filename+".xls", request, response);
            //下载完删除文件
            //FileUtil.deleteFile(path);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return jsonResult;
    }

    /**
     * 去掉不显示的列
     * <p> TODO</p>
     * @author: Zhang Lei
     * @param:    @return
     * @return: String[]
     * @Date :          2017年1月9日 下午5:35:15
     * @throws:
     */
    private String[] removeNoshowLines(String[] strs, ArrayList<Integer> list) {
        String[] val = new String[strs.length - list.size()];
        int num = 0;
        for (int i = 0; i < strs.length; i++) {
            if (list.contains(i)) {

            } else {
                val[num] = strs[i];
                num++;
            }
        }
        return val;
    }
}
