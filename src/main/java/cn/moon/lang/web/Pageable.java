package cn.moon.lang.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Data
public class Pageable {

    /**
     * 从 1 开始计算
     * indexed on one
     */
    boolean oneIndexed = true;

    int pageNo = 1;
    int pageSize = 20;

    public Pageable() {
    }

    public Pageable(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Pageable(boolean oneIndexed, int pageNo, int pageSize) {
        this.oneIndexed = oneIndexed;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public void convertToZeroIndexed(){
        if(this.oneIndexed){
            pageNo = pageNo - 1;
            this.oneIndexed = false;
        }
    }

    public static Pageable of(int page, int size) {
        return new Pageable(page, size);
    }

    public long getOffset() {
        return (long) (pageNo-1) * (long) pageSize;
    }
}
