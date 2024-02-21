package cn.moon.lang.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pageable {

    /**
     * 从 1 开始计算
     * indexed on one
     */

    int pageNo;
    int pageSize;


    public static Pageable of(int page, int size) {
        return new Pageable(page, size);
    }

    public long getOffset() {
        return (long) (pageNo-1) * (long) pageSize;
    }
}
