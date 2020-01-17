package com.freedom.jlbatross.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Description: 自定义响应数据结构
 * 				200：表示成功
 * 				500：表示错误，错误信息在msg字段中
 * 				501：bean验证错误，不管多少个错误都以map形式返回
 * 				502：拦截器拦截到用户token出错
 * 				555：异常抛出信息
 */
public class JSONResult {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;
    
    // 附加数据
    private Object attach;

    public static JSONResult ok(Object data,String msg,Object attach) {
        return new JSONResult(data,msg,attach);
    }
    
    public static JSONResult build(Integer status, String msg, Object data) {
        return new JSONResult(status, msg, data);
    }

    public static JSONResult ok(Object data,String msg) {
        return new JSONResult(data,msg);
    }
    
    public static JSONResult ok(Object data,Object attach) {
    	return new JSONResult(data,attach);
    }
    
    public static JSONResult ok(Object data) {
        return new JSONResult(data);
    }

    public static JSONResult ok(String msg) {
        return new JSONResult(null,msg);
    }
    
    public static JSONResult ok() {
        return new JSONResult(null);
    }
    
    public static JSONResult errorMsg(String msg) {
        return new JSONResult(500, msg, null);
    }
    
    public static JSONResult errorMap(Object data) {
        return new JSONResult(501, "error", data);
    }
    
    public static JSONResult errorTokenMsg(String msg) {
        return new JSONResult(502, msg, null);
    }
    
    public static JSONResult errorException(String msg) {
        return new JSONResult(555, msg, null);
    }

    public static JSONResult updateResult(int count) {
        if(count == 0) {
            return JSONResult.ok("操作成功");
        } else {
            return JSONResult.errorMsg("操作失败");
        }
    }

    public JSONResult() {

    }

    public JSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public JSONResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }
    
    public JSONResult(Object data, String msg) {
    	this.status = 200;
    	this.msg =msg;
    	this.data = data;
    }
    
    public JSONResult(Object data, Object attach) {
    	this.status = 200;
    	this.msg = "OK";
    	this.data = data;
    	this.attach=attach;
    }
    public JSONResult(Object data, String msg, Object attach) {
    	this.status = 200;
    	this.msg = "OK";
    	this.data = data;
    	this.msg=msg;
    	this.attach=attach;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	public Object getAttach() {
		return attach;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}

	/**
	 * @Decription: 获取异常信息
	 * @Author: liuxiangtao90
	 * @CreateDate: Created in 2019/2/2 14:12
	 * @param:
	 * @param e
	 * @Return: java.lang.String
	 */
	public static String getExceptionString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
}
