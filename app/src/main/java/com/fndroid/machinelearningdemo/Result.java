package com.fndroid.machinelearningdemo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */

public class Result {

    /**
     * from : en
     * to : zh
     * trans_result : [{"src":"portrait,people,one,adult,facial expression,happiness,fun,wear,
     * man,child,face,boy,woman,joy,recreation,girl,festival,smile,laughing,fashion,",
     * "dst":"人像，人，一，成人，面部表情，幸福，快乐，穿着，男人，孩子，脸，男孩，女人，快乐，娱乐，女孩，节日，微笑，大笑，时尚，"}]
     */

    private String from;
    private String to;
    /**
     * src : portrait,people,one,adult,facial expression,happiness,fun,wear,man,child,face,boy,
     * woman,joy,recreation,girl,festival,smile,laughing,fashion,
     * dst : 人像，人，一，成人，面部表情，幸福，快乐，穿着，男人，孩子，脸，男孩，女人，快乐，娱乐，女孩，节日，微笑，大笑，时尚，
     */

    private List<TransResultBean> trans_result;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TransResultBean> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<TransResultBean> trans_result) {
        this.trans_result = trans_result;
    }

    public static class TransResultBean {
        private String src;
        private String dst;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDst() {
            return dst;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }
    }
}
