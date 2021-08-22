package cc.carm.plugin.userprefix.util.gui;


import java.util.ArrayList;
import java.util.List;

public abstract class PagedGUI extends GUI {

    List<GUIItem> container = new ArrayList<>();
    public int page = 1;

    public PagedGUI(GUIType type, String name) {
        super(type, name);
    }

    public int addItem(GUIItem i) {
        container.add(i);
        return container.size() - 1;
    }

    /**
     * 从GUI中移除一个物品
     *
     * @param item 物品
     */
    public void removeItem(GUIItem item) {
        container.remove(item);
    }

    /**
     * 从GUI中移除一个物品
     *
     * @param slot 物品格子数
     */
    public void removeItem(int slot) {
        container.remove(slot);
    }

    public List<GUIItem> getItemsContainer() {
        return new ArrayList<>(container);
    }

    /**
     * 前往上一页
     */
    public void goPreviousPage() {
        if (hasPreviousPage())
            page--;
        else
            throw new IndexOutOfBoundsException();
    }


    /**
     * 前往下一页
     */
    public void goNextPage() {
        if (hasNextPage())
            page++;
        else
            throw new IndexOutOfBoundsException();
    }


    /**
     * @return 是否有上一页
     */
    public abstract boolean hasPreviousPage();

    /**
     * @return 是否有下一页
     */
    public abstract boolean hasNextPage();

}
