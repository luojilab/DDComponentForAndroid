package com.luojilab.router.facade.model;

import com.luojilab.router.facade.enums.NodeType;

import java.util.Map;

import javax.lang.model.element.Element;

/**
 * <p><b>Package:</b> com.luojilab.router.facade.model </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> Node </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/9/19.
 */

public class Node {
    private NodeType nodeType;
    private Element rawType;        // Raw type of route
    private Class<?> destination;   // Destination
    private String path;            // Path of route
    private String desc;            // Desc of route
    private int priority = -1;      // The smaller the number, the higher the priority
    private Map<String, Integer> paramsType;
    private Map<String, String> paramsDesc;

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public Element getRawType() {
        return rawType;
    }

    public void setRawType(Element rawType) {
        this.rawType = rawType;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Map<String, Integer> getParamsType() {
        return paramsType;
    }

    public void setParamsType(Map<String, Integer> paramsType) {
        this.paramsType = paramsType;
    }

    public Map<String, String> getParamsDesc() {
        return paramsDesc;
    }

    public void setParamsDesc(Map<String, String> paramsDesc) {
        this.paramsDesc = paramsDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
