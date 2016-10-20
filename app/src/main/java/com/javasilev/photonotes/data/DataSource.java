package com.javasilev.photonotes.data;

import java.util.List;

/**
 * Created by Aleksei Vasilev.
 */

public interface DataSource<Data> {
	Data createOrUpdate(Data data);
	Data getItem(long itemId);
	void deleteItem(long itemId);
	List<Data> getAll();
	List<Data> find(String searchQuery);
}
