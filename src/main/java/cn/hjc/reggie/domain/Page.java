package cn.hjc.reggie.domain;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

    //总记录数
    private long total;
    //当前页码
    private long current;
    //每页显示条数
    private long pageSize;
    //数据
    private List<T> records;

    public Page() {
    }

    public Page(long total, long current, long pageSize, List<T> records) {
        this.total = total;
        this.current = current;
        this.pageSize = pageSize;
        this.records = records;
    }
}
