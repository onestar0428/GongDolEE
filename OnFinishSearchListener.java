package example.com.mptermui;

/**
 * Created by YY on 2016-05-31.
 */

import java.util.List;

public interface OnFinishSearchListener {
    public void onSuccess(List<Item> itemList);
    public void onFail();
}
