package cn.moon.lang.web;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Data
@AllArgsConstructor
public class Option {
    String label;
    Object value;


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




}
