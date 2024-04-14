package cn.moon.lang.web;

import lombok.Data;

import java.util.*;
import java.util.function.Function;

/**
 * antd 树状选择对象
 */
@Data
public class TreeOption {
    String title;
    Object key;

    Object parentKey;

    List<TreeOption> children = new ArrayList<>();


    boolean selectable = true;
    boolean checkable = true;
    boolean disabled = false;

    Boolean isLeaf;


    Object data;


    public Object getValue() {
        return key;
    }


    public TreeOption() {
    }

    public TreeOption(String title, Object key, Object parentKey) {
        this.title = title;
        this.key = key;
        this.parentKey = parentKey;
    }

    public TreeOption(String title, Object key, Object parentKey, boolean disabled) {
        this.title = title;
        this.key = key;
        this.parentKey = parentKey;
        this.disabled = disabled;
    }



    public static List<TreeOption> convertTree(List<TreeOption> list) {
        // 构建 map，方便快查询
        Map<Object, TreeOption> map = new LinkedHashMap<>();
        for (TreeOption option : list) {
            if (map.containsKey(option.getKey())) {
                throw new IllegalArgumentException("输入的原始列表中不能包含相同KEY " + option);
            }
            map.put(option.getKey(), option);
        }

        // 设置children
        List<TreeOption> root = new ArrayList<>();
        for (TreeOption t : list) {
            Object value = t.getKey();

            Object parentValue = t.getParentKey();
            TreeOption option = map.get(value);

            if (parentValue != null && map.containsKey(parentValue)) {
                TreeOption parent = map.get(parentValue);
                parent.children.add(option);
            } else {
                // 父节点为空，加入为根节点
                root.add(option);
            }
        }


        Collection<TreeOption> values = map.values();

        checkAndSetLeaf(values);


        return root;
    }

    private static void checkAndSetLeaf(Collection<TreeOption> values) {
        for (TreeOption v : values) {
            List<TreeOption> children = v.getChildren();
            boolean isLeaf = children == null || children.isEmpty();
            if (isLeaf) {
                v.setChildren(null);

            }
            v.setIsLeaf(isLeaf);
        }
    }




    public static <T> List<TreeOption> convertTree(Iterable<T> list, Function<T, Object> valueFn, Function<T, Object> parentValueFn, Function<T, String> labelFn) {
        return convertTree(list, valueFn, parentValueFn, labelFn, null);
    }


    public static <T> List<TreeOption> convertTree(Iterable<T> list, Function<T, Object> valueFn, Function<T, Object> parentValueFn, Function<T, String> labelFn, Function<T, Boolean> selectableFn) {

        List<TreeOption> treeList = new ArrayList<>();
        for (T t : list) {
            String label = labelFn.apply(t);
            Object value = valueFn.apply(t);

            TreeOption option = new TreeOption(label, value, parentValueFn.apply(t));

            if (selectableFn != null) {
                Boolean selectable = selectableFn.apply(t);
                if (selectable != null) {
                    option.setCheckable(selectable);
                    option.setSelectable(selectable);
                }
            }
            treeList.add(option);
        }

        return convertTree(treeList);
    }


}
