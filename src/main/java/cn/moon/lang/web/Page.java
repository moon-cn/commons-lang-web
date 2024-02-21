package cn.moon.lang.web;


import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Page<T> {

	private Pageable pageable;

	private  long totalElements;
	private List<T> content;


	public Page(List<T> content ,Pageable pageable, long totalElements){
		this.content = content;
		this.pageable = pageable;
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return pageable.pageSize == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) pageable.pageSize);
	}


	public boolean hasNext() {
		return pageable.getPageNo() + 1 < getTotalPages();
	}


}
