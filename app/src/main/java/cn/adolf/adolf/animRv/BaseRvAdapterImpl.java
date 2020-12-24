package cn.adolf.adolf.animRv;

/**
 * @program: LoveWidget
 * @description:
 * @author: Adolf
 * @create: 2020-11-09 17:18
 **/
interface BaseRvAdapterImpl {

    void moveItem(int from, int to);

    void delItem(int position);

    interface ViewHolderImpl { // 拖拽或滑动时，控制View的变化

        void onItemSelected();

        void onItemClear();
    }
}
