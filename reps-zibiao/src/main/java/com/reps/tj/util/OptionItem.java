package com.reps.tj.util;

import java.io.Serializable;


/**
 * 下拉选择框选项
 * 
 * @author qianguobing
 * @date 2017年8月7日 下午2:39:42
 *
 */
public class OptionItem implements Serializable {

    private static final long serialVersionUID = 3223949184958632506L;
    private String id = "";
    private String text = "";

    public OptionItem() {

    }

    public OptionItem(String id, String text) {
        this.id = id;
        this.text = text;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
