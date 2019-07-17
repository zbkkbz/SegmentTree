/**
 * @author ZBK
 * @date 2019/7/16 - 0:16
 */
//创建此接口,创建merge方法,用户可以通过实现来自定义
    //此接口可以理解为前面优先队列的的比较器,实现接口,实现自定义方法
public interface Merger<E> {

    E merge(E a,E b);
}
