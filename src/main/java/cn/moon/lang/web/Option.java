package cn.moon.lang.web;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
public class Option {
    String label;
    Object value;


    // 兼容性处理
    public String getText() {
        return label;
    }

    public static <T> List<Option> convertList(Iterable<T> list, Function<T, Object> valueFn, Function<T, String> labelFn) {
        List<Option> result = new ArrayList<>();
        Iterator<T> ite = list.iterator();

        while(ite.hasNext()) {
            T t = ite.next();
            String label = labelFn.apply(t);
            Object value = valueFn.apply(t);
            result.add(new Option(label, value));
        }

        return result;
    }


    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Option) {
            Option other = (Option) obj;
            return Objects.equals(value, other.value);
        }

        return false;
    }

}
