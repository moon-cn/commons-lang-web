package cn.moon.lang.web.persistence;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 查询条件
 * 多对多可 可使用 containsAnyMember
 */
public class Query implements Specification {

    private final List<Specification> specificationList = new ArrayList<>();


    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
        if (specificationList.isEmpty()) {
            return builder.conjunction();
        }

        if (specificationList.size() == 1) {
            return specificationList.get(0).toPredicate(root, query, builder);
        }

        List<Predicate> predicates = new ArrayList<>();
        for (Specification c : specificationList) {
            Predicate predicate = c.toPredicate(root, query, builder);
            if (predicate != null) {
                predicates.add(predicate);
            }
        }

        // 将所有条件用 and 联合起来
        return builder.and(predicates.toArray(new Predicate[0]));
    }


    public void eq(String column, Object value) {
        this.addSpecification((Specification) (root, query, builder) -> {
            Expression expression = getExpression(column, root);
            return value == null ? builder.isNull(expression) : builder.equal(expression, value);
        });

    }

    public void isNull(String column) {
        this.addSpecification((Specification) (root, query, builder) -> {
            Expression expression = getExpression(column, root);
            return builder.isNull(expression);
        });
    }

    public void isNotNull(String column) {
        this.addSpecification((Specification) (root, query, builder) -> {
            Expression expression = getExpression(column, root);
            return builder.isNotNull(expression);
        });

    }


    public void ne(String column, Object value) {
        this.addSpecification((Specification) (root, query, builder) -> {
            Expression expression = getExpression(column, root);
            return value == null ? builder.isNotNull(expression) : builder.notEqual(expression, value);
        });
    }


    public void gt(String column, Object value) {
        this.addSpecification((Specification) (root, query, builder) -> {
            Expression expression = getExpression(column, root);
            return builder.lessThan(expression, (Comparable) value);
        });
    }


    public void ge(String column, Object val) {
        this.addSpecification((Specification) (root, query, builder) -> {
            Expression expression = getExpression(column, root);
            return builder.greaterThanOrEqualTo(expression, (Comparable) val);
        });
    }


    public void lt(String column, Object val) {
        this.addSpecification((Specification) (root, query, builder) -> {
            Expression expression = getExpression(column, root);
            return builder.lessThan(expression, (Comparable) val);
        });
    }


    public void le(String column, Object val) {
        this.addSpecification((Specification) (root, query, builder) -> {
            Expression expression = getExpression(column, root);
            return builder.lessThanOrEqualTo(expression, (Comparable) val);
        });
    }


    public void between(String column, Object v1, Object v2) {
        this.addSpecification(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
                Expression expression = getExpression(column, root);
                Predicate a = builder.greaterThanOrEqualTo(expression, (Comparable) v1);

                Predicate b = builder.lessThanOrEqualTo(expression, (Comparable) v2);

                return builder.and(a, b);
            }
        });


    }


    public void notBetween(String column, Object v1, Object v2) {
        this.addSpecification(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
                Expression expression = getExpression(column, root);
                Predicate a = builder.lessThan(expression, (Comparable) v1);

                Predicate b = builder.greaterThan(expression, (Comparable) v2);

                return builder.and(a, b);
            }
        });


    }

    public void like(String column, Object val) {
        if (val == null) {
            this.eq(column, null);
            return;
        }

        this.addSpecification(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                Expression expression = getExpression(column, root);

                String str = val.toString();
                str = str.contains("%") ? str : "%" + str + "%";

                return criteriaBuilder.like(expression, str);
            }
        });
    }

    /**
     * ignore
     */
    public void notLike(String column, Object val) {
        if (val == null) {
            this.ne(column, null);
            return;
        }

        this.addSpecification(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                Expression expression = getExpression(column, root);

                String str = val.toString();
                str = str.contains("%") ? str : "%" + str + "%";

                return criteriaBuilder.notLike(expression, str);
            }
        });


    }


    public void likeLeft(String column, Object val) {
        if (val != null) {
            val = "%" + val;
        }

        this.like(column, val);
    }


    public void likeRight(String column, Object val) {
        if (val != null) {
            val += "%";
        }

        this.like(column, val);
    }


    public void in(String column, Iterable valueList) {
        List<Object> params = new ArrayList<>();
        if (valueList != null) {
            for (Object obj : valueList) {
                params.add(obj);
            }
        }

        this.in(column, params.toArray());
    }

    public Query notIn(String column, Iterable valueList) {
        List<Object> objs = new ArrayList<>();
        for (Object obj : valueList) {
            objs.add(obj);
        }
        return this.notIn(column, objs.toArray());

    }

    public void in(String column, Object... valueList) {
        boolean hasValue = valueList != null && valueList.length > 0;

        if (!hasValue) {
            // in 空值， 相当于 1!=1, 直接没有数据返回了，这里用 is null and is not null 表示false
            this.eq(column, null);
            this.ne(column, null);
            return ;
        }


        this.addSpecification((Specification) (root, query, cb) -> {
            Expression expression = getExpression(column, root);
            CriteriaBuilder.In<Object> in = cb.in(expression);
            for (Object value : valueList) {
                Assert.notNull(value, "in 中不应包含null");
                in.value(value);
            }

            return in;
        });
    }


    public Query notIn(String column, Object... valueList) {
        Assert.state(valueList != null && valueList.length > 0, "notIn 方法的参数应大于0");
        this.addSpecification((Specification) (root, query, cb) -> {
            Expression expression = getExpression(column, root);
            CriteriaBuilder.In<Object> in = cb.in(expression);
            for (Object value : valueList) {
                Assert.notNull(value, "not in 中不应包含null");
                in.value(value);
            }

            // 注意，sql 中的not in 不会处理null值，这里or 一下
            return cb.or(in.not(), cb.isNull(expression));
        });
        return this;
    }


    public void addSpecification(Specification e) {
        if (e != null) {
            specificationList.add(e);
        }
    }




    public void addOr(Specification... specifications) {
        this.addSpecification((Specification) (root, query, criteriaBuilder) -> {
            Predicate[] arr = new Predicate[specifications.length];
            for (int i = 0, specificationsLength = specifications.length; i < specificationsLength; i++) {
                Specification specification = specifications[i];
                Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);
                arr[i] = predicate;
            }
            return criteriaBuilder.or(arr);
        });

    }

    public void like(Map<String, Object> param) {
        Set<Map.Entry<String, Object>> entries = param.entrySet();
        for (Map.Entry<String, Object> e : entries) {
            String key = e.getKey();
            Object value = e.getValue();

            if (value != null) {
                if (value instanceof String) {
                    this.like(key, value);
                } else {
                    this.eq(key, value);
                }
            }

        }
    }

    // 去重复
    public void distinct() {
        this.addSpecification((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            return criteriaBuilder.conjunction();
        });
    }

    /**
     * 获得字段路径，
     *
     * @param fieldName 字段包含小数点 .
     * @param root
     * @param
     * @return
     */
    private static Expression getExpression(String fieldName, Root root) {
        // 普通字段
        if (!fieldName.contains(".")) {
            Attribute.PersistentAttributeType type = root.getModel().getAttribute(fieldName).getPersistentAttributeType();
            boolean isMany = type == Attribute.PersistentAttributeType.ONE_TO_MANY
                    || type == Attribute.PersistentAttributeType.MANY_TO_MANY;

            // boolean member = operator == Operator.IS_MEMBER || operator == Operator.IS_NOT_MEMBER;

            if (isMany /*&& !member*/) {
                return getOrCreateJoin(root, fieldName);
            } else {
                return root.get(fieldName);
            }
        }
        Expression expression = root;
        String[] names = fieldName.split("\\.");

        for (String name : names) {
            Class clazz = expression.getJavaType();
            if (clazz.equals(Set.class)) {
                expression = root.joinSet(name);
            } else if (clazz.equals(List.class)) {
                expression = root.joinList(name);
            } else if (clazz.equals(Map.class)) {
                expression = root.joinMap(name);
            } else {
                expression = ((Path) expression).get(name);
            }
        }

        return expression;

    }

    private static Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute) {
        for (Join<?, ?> join : from.getJoins()) {
            boolean sameName = join.getAttribute().getName().equals(attribute);

            if (sameName && join.getJoinType().equals(JoinType.LEFT)) {
                return join;
            }
        }

        return from.join(attribute, JoinType.LEFT);
    }


}
